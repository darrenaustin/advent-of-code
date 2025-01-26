(ns aoc2015.day01-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2015.day01 :as d]
            [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
    (= expected (d/part1 input))
    0 "(())"
    0 "()()"
    3 "((("
    3 "(()(()("
    3 "))((((("
    -1 "())"
    -1 "))("
    -3 ")))"
    -3 ")())())"))

(deftest part2-example
  (are [expected input]
    (= expected (d/part2 input))
    1 ")"
    5 "()())"))

(def answers (delay (day-answers 2015 1)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
