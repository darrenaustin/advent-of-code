;; https://adventofcode.com/2015/day/4
(ns aoc2015.day04
  (:require
   [aoc.day :as d]
   [clojure.string :as str]
   [pandect.algo.md5 :refer [md5]]))

(defn input [] (d/day-input 2015 4))

(defn lowest-num-for [key prefix]
  (first (filter #(str/starts-with? (md5 (str key %)) prefix)
                 (range))))

(defn part1 [input] (lowest-num-for input "00000"))

(defn part2 [input] (lowest-num-for input "000000"))
