;; https://adventofcode.com/2025/day/2
(ns aoc2025.day02
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2025 2))

(defn parse-ranges [input]
  (map (fn [[_ start end]] [(s/int start) (s/int end)])
       (re-seq #"(\d+)-(\d+)" input)))

(defn matches-in-range [regex [start end]]
  (filter #(re-matches regex (str %)) (range start (inc end))))

(defn single-repeats-in-range [num-range]
  (matches-in-range #"^(.+)\1$" num-range))

(defn multiple-repeats-in-range [num-range]
  (matches-in-range #"^(.+)\1+$"  num-range))

(defn part1 [input]
  (m/sum (mapcat single-repeats-in-range (parse-ranges input))))

(defn part2 [input]
  (m/sum (mapcat multiple-repeats-in-range (parse-ranges input))))
