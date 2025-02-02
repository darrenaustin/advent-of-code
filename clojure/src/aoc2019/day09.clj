;; https://adventofcode.com/2019/day/9
(ns aoc2019.day09
  (:require [aoc.day :as d]
            [aoc2019.intcode :as i]))

(defn input [] (d/day-input 2019 9))

(defn part1 [input]
  (first (:output (i/execute (i/parse input) '(1) []))))

(defn part2 [input]
  (first (:output (i/execute (i/parse input) '(2) []))))
