(ns tools.fetch-input
  (:require
   [aoc.day :as d]
   [clj-http.client :as http]
   [clojure.java.io :as io]
   [clojure.string :as str])
  (:gen-class))

;; TODO: move this to a common location for the tools
(defn parse-day-spec
  ([s] (parse-day-spec s {:day-required? false}))
  ([s {:keys [day-required?] :or {day-required? false}}]
   (if-let [[_ year _ day-num] (re-find #"(\d+)(\.(\d+))?" s)]
     (if (and day-required? (nil? day-num))
       (throw (Exception. (format "Must include day as well as year: %s" s)))
       {:year (when year (Integer/parseInt year))
        :day-num (when day-num (Integer/parseInt day-num))})
     (throw (Exception. (format "Invalid year.day argument: %s" s))))))

(defn load-session []
  (try
    (str/trim (slurp (io/file ".session")))
    (catch Exception _ nil)))

(defn fetch-input-content [year day-num session-id]
  (let [url (format "https://adventofcode.com/%d/day/%d/input" year day-num)]
    (http/get url {:cookies {:session {:value session-id}} :throw-exceptions false})))

(defn usage []
  (println "Usage:\n   clj -M -m tools.fetch-input YYYY.dd\n")
  (println "Which will fetch the input from the website for")
  (println "for the given YYYY.dd day specification."))

(defn -main [& args]
  (if (not= 1 (count args))
    (usage)

    (let [{:keys [year day-num]} (parse-day-spec (first args) {:day-required? true})
          session-id (load-session)]
      (if (not session-id)
        (println "No session data found, unable to download input file.")

        (let [answer-file (io/file (d/input-file-name year day-num))]
          (if (and (.exists answer-file) (not (zero? (.length answer-file))))
            (println "File already downloaded, fetching skipped.")

            (let [response (fetch-input-content year day-num session-id)]
              (if (not (http/success? response))
                (do
                  (println "Unable to fetch input. Response code" (:status response))
                  (println (:body response)))

                (let [content (:body response)]
                  (with-open [wrtr (io/writer answer-file)]
                    (.write wrtr content))
                  (println "Successfully downloaded input to " (.getPath answer-file)))))))))))
