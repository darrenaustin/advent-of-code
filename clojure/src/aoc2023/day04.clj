;; https://adventofcode.com/2023/day/4
(ns aoc2023.day04
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]
   [clojure.set :as set]
   [clojure.string :as str]))

(defn input [] (d/day-input 2023 4))

(defn- num-winners [card-line]
  (let [[_ winning picked] (str/split card-line #":|\|")]
    (count (set/intersection (set (s/ints winning)) (set (s/ints picked))))))

(defn- points [n] (if (pos? n) (bit-shift-left 1 (dec n)) 0))

(defn- card-count
  ([cards] (reduce + cards))
  ([cards winners] (conj cards (reduce + 1 (take winners cards)))))

(defn part1 [input]
  (transduce (map (comp points num-winners)) + (s/lines input)))

(defn part2 [input]
  (transduce (map num-winners) card-count '() (reverse (s/lines input))))
