(ns tasks
  (:require
   [babashka.curl :as curl]
   [babashka.fs :as fs]
   [cheshire.core :as json]
   [clojure.string :as str]
   [selmer.parser :refer [render-file]]))

(def now (java.time.LocalDate/now))
(def current-year (.getYear now))
(def current-day (if (== current-year 2024)
                   ;; 2024 started a day early.
                   (inc (.getDayOfMonth now))
                   (.getDayOfMonth now)))

(def today {:year current-year :day current-day})

(def aoc-url "https://adventofcode.com")

(defn- problem-url [y d] (str aoc-url "/" y "/day/" d))
(defn- input-url [y d] (str (problem-url y d) "/input"))

(defn- source-dir [y] (str "src/aoc" y))
(defn- source-path [y d] (format "%s/day%02d.clj" (source-dir y) d))
(defn- test-dir [y] (str "test/aoc" y))
(defn- test-path [y d] (format "%s/day%02d_test.clj" (test-dir y) d))
;; TODO: add a way to override this with an env var.
(defn- input-dir [y] (str "../inputs/" y))
(defn- input-path [y d] (format "%s/%02d_input.txt" (input-dir y) d))
(defn- answer-path [y d] (format "%s/%02d_answer.json" (input-dir y) d))

(def days-file "src/aoc/days.clj")

(defn load-session []
  (try
    (str/trim (slurp "../.session"))
    (catch Exception _ nil)))

(defn aoc-headers []
  (let [session (load-session)
        headers {:headers
                 {"UserAgent" "git@github.com:darrenaustin/advent-of-code.git by Darren Austin"}
                 :throw false}]
    (if session
      (update-in headers [:headers] assoc "Cookie" (str "session=" session))
      headers)))

(defn- parse-day-spec [s]
  (if-let [[_ year _ day] (re-find #"(\d+)(\.(\d+))?" s)]
    {:year (when year (Integer/parseInt year))
     :day  (when day (Integer/parseInt day))}
    (throw (Exception. (format "Invalid year.day argument: %s" s)))))

(defn- file-empty? [file]
  (or (not (fs/exists? file)) (zero? (fs/size file))))

(defn- create-file [file template args &
                    {:keys [overwrite] :or {overwrite false}}]
  (if (or overwrite (file-empty? file))
    (spit file (render-file template args))
    (println (format "Create '%s' failed, file already exists." file))))

(defn find-implemented-dates []
  (->> (file-seq (fs/file "src"))
       (map #(re-matches #"src/aoc(\d\d\d\d)/day(\d\d).clj" (.getPath %)))
       (filter seq)
       sort
       (map (fn [[_ year day]]
              {:year (Integer/parseInt year)
               :day  (Integer/parseInt day)}))))

(defn- create-day-files [{:keys [year day] :as date}]
  (fs/create-dirs (source-dir year))
  (create-file (source-path year day) "templates/src.clj.tmpl" date)
  (fs/create-dirs (test-dir year))
  (create-file (test-path year day) "templates/test.clj.tmpl" date)
  (fs/create-dirs (input-dir year))
  (create-file (input-path year day) "templates/input.txt.tmpl" date)
  (create-file (answer-path year day) "templates/answer.json.tmpl" date))

(defn- update-day-name [year day headers]
  (let [answers-file (answer-path year day)
        answers      (if (fs/exists? answers-file)
                       (json/parse-string (slurp answers-file) true)
                       {})
        response     (curl/get (problem-url year day) headers)
        body         (:body (curl/get (problem-url year day) headers))
        name         (second (re-find #"--- Day \d+: (.*) ---" body))]
    (if (= (:status response) 200)
      (do
        (fs/create-dirs (input-dir year))
        (spit answers-file (str (json/generate-string (assoc answers :name name)
                                                      {:pretty true})
                                "\n")))
      (println "Error:" body))))

(defn- days-require-str [dates]
  ;; Due to a lack of whitespace control in the `for`
  ;; loop in selmer:
  ;;
  ;; https://github.com/yogthos/Selmer/issues/115
  ;;
  ;; We construct the wanted string of days for the
  ;; require form here and pass it into the template.
  (->> dates
       (map (fn [{:keys [year day]}]
              (format "[aoc%d.day%02d]" year day)))
       (str/join "\n            ")))

(defn update-days-file [& _]
  (let [dates (days-require-str (find-implemented-dates))]
    (create-file days-file "templates/days.clj.tmpl"
                 {:dates dates}
                 :overwrite true)))

(defn new-day [& _]
  (doseq [date (or (seq (map parse-day-spec *command-line-args*))
                   [today])]
    (if (:day date)
      (create-day-files date)

      ;; generate the whole year
      (doseq [day (range 1 26)]
        (create-day-files (assoc date :day day))))

    (update-days-file)))

(defn fetch-day [& _]
  (if (not= 1 (count *command-line-args*))
    (println "Can only fetch one day specified with YYYY.DD")
    (let [{:keys [year day]} (parse-day-spec (first *command-line-args*))
          input-file (input-path year day)
          headers    (aoc-headers)]
      (fs/create-dirs (input-dir year))
      (if (get-in headers [:headers "Cookie"])
        (if (file-empty? input-file)
          (let [response (curl/get (input-url year day) headers)]
            (if (= (:status response) 200)
              (do (spit input-file (:body response))
                  (update-day-name year day headers))
              (println "Error:" (:body response))))
          (println (format "Fetching '%s' failed, file already exists." input-file)))
        (println (format "Fetching '%s' failed, unable to load session file." input-file))))))
