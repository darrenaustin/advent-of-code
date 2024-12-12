(ns tasks
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [selmer.parser :refer [render-file]]))

(def now          (java.time.LocalDate/now))
(def current-year (.getYear now))
(def current-day  (if (== current-year 2024)
                    ;; 2024 started a day early.
                    (inc (.getDayOfMonth now))
                    (.getDayOfMonth now)))

(def today {:year current-year :day current-day})

(def aoc-url "https://adventofcode.com")

(defn- problem-url [y d] (str aoc-url "/" y "/day/" d))
(defn- input-url   [y d] (str (problem-url y d) "/input"))

(defn- source-dir  [y]   (str "src/aoc" y))
(defn- source-path [y d] (format "%s/day%02d.clj" (source-dir y) d))
(defn- test-dir    [y]   (str "test/aoc" y))
(defn- test-path   [y d] (format "%s/day%02d_test.clj" (test-dir y) d))
;; TODO: add a way to override this with an env var.
(defn- input-dir   [y]   (str "../advent_of_code_input/" y))
(defn- input-path  [y d] (format "%s/%02d_input.txt" (input-dir y) d))
(defn- answer-path [y d] (format "%s/%02d_answer.json" (input-dir y) d))

(def days-file "src/aoc/days.clj")

(defn- parse-day-spec [s]
  (if-let [[_ year _ day] (re-find #"(\d+)(\.(\d+))?" s)]
    {:year (when year (Integer/parseInt year))
     :day  (when day  (Integer/parseInt day))}
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
               :day (Integer/parseInt day)}))))

(defn- create-day-files [{:keys [year day] :as date}]
  (fs/create-dirs (source-dir year))
  (create-file (source-path year day) "templates/src.clj.tmpl" date)
  (fs/create-dirs (test-dir year))
  (create-file (test-path   year day) "templates/test.clj.tmpl" date)
  (fs/create-dirs (input-dir year))
  (create-file (input-path  year day) "templates/input.txt.tmpl" date)
  (create-file (answer-path year day) "templates/answer.json.tmpl" date))

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
              (format "   [aoc%d.day%02d]" year day)))
       (str/join "\n")))

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
