;; Test for aoc2025.day1
(ns aoc2025.day01-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2025.day01 :as d]
            [clojure.test :refer :all]))

(def example
  "L68
L30
R48
L5
R60
L55
L1
L99
R14
L82")

(deftest part1-example
  (is (= 3 (d/part1 example))))

(deftest part2-example
  (is (= 6 (d/part2 example))))

(def answers (delay (day-answers 2025 1)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
