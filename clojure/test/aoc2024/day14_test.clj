;; Test for aoc2024.day14
(ns aoc2024.day14-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day14 :as d]
            [clojure.test :refer :all]))

(def example-input
  "p=0,4 v=3,-3
p=6,3 v=-1,-3
p=10,3 v=-1,2
p=2,0 v=2,-1
p=0,0 v=1,3
p=3,0 v=-2,-2
p=7,6 v=-1,-3
p=3,0 v=-1,-2
p=9,3 v=2,3
p=7,3 v=-1,2
p=2,4 v=2,-3
p=9,5 v=-3,-3")

(deftest part1-example
  (is (= 12 (d/part1 example-input [11 7]))))

(def answers (delay (day-answers 2024 14)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
