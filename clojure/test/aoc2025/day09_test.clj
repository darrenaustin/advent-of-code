;; Test for aoc2025.day9
(ns aoc2025.day09-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2025.day09 :as d]
   [clojure.test :refer :all]))

(def example
  "7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3")

(deftest part1-example
  (is (= 50 (d/part1 example))))

(deftest part2-example
  (is (= 24 (d/part2 example))))

(def answers (delay (day-answers 2025 9)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
