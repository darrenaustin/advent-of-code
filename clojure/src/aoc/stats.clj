(ns aoc.stats
  (:require
   [aoc.core :as core]
   [aoc.day :as day]
   [cheshire.core :as json]))

(defn stars [year day-num]
  (print day-num "")
  (flush)
  (day/stars year day-num))

(defn year-data [year days]
  (print "Generating stats for" year "")
  (let [data {:year year
              :path (str "aoc" year)
              :days (vec (for [[y d] days]
                           {:day d
                            :path (format "day%02d.clj" d)
                            :stars (stars y d)}))}]
    (println)
    data))

(defn -main []
  (let [days-by-year (into (sorted-map) (group-by first (core/all-days)))
        ;; days-by-year {2019 (days-by-year 2019)}
        stats {:language "Clojure"
               :path "clojure"
               :years (vec (for [[year days] days-by-year]
                             (year-data year days)))}]
    (spit "stats.json" (str (json/generate-string stats {:pretty true})
                            "\n"))))
