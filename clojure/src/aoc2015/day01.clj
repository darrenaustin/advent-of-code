;; https://adventofcode.com/2015/day/1
(ns aoc2015.day01
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.math :as m]))

(defn input [] (d/day-input 2015 1))

(def floor-delta {\( 1, \) -1})

(defn part1 [input]
  (m/sum (map floor-delta input)))

(defn part2 [input]
  (inc (c/index-of (reductions + (map floor-delta input)) -1)))
