;; https://adventofcode.com/2015/day/1
(ns aoc2015.day01
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]))

(def input (d/day-input 2015 1))

(def floor-dirs {\( 1, \) -1})

(defn part1 [input]
  (m/sum (map floor-dirs input)))

(defn part2 [input]
  (reduce
   (fn [stops dir]
     (let [[floor idx] (peek stops)
           [floor idx] [(+ floor (floor-dirs dir)) (inc idx)]]
       (if (< floor 0)
         (reduced idx)
         (conj stops [floor idx]))))
   [[0 0]]
   input))
