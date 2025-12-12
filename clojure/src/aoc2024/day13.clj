;; https://adventofcode.com/2024/day/13
(ns aoc2024.day13
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [aoc.util.vec :as v]))

(defn input [] (d/day-input 2024 13))

(defn parse [input]
  (partition 3 (partition 2 (s/parse-ints input))))

;; a * Ax + b * Bx = Px => a * Ax = Px - b * Bx => a = (Px - b * Bx) / Ax
;; a * Ay + b * By = Py => ((Px - b * Bx) / Ax) * Ay + b * By = Py
;; => (Px * Ay) / Ax - (b Bx Ay) / Ax + b By = Py
;; => Py - (Px * Ay) / Ax = b (By - (Bx Ay)/Ax)
;;
;; a = (Px - b * Bx) / Ax
;; b = (Py - (Px * Ay) / Ax) / (By - (Bx Ay)/Ax)

(defn button-presses [[[ax ay] [bx by] [px py]]]
  (let [b (/ (- py (/ (* px ay) ax)) (- by (/ (* bx ay) ax)))
        a (/ (- px (* b bx)) ax)]
    [a b]))

(defn cost-of [[ac bc]]
  (if-not (or (ratio? ac) (ratio? bc))
    (+ (* 3 ac) bc)
    0))

(defn adjust-prize [prize-fn [a b p]]
  [a b (prize-fn p)])

(defn solve [input prize-fn]
  (->> input
       parse
       (map (comp cost-of button-presses #(adjust-prize prize-fn %)))
       m/sum))

(defn part1 [input]
  (solve input identity))

(defn part2 [input]
  (solve input #(v/vec+ [10000000000000 10000000000000] %)))
