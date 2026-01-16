;; https://adventofcode.com/2020/day/1
(ns aoc2020.day01
  (:require
   [aoc.day :as d]
   [aoc.util.collection :refer [first-where]]
   [aoc.util.math :refer [product sum]]
   [aoc.util.string :as s]
   [clojure.math.combinatorics :as combo]))

(defn input [] (d/day-input 2020 1))

(defn- fix-report [input n]
  (->> (combo/combinations (s/ints input) n)
       (first-where #(= 2020 (sum %)))
       product))

(defn part1 [input] (fix-report input 2))

(defn part2 [input] (fix-report input 3))
