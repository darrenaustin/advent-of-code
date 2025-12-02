;; https://adventofcode.com/2025/day/2
(ns aoc2025.day02
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2025 2))

(defn parse-ranges [input]
  (map (fn [[_ start end]] [(s/parse-int start) (s/parse-int end)]) (re-seq #"(\d+)-(\d+)" input)))

(defn single-repeats-in-range [[start end]]
  (filter #(re-matches #"(.*)(\1)" (str %)) (range start (inc end))))

(defn multiple-repeats-in-range [[start end]]
  (filter #(re-matches #"(.*)((\1)+)" (str %)) (range start (inc end))))

(defn part1 [input]
  (m/sum (flatten (map single-repeats-in-range (parse-ranges input)))))

(defn part2 [input]
  (m/sum (flatten (map multiple-repeats-in-range (parse-ranges input)))))
