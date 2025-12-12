(ns aoc2024.day01-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2024.day01 :as d]
   [clojure.test :refer :all]))

(def example-input
  "3   4
4   3
2   5
1   3
3   9
3   3")

(deftest part1-test-example
  (is (= 11 (d/part1 example-input))))

(deftest part2-test-example
  (is (= 31 (d/part2 example-input))))

(def answers (delay (day-answers 2024 1)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
