;; https://adventofcode.com/2019/day/1
(ns aoc2019.day01
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2019 1))

(defn- mass->fuel [mass] (- (quot mass 3) 2))

(defn- mass->total-fuel [mass]
  (->> (iterate mass->fuel mass)
       rest
       (take-while pos?)
       (reduce +)))

(defn- fuel-for [input calc-fn]
  (transduce (map calc-fn) + (s/ints input)))

(defn part1 [input] (fuel-for input mass->fuel))

(defn part2 [input] (fuel-for input mass->total-fuel))
