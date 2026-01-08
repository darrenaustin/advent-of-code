;; https://adventofcode.com/2018/day/1
(ns aoc2018.day01
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2018 1))

(defn part1 [input] (reduce + (s/ints input)))

(defn part2 [input]
  (->> (s/ints input)
       cycle
       (reductions + 0)
       c/first-duplicate))
