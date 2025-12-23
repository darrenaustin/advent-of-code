;; https://adventofcode.com/2024/day/8
  (ns aoc2024.day08
    (:require
     [aoc.day :as d]
     [aoc.util.grid :as g]
     [aoc.util.pos :as p]
     [clojure.math.combinatorics :as combo]))

(defn input [] (d/day-input 2024 8))

(defn antennas [grid]
  ;; TODO: do we need to add iterator support for grid-vec?
  ;; otherwise we have to use (seq grid) here.
  (map keys (map val (dissoc (group-by val (seq grid)) \.))))

(defn antinodes [valid-pos? [a b]]
  (let [dir (p/pos- b a)]
    (filter valid-pos? [(p/pos- a dir) (p/pos+ b dir)])))

(defn super-antinodes [valid-loc? [a b]]
  (let [dir (p/pos- b a)]
    (concat
     (take-while valid-loc? (iterate #(p/pos- % dir) a))
     (take-while valid-loc? (iterate #(p/pos+ % dir) b)))))

(defn find-nodes [finder-fn locs]
  (mapcat finder-fn (combo/combinations locs 2)))

(defn num-antinodes [input antinode-fn]
  (let [grid            (g/str->grid input)
        valid-loc?      (partial contains? grid)
        antennas        (antennas grid)
        antinode-finder (partial antinode-fn valid-loc?)]
    (count (set (mapcat #(find-nodes antinode-finder %) antennas)))))

(defn part1 [input]
  (num-antinodes input antinodes))

(defn part2 [input]
  (num-antinodes input super-antinodes))
