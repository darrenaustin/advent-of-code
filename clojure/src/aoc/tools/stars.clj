(ns aoc.tools.stars
  (:require
   [aoc.util.collection :as c]
   [cheshire.core :as json]
   [clojure.java.io :as io]
   [clojure.java.shell :refer [sh]]
   [clojure.string :as str])
  (:import
   java.io.File))

(def project-root "..")

(def active-years (range 2015 2026))

(defn days-per-year [year] (if (< year 2025) 25 12))

(defn aoc-day [year day]
  (str "[Day " (format "%02d" day) "](https://adventofcode.com/" year "/day/" day ")"))

(defn day-solutions [year-stats year-paths day]
  (map (fn [stats path]
         (if-let [day-file (get-in stats [:days day :path])]
           (str "[" day-file "](" (str/join "/" [path day-file]) ")")
           "-"))
       year-stats year-paths))

(defn language-names [stats]
  (map #(str "[" (:language %) "](" (:path %) "/README.md)") stats))

(defn table-for-year [year stats answers]
  (let [year-stats (map #(get-in % [:years year]) stats)
        year-paths (map (fn [l] (str/join "/" [(:path l) (get-in l [:years year :path])])) stats)
        year-stars (reduce + (map :stars (vals (answers year))))]
    (println "## " year)
    (println)
    (println "|" (if-not (zero? year-stars) (str year-stars " ⭐️") "-") "| Title |" (str/join "|" (language-names stats)) "|")
    (println "| :----: | :---- |" (str/join "|" (repeat (count stats) ":----:")) "|")
    (doseq [day (range 1 (inc (days-per-year year)))]
      (println "|" (aoc-day year day) "|" (or (get-in answers [year day :name]) "-") "|" (str/join " | " (day-solutions year-stats year-paths day)) " | "))
    (println)))

(defn read-stats []
  (->> (io/file project-root)
       File/.listFiles
       (filter #(File/.isDirectory %))
       (mapcat File/.listFiles)
       (filter #(= "stats.json" (File/.getName %)))
       (map File/.getPath)
       (map slurp)
       (map #(json/parse-string % keyword))))

(defn convert-stats [stats]
  (update stats :years
          #(c/map-by :year
                     (map (fn [year]
                            (update year :days (partial c/map-by :day)))
                          %))))

(defn read-answers []
  (let [input-dir (io/file project-root "inputs")]
    (->> (file-seq input-dir)
         (filter #(re-matches #"\d{4}" (File/.getName %)))
         (map (fn [year-dir]
                (let [year (Integer/parseInt (File/.getName year-dir) 10)
                      day-files (filter #(re-matches #"\d{2}_answer\.json" (File/.getName %))
                                        (file-seq year-dir))]
                  [year
                   (into {}
                         (map (fn [f]
                                (let [day (Integer/parseInt (subs (File/.getName f) 0 2) 10)
                                      {:keys [name answer1 answer2]}
                                      (json/parse-string (slurp f) true)]
                                  [day {:name name
                                        :stars (count (keep identity [answer1 answer2]))}]))
                              day-files))])))
         (into {}))))

(defn -main []
  (let [stats (map convert-stats (read-stats))
        answers (read-answers)
        scoreboard (str/join "/" [project-root "SCOREBOARD.md"])]
    (spit scoreboard
          (with-out-str
            (println "# 🎄 &nbsp; Scoreboard &nbsp; 🎄")
            (println)
            (doseq [year active-years]
              (table-for-year year stats answers))))
    (let [result (:exit (sh "prettier" "--write" scoreboard))]
      (shutdown-agents)
      result)))
