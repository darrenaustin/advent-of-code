;; https://adventofcode.com/2016/day/6
(ns aoc2016.day06
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2016 6))

(defn most-common [xs]
  (ffirst (sort-by second > (frequencies xs))))

(defn least-common [xs]
  (ffirst (sort-by second (frequencies xs))))

(defn part1 [input]
  (str/join (map most-common (apply map vector (s/lines input)))))

(defn part2 [input]
  (str/join (map least-common (apply map vector (s/lines input)))))
