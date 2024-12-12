(ns aoc2015.day01-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2015.day01 :as d]
            [clojure.test :refer [deftest are is]]))

(deftest part1-examples
  (are [expected input] (= expected (d/part1 input))
    0  "(())"
    0  "()()"
    3  "((("
    3  "(()(()("
    3  "))((((("
    -1 "())"
    -1 "))("
    -3 ")))"
    -3 ")())())"))

(deftest part2-example
  (are [expected input] (= expected (d/part2 input))
    1 ")"
    5 "()())"))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2015 1)]
    (is (= answer1 (d/part1 d/input)))
    (is (= answer2 (d/part2 d/input)))))
