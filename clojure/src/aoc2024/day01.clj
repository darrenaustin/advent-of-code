;; https://adventofcode.com/2024/day/1
(ns aoc2024.day01
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.matrix :as mat]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2024 1))

(defn parse-locations [input]
  (mat/transpose (partition 2 (s/ints input))))

(defn part1 [input]
  (let [[left right] (map sort (parse-locations input))]
    (m/sum (map m/distance left right))))

(defn similarity [x freqs]
  (* x (get freqs x 0)))

(defn part2 [input]
  (let [[left right] (parse-locations input)
        freqs (frequencies right)]
    (m/sum (map #(similarity % freqs) left))))
