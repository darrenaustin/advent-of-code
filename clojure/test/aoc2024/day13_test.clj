;; Test for aoc2024.day13
(ns aoc2024.day13-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day13 :as d]
            [clojure.test :refer :all]))

(def example-input
  "Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279")

(deftest part1-example
  (is (= 480 (d/part1 example-input))))

(def answers (delay (day-answers 2024 13)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
