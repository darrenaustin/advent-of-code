;; https://adventofcode.com/2022/day/4
 (ns aoc2022.day04
   (:require
    [aoc.day :as d]
    [aoc.util.string :as s]))

(defn input [] (d/day-input 2022 4))

(defn- fully-contains? [[[min1 max1] [min2 max2]]]
  (or (<= min1 min2 max2 max1)
      (<= min2 min1 max1 max2)))

(defn- overlapping? [[[min1 max1] [min2 max2]]]
  (and (<= min2 max1) (<= min1 max2)))

(defn- duplicate-effort [input duplicating?]
  (->> (s/lines input)
       (map s/pos-ints)
       (map (partial partition 2))
       (filter duplicating?)
       count))

(defn part1 [input] (duplicate-effort input fully-contains?))

(defn part2 [input] (duplicate-effort input overlapping?))
