;; https://adventofcode.com/2015/day/3
(ns aoc2015.day03
  (:require
   [aoc.day :as d]
   [aoc.util.grid :refer :all]))

(def input (d/day-input 2015 3))

(def dirs {\^ dir-up
           \v dir-down
           \> dir-right
           \< dir-left})

(defn packages-delivered [input locations packages]
  (if (seq input)
    (let [location (map + (first locations) (dirs (first input)))]
      (recur (rest input)
             (conj (into [] (rest locations)) location)
             (conj packages location)))
    packages))

(defn part1 [input]
  (count (packages-delivered input [[0 0]] #{[0 0]})))

(defn part2 [input]
  (count (packages-delivered input [[0 0] [0 0]] #{[0 0]})))
