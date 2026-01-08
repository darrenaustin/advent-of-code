;; https://adventofcode.com/2017/day/11
(ns aoc2017.day11
  (:require
   [aoc.day :as d]
   [aoc.util.hex :as hex]
   [aoc.util.pos :as p]
   [clojure.string :as str]))

(defn input [] (d/day-input 2017 11))

(defn- parse-steps [input] (map hex/dirs (str/split input #",")))

(defn part1 [input]
  (->> (parse-steps input)
       (reduce p/pos+ hex/origin)
       hex/distance))

(defn part2 [input]
  (->> (parse-steps input)
       (reductions p/pos+ hex/origin)
       (map hex/distance)
       (apply max)))
