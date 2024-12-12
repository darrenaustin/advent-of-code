;; https://adventofcode.com/2024/day/4
(ns aoc2024.day04
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.grid :refer :all]
   [aoc.util.math :as m]))

(def input (d/day-input 2024 4))

(def xmas (c/indexed "XMAS"))

(defn xmas-in-dir? [grid loc dir]
  (every?
   (fn [[idx letter]]
     (= (grid (vec+ loc (vec-n* idx dir))) letter))
   xmas))

(defn count-xmas-at [grid pos]
  (count (filter #(xmas-in-dir? grid pos %) cardinal-dirs)))

(defn xmas-in [grid]
  (m/sum (map #(count-xmas-at grid %) (keys grid))))


(defn part1 [input] (xmas-in (parse-grid input)))

(defn x-mas-at? [grid pos]
  ;; assumes there is an 'A' in the grid at pos.
  (let [left-up (grid (vec+ pos dir-nw))
        left-down (grid (vec+ pos dir-sw))
        right-up (grid (vec+ pos dir-ne))
        right-down (grid (vec+ pos dir-se))]
    (= #{\M \S} (set [left-up right-down]) (set [left-down right-up]))))

(defn x-mas-in [grid]
  (count
   (filter #(x-mas-at? grid %)
             ;; Find all the "A" locations in the grid
           (map key (filter (fn [[_ v]] (= \A v)) grid)))))

(defn part2 [input] (x-mas-in (parse-grid input)))
