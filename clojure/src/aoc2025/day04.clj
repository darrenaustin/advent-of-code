;; https://adventofcode.com/2025/day/4
(ns aoc2025.day04
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.grid :as g]
   [aoc.util.pos :as p]))

(defn input [] (d/day-input 2025 4))

(def roll? #{\@})

(defn roll-locations
  ([grid] (c/keys-when-val roll? grid))
  ([grid candidate-locs] (filter #(roll? (grid %)) candidate-locs)))

(defn removeable-rolls [grid]
  (filter #(< (count (roll-locations grid (p/adjacent-to %))) 4)
          (roll-locations grid)))

(defn remove-rolls [grid locs]
  (apply dissoc grid locs))

(defn part1 [input]
  (-> (g/->sparse-grid input)
      removeable-rolls
      count))

(defn part2 [input]
  (let [grid (g/->sparse-grid input)]
    (loop [grid' grid]
      (let [rolls (removeable-rolls grid')]
        (if (zero? (count rolls))
          (- (count (roll-locations grid)) (count (roll-locations grid')))
          (recur (remove-rolls grid' rolls)))))))
