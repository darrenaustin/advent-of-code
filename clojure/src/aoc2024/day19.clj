;; https://adventofcode.com/2024/day/19
(ns aoc2024.day19
  (:require
   [aoc.day :as d]
   [aoc.util.collection :refer [count-where]]
   [aoc.util.math :refer [sum]]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2024 19))

(defn parse [input]
  (let [[towels designs] (str/split input #"\n\n")]
    [(str/split towels #", ")
     (s/lines designs)]))

(def ways-to-make
  (memoize
   (fn [towels design]
     (if (empty? design)
       1
       (sum (map #(ways-to-make towels (subs design (count %)))
                 (filter #(str/starts-with? design %) towels)))))))

(defn solve [input]
  (let [[towels designs] (parse input)]
    (map #(ways-to-make towels %) designs)))

(defn part1 [input]
  (count-where #(not= 0 %) (solve input)))

(defn part2 [input]
  (sum (solve input)))
