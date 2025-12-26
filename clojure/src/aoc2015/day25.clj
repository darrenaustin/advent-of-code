;; https://adventofcode.com/2015/day/25
(ns aoc2015.day25
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2015 25))

(defn next-code [previous-code]
  (rem (* previous-code 252533) 33554393))

;; Naive brute force, but it works and isn't too slow.
(defn code-at [starting-code [target-row target-col]]
  (loop [code starting-code, row 1, col 1]
    (if (and (= row target-row) (= col target-col))
      code
      (if (= row 1)
        (recur (next-code code) (inc col) 1)
        (recur (next-code code) (dec row) (inc col))))))

(defn part1 [input] (code-at 20151125 (s/ints input)))

(defn part2 [_] "🎄 Got em all! 🎉")
