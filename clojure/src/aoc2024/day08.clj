;; https://adventofcode.com/2024/day/8
(ns aoc2024.day08
  (:require
   [aoc.day :as d]
   [aoc.util.grid :refer [parse-grid vec+ vec-]]
   [clojure.math.combinatorics :as combo]))

(def input (d/day-input 2024 8))

(defn antennas [grid]
  (map keys (map val (dissoc (group-by val grid) \.))))

(defn antinodes [valid-pos? [a b]]
  (let [dir (vec- b a)]
    (filter valid-pos? [(vec- a dir) (vec+ b dir)])))

(defn super-antinodes [valid-loc? [a b]]
  (let [dir (vec- b a)]
    (concat
     (take-while valid-loc? (iterate #(vec- % dir) a))
     (take-while valid-loc? (iterate #(vec+ % dir) b)))))

(defn find-nodes [finder-fn locs]
  (mapcat finder-fn (combo/combinations locs 2)))

(defn num-antinodes [input antinode-fn]
  (let [grid (parse-grid input)
        valid-loc? (partial contains? grid)
        antennas (antennas grid)
        anitnode-finder (partial antinode-fn valid-loc?)]
    (count
     (into #{} (mapcat #(find-nodes anitnode-finder %) antennas)))))

(defn part1 [input]
  (num-antinodes input antinodes))

(defn part2 [input]
  (num-antinodes input super-antinodes))
