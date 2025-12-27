;; https://adventofcode.com/2016/day/3
(ns aoc2016.day03
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2016 3))

(defn parse-triplets [input] (map s/ints (s/lines input)))

(defn triangle? [[a b c]]
  (and (< a (+ b c))
       (< b (+ a c))
       (< c (+ a b))))

(defn num-triangles [triplets]
  (count (filter triangle? triplets)))

(defn part1 [input] (num-triangles (parse-triplets input)))

(defn part2 [input]
  (num-triangles (->> (parse-triplets input)
                      (apply mapcat vector)
                      (partition 3))))
