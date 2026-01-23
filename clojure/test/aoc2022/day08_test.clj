;; Test for aoc2022.day8
(ns aoc2022.day08-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day08 :as d]
   [clojure.test :refer :all]))

(def example
  "30373
25512
65332
33549
35390")

(deftest part1-example
  (is (= 21 (d/part1 example))))

(deftest part2-example
  (is (= 8 (d/part2 example))))

(def answers (delay (day-answers 2022 8)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
