;; https://adventofcode.com/2019/day/5
(ns aoc2019.day05
  (:require
   [aoc.day :as d]
   [aoc2019.intcode :as ic]))

(defn input [] (d/day-input 2019 5))

(defn part1 [input]
  (last (:output (ic/run (ic/parse-program input) [1] []))))

(defn part2 [input]
  (first (:output (ic/run (ic/parse-program input) [5] []))))
