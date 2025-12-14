;; https://adventofcode.com/2015/day/10
(ns aoc2015.day10
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [clojure.string :as str]))

(defn input [] (d/day-input 2015 10))

(defn look-and-say [s]
  (str/join
   (mapcat (fn [g] [(count g) (first g)])
           (partition-by identity s))))

(defn length-after [input n]
  (count (c/iterate-n look-and-say input n)))

(defn part1 [input] (length-after input 40))

(defn part2 [input] (length-after input 50))
