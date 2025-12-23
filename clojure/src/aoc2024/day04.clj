;; https://adventofcode.com/2024/day/4
(ns aoc2024.day04
  (:require
   [aoc.day :as d]
   [aoc.util.collection :refer [count-where indexed keys-when-val]]
   [aoc.util.grid :as g]
   [aoc.util.math :as m]
   [aoc.util.pos :as p]))

(defn input [] (d/day-input 2024 4))

(def xmas (indexed "XMAS"))

(defn xmas-in-dir? [grid loc dir]
  (every?
   (fn [[idx letter]]
     (= (grid (p/pos+ loc (p/pos* idx dir))) letter))
   xmas))

(defn count-xmas-at [grid pos]
  (count-where #(xmas-in-dir? grid pos %) p/adjacent-dirs))

(defn xmas-in [grid]
  (m/sum (map #(count-xmas-at grid %) (keys grid))))

(defn part1 [input] (xmas-in (g/str->grid input)))

(defn x-mas-at? [grid pos]
  ;; assumes there is an 'A' in the grid at pos.
  (let [left-up    (grid (p/pos+ pos p/dir-nw))
        left-down  (grid (p/pos+ pos p/dir-sw))
        right-up   (grid (p/pos+ pos p/dir-ne))
        right-down (grid (p/pos+ pos p/dir-se))]
    (= #{\M \S} (set [left-up right-down]) (set [left-down right-up]))))

(defn x-mas-in [grid]
  (count-where #(x-mas-at? grid %) (keys-when-val #{\A} grid)))

(defn part2 [input] (x-mas-in (g/str->grid input)))
