;; Test for aoc2016.day11
(ns aoc2016.day11-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day11 :as d]
   [clojure.test :refer :all]))

(def example
  "The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
 The second floor contains a hydrogen generator.
 The third floor contains a lithium generator.
 The fourth floor contains nothing relevant.")

(deftest part1-example
  (is (= 11 (d/part1 example))))

(def answers (delay (day-answers 2016 11)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
