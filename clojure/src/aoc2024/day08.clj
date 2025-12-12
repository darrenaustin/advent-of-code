;; https://adventofcode.com/2024/day/8
  (ns aoc2024.day08
    (:require
     [aoc.day :as d]
     [aoc.util.grid :as g]
     [aoc.util.vec :as v]
     [clojure.math.combinatorics :as combo]))

(defn input [] (d/day-input 2024 8))

(defn antennas [grid]
  (map keys (map val (dissoc (group-by val grid) \.))))

(defn antinodes [valid-pos? [a b]]
  (let [dir (v/vec- b a)]
    (filter valid-pos? [(v/vec- a dir) (v/vec+ b dir)])))

(defn super-antinodes [valid-loc? [a b]]
  (let [dir (v/vec- b a)]
    (concat
     (take-while valid-loc? (iterate #(v/vec- % dir) a))
     (take-while valid-loc? (iterate #(v/vec+ % dir) b)))))

(defn find-nodes [finder-fn locs]
  (mapcat finder-fn (combo/combinations locs 2)))

(defn num-antinodes [input antinode-fn]
  (let [grid            (g/parse-grid input)
        valid-loc?      (partial contains? grid)
        antennas        (antennas grid)
        antinode-finder (partial antinode-fn valid-loc?)]
    (count (set (mapcat #(find-nodes antinode-finder %) antennas)))))

(defn part1 [input]
  (num-antinodes input antinodes))

(defn part2 [input]
  (num-antinodes input super-antinodes))
