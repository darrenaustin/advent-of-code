;; https://adventofcode.com/2020/day/3
 (ns aoc2020.day03
   (:require
    [aoc.day :as d]
    [aoc.util.grid :as g]
    [aoc.util.math :refer [product sum]]
    [aoc.util.pos :as p]))

(defn input [] (d/day-input 2020 3))

(defn- parse-map [input] (g/str->grid input {\. 0 \# 1}))

(defn- get-repeating [grid [x y]]
  (get grid [(mod x (g/width grid)) y]))

(defn- trees-hit [grid slope]
  (->> (iterate (partial p/pos+ slope) p/origin)
       (map (partial get-repeating grid))
       (take-while some?)
       sum))

(defn part1 [input]
  (trees-hit (parse-map input) [3 1]))

(defn part2 [input]
  (let [grid (parse-map input)]
    (->> [[1 1] [3 1] [5 1] [7 1] [1 2]]
         (map (partial trees-hit grid))
         product)))

