;; https://adventofcode.com/2024/day/2
(ns aoc2024.day02
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(def input (d/day-input 2024 2))

(defn parse-reports [input]
  (->> input
       str/split-lines
       (map s/parse-nums)))

(defn safe [report]
  (let [diffs (map - report (rest report))]
    (and (or (every? pos? diffs)
             (every? neg? diffs))
         (every? #(<= 1 (abs %) 3) diffs))))

(defn part1 [input]
  (count (filter safe (parse-reports input))))

(defn missing-one [report]
  (for [idx (range (count report))]
    (concat (take idx report) (drop (inc idx) report))))

(defn safe-ish [report]
  (some safe (missing-one report)))

(defn part2 [input]
  (count (filter safe-ish (parse-reports input))))
