;; https://adventofcode.com/2015/day/24
(ns aoc2015.day24
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.math.combinatorics :as combo]))

(defn input [] (d/day-input 2015 24))

(defn- best-loadout [input num-groups]
  (let [packages (sort > (s/ints input))
        weight (/ (m/sum packages) num-groups)]
    (loop [[n & rest] (range 1 (/ (count packages) num-groups))]
      (let [smallest-fronts (->> (combo/combinations packages n)
                                 (filter #(= (m/sum %) weight))
                                 (map m/product))]
        (if (seq smallest-fronts)
          (apply min smallest-fronts)
          (recur rest))))))

(defn part1 [input] (best-loadout input 3))

(defn part2 [input] (best-loadout input 4))
