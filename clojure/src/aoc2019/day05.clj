;; https://adventofcode.com/2019/day/5
(ns aoc2019.day05
  (:require [aoc.day :as d]
            [aoc2019.intcode :as i]))

(defn input [] (d/day-input 2019 5))

(defn part1 [input]
  (last (:output (i/execute (i/parse input) '(1) []))))

(defn part2 [input]
  (first (:output (i/execute (i/parse input) '(5) []))))
