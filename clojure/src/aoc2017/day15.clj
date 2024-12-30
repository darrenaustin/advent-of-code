;; https://adventofcode.com/2017/day/15
(ns aoc2017.day15
  (:require [aoc.day :as d]
            [aoc.util.collection :refer [count-where]]
            [aoc.util.string :as s]))

(defn input [] (d/day-input 2017 15))

(defn generator [factor start]
  (rest (iterate #(rem (* % factor) 2147483647) start)))

(defn generator2 [factor start divisible]
  (filter #(zero? (mod % divisible)) (rest (iterate #(rem (* % factor) 2147483647) start))))

;; TODO: find a way to speed these up
(defn part1 [input]
  (let [[a b] (map generator [16807 48271] (s/parse-ints input))]
    (count-where true? (take 40000000 (map #(= (bit-and 0xffff %1) (bit-and 0xffff %2)) a b)))))

(defn part2 [input]
  (let [[a b] (map generator2 [16807 48271] (s/parse-ints input) [4 8])]
    (count-where true? (take 5000000 (map #(= (bit-and 0xffff %1) (bit-and 0xffff %2)) a b)))))
