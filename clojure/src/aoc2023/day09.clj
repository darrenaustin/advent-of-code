;; https://adventofcode.com/2023/day/9
(ns aoc2023.day09
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2023 9))

(defn- parse-dataset [input] (map s/ints (s/lines input)))

;; I had a slightly more complicated version of this (see below).
;; Then I saw this super elegant solution from:
;;
;; https://github.com/tschady/advent-of-code/blob/main/src/aoc/2023/d09.clj

(defn- extrapolate [xs]
  (if (every? zero? xs)
    0
    (+ (last xs) (extrapolate (m/intervals xs)))))

(defn part1 [input]
  (transduce (map extrapolate) + (parse-dataset input)))

(defn part2 [input]
  (transduce (map (comp extrapolate reverse)) + (parse-dataset input)))

;; My original solution:
;;
;; (defn- diffs [xs]
;;   (take-while (complement #(every? zero? %)) (iterate m/intervals xs)))
;;
;; (defn- extrapolate-next [diff-xs]
;;   (reduce + (map last diff-xs)))
;;
;; (defn- extrapolate-previous [diff-xs]
;;   (reduce #(- %2 %1) (map first (reverse diff-xs))))
;;
;; (defn- sum-extrapolation [input extrap-fn]
;;   (->> (parse-dataset input)
;;        (map diffs)
;;        (map extrap-fn)
;;        (reduce +)))
;;
;; (defn part1 [input] (sum-extrapolation input extrapolate-next))
;;
;; (defn part2 [input] (sum-extrapolation input extrapolate-previous))
