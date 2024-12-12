;; Test for aoc2024.day11
(ns aoc2024.day11-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day11 :as d]
            [clojure.test :refer :all]))

(deftest example
  (are [expected input] (= expected (d/blink "125 17" input))
    2     0
    3     1
    4     2
    5     3
    9     4
    13    5
    22    6
    55312 25))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2024 11)]
    (is (= answer1 (d/part1 d/input)))
    (is (= answer2 (d/part2 d/input)))))
