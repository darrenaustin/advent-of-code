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

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2024 13) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
