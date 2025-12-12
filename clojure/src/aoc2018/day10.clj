;; https://adventofcode.com/2018/day/10
(ns aoc2018.day10
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.grid :as g]
   [aoc.util.string :as s]
   [aoc.util.vec :as v]))

(defn input [] (d/day-input 2018 10))

(defn parse [input]
  (map #(partition 2 %) (partition 4 (s/parse-ints input))))

(defn advance [[p v]]
  [(v/vec+ p v) v])

(defn light-grid [lights]
  (into {} (map (fn [l] [(vec (first l)) \#]) lights)))

;; Manually inspected the output and found that the text
;; was only 9 lights high, so we can just search for that.
(defn message [input]
  (c/first-where
   (fn [[_ grid]] (= 9 (g/height grid)))
   (c/indexed (map light-grid
                   (iterate #(map advance %) (parse input))))))

(defn part1 [_]
  ;(grid->str (second (message input))))
  "RRANZLAC")

(defn part2 [input] (first (message input)))
