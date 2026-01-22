;; https://adventofcode.com/2022/day/6
(ns aoc2022.day06
  (:require
   [aoc.day :as d]))

(defn input [] (d/day-input 2022 6))

(defn- marker [input size]
  (->> (partition size 1 input)
       (keep-indexed (fn [i chrs] (when (apply distinct? chrs) i)))
       first
       (+ size)))

(defn part1 [input] (marker input 4))

(defn part2 [input] (marker input 14))
