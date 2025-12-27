(ns aoc.stars
  (:require
   [cheshire.core :as json]))

(def active-years (range 2015 2026))

(defn days-per-year [year] (if (< year 2025) 25 12))

(defn stars-per-year [year] (* 2 (days-per-year year)))

(defn stats-for-year [stats year]
  (first (filter #(= (:year %) year) (:years stats))))

(defn stats-for-day [year-stats day]
  (first (filter #(= (:day %) day) (:days year-stats))))

(defn stars-for-year [year-stats]
  (if (seq (:days year-stats))
    (reduce + (map :stars (:days year-stats)))
    0))

(defn table-for-year [year-stats base-path]
  (let [year (:year year-stats)
        total (stars-for-year year-stats)]
    (println "###" year "-" total "⭐️")
    (println "|       |       |       |       |       |       |       |       |       |       |")
    (println "| ----: | ----: | ----: | ----: | ----: | ----: | ----: | ----: | ----: | ----: |")
    (doseq [row (partition 10 10 nil (range 1 (inc (days-per-year year))))]
      (print "| ")
      (doseq [day row]
        (let [day-stats (stats-for-day year-stats day)
              num-stars (get day-stats :stars 0)
              stars (case num-stars
                      0 "☆☆"
                      1 "⭐️☆"
                      2 "⭐️⭐️")
              link (when (seq day-stats) (str base-path "/" (:path year-stats) "/" (:path day-stats)))]
          (if link
            (print "[" day stars "](" link ") | ")
            (print day stars "| "))))
      (println))
    (println)))

(defn -main []
  (let [stats (json/parse-string (slurp "stats.json") keyword)
        possible-stars (reduce + (map stars-per-year active-years))
        total-stars (reduce + (map #(stars-for-year (stats-for-year stats %)) active-years))]
    (spit "stars.md"
          (with-out-str
            (println "# 🎄⭐️" (:language stats) "Stars ⭐️🎄")
            (println "\n**Total:" total-stars "/" possible-stars "⭐️**\n")
            (doseq [year active-years]
              (table-for-year (or (stats-for-year stats year) {:year year}) "src"))))))

