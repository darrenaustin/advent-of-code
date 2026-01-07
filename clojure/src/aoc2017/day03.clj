;; https://adventofcode.com/2017/day/3
(ns aoc2017.day03
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.pos :as p]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2017 3))

;; This elegant lazy sequence of dirs was taken from:
;;
;; https://github.com/tschady/advent-of-code/blob/main/src/aoc/2017/d03.clj
(defn- spiral-dirs []
  (let [steps (mapcat #(repeat 2 %) (map inc (range)))
        dirs (cycle [p/dir-right p/dir-up p/dir-left p/dir-down])]
    (mapcat repeat steps dirs)))

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
  (->> (dec (s/int input))
       (nth (spiral-positions))
       (m/manhattan-distance p/origin)))

(defn part2 [input] (first-greater (s/int input)))
