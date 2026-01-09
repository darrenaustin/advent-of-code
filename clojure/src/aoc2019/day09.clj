;; https://adventofcode.com/2019/day/9
(ns aoc2019.day09
  (:require
   [aoc.day :as d]
   [aoc2019.intcode :as ic]))

(defn input [] (d/day-input 2019 9))

(defn- run [program input]
  (-> (ic/parse-program program)
      (ic/run [input] [])
      :output
      first))

(defn part1 [input] (run input 1))

(defn part2 [input] (run input 2))
