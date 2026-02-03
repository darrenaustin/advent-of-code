;; https://adventofcode.com/2022/day/8
(ns aoc2022.day08
  (:require
   [aoc.day :as d]
   [aoc.util.char :as char]
   [aoc.util.collection :as c]
   [aoc.util.grid :as g]
   [aoc.util.math :refer [product]]
   [aoc.util.pos :as p]))

(defn input [] (d/day-input 2022 8))

(defn- heights-from [grid pos dir]
  (->> (iterate (partial p/pos+ dir) pos)
       (map grid)
       (take-while identity)
       rest))

(defn- visible-in? [grid pos dir]
  (let [height (grid pos)]
    (every? #(> height %) (heights-from grid pos dir))))

(defn- visible? [grid pos]
  (some (partial visible-in? grid pos) p/orthogonal-dirs))

(defn- visible-trees [grid]
  (filter #(visible? grid %) (keys grid)))

(defn- scenic-score-from [grid pos dir]
  (let [height (grid pos)]
    (count (c/take-upto #(< % height) (heights-from grid pos dir)))))

(defn- scenic-score [grid pos]
  (->> p/orthogonal-dirs
       (map (partial scenic-score-from grid pos))
       product))

(defn part1 [input]
  (->> (g/->grid input char/digit)
       visible-trees
       count))

(defn part2 [input]
  (let [grid (g/->grid input char/digit)]
    (transduce (map (partial scenic-score grid)) max 0 (keys grid))))
