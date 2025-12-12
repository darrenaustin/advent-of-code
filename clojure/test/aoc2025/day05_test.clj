;; Test for aoc2025.day5
(ns aoc2025.day05-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2025.day05 :as d]
   [clojure.test :refer :all]))

(def example
  "3-5
10-14
16-20
12-18

1
5
8
11
17
32")

(deftest part1-example
  (is (= 3 (d/part1 example))))

(deftest part2-example
  (is (= 14 (d/part2 example))))

(def answers (delay (day-answers 2025 5)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
