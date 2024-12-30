;; https://adventofcode.com/2017/day/10
(ns aoc2017.day10
  (:require [aoc.day :as d]
            [aoc.util.string :refer [parse-ints]]
            [aoc2017.knot-hash :refer [knot-hash sparse-hash]]))

(defn input [] (d/day-input 2017 10))

(defn part1
  ([input] (part1 input 256))
  ([input list-size]
   (->> input
        parse-ints
        (sparse-hash (vec (range list-size)))
        (take 2)
        (apply *))))

(defn part2 [input] (knot-hash input))
