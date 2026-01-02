;; https://adventofcode.com/2016/day/12
(ns aoc2016.day12
  (:require
   [aoc.day :as d]
   [aoc2016.assembunny :refer [assembunny]]))

(defn input [] (d/day-input 2016 12))

(defn part1 [input] ((assembunny input) :a))

(defn part2 [input] ((assembunny input {:c 1}) :a))
