;; https://adventofcode.com/2024/day/4
(ns aoc2024.day04
  (:require
   [aoc.day :as d]
   [aoc.util.collection :refer [count-where indexed]]
   [aoc.util.grid :as g]
   [aoc.util.math :as m]
   [aoc.util.vec :as v]))

(defn input [] (d/day-input 2024 4))

(def xmas (indexed "XMAS"))

(defn xmas-in-dir? [grid loc dir]
  (every?
   (fn [[idx letter]]
     (= (grid (v/vec+ loc (v/vec-n* idx dir))) letter))
   xmas))

(defn count-xmas-at [grid pos]
  (count-where #(xmas-in-dir? grid pos %) v/adjacent-dirs))

(defn xmas-in [grid]
  (m/sum (map #(count-xmas-at grid %) (keys grid))))

(defn part1 [input] (xmas-in (g/parse-grid input)))

(defn x-mas-at? [grid pos]
  ;; assumes there is an 'A' in the grid at pos.
  (let [left-up    (grid (v/vec+ pos v/dir-nw))
        left-down  (grid (v/vec+ pos v/dir-sw))
        right-up   (grid (v/vec+ pos v/dir-ne))
        right-down (grid (v/vec+ pos v/dir-se))]
    (= #{\M \S} (set [left-up right-down]) (set [left-down right-up]))))

(defn x-mas-in [grid]
  (count-where #(x-mas-at? grid %) (g/locs-where grid #{\A})))

(defn part2 [input] (x-mas-in (g/parse-grid input)))
