;; https://adventofcode.com/2017/day/3
(ns aoc2017.day03
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.pos :as p]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2017 3))

(defn- spiral-dirs []
  (let [ring (mapcat #(repeat 2 %) (map inc (range)))
        dir (cycle [p/dir-right p/dir-up p/dir-left p/dir-down])]
    (mapcat repeat ring dir)))

(defn- spiral-positions []
  (reductions p/pos+ p/origin (spiral-dirs)))

(defn- sum-adjacent [grid pos]
  (reduce + (keep grid (p/adjacent-to pos))))

(defn- first-greater [target]
  (reduce
   (fn [grid pos]
     (let [value (sum-adjacent grid pos)]
       (if (> value target)
         (reduced value)
         (assoc grid pos value))))
   {p/origin 1}
   (rest (spiral-positions))))

(defn part1 [input]
  (->> (s/int input)
       dec
       (nth (spiral-positions))
       (m/manhattan-distance p/origin)))

(defn part2 [input] (first-greater (s/int input)))
