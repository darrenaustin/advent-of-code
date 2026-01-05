;; https://adventofcode.com/2016/day/20
    (ns aoc2016.day20
      (:require
       [aoc.day :as d]
       [aoc.util.string :as s]))

(defn input [] (d/day-input 2016 20))

;; Assumes the ranges are sorted lowest-min -> highest-min

(defn- touches? [[_ max1] [min2 _]] (<= min2 (inc max1)))

(defn- combine [[min1 max1] [min2 max2]] [(min min1 min2) (max max1 max2)])

(defn- range-between [[[_ max1] [min2 _]]]
  (when (not= (inc max1) min2)
    [(inc max1) (dec min2)]))

(defn- range-size [[min max]] (inc (- max min)))

(defn- merge-range [ranges r]
  (if-let [current (peek ranges)]
    (if (touches? current r)
      (conj (pop ranges) (combine current r))
      (conj ranges r))
    [r]))

(defn- available-ranges [input]
  (let [blocked (sort (mapv s/pos-ints (s/lines input)))]
    (->> (concat [[-1 -1]] blocked [[4294967296 4294967296]])
         (reduce merge-range [])
         (partition 2 1)
         (keep range-between))))

(defn part1 [input]
  (ffirst (available-ranges input)))

(defn part2 [input]
  (->> (available-ranges input)
       (map range-size)
       (reduce +)))
