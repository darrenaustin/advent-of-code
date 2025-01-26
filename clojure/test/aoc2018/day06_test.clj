(ns aoc2018.day06-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day06 :as d]
            [clojure.test :refer :all]))

(def example-input
  "1, 1
1, 6
8, 3
3, 4
5, 5
8, 9")

(deftest part1-example
  (is (= 17 (d/part1 example-input))))

(deftest part2-test-example
  (is (= 16 (d/part2 example-input 32))))

(def answers (delay (day-answers 2018 6)))
(def input (delay (d/input)))

(deftest ^:slow part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
