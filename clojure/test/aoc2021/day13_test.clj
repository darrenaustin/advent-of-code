;; Test for aoc2021.day13
(ns aoc2021.day13-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2021.day13 :as d]
   [clojure.test :refer :all]))

(def example
  "6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5")

(deftest part1-example
  (is (= 17 (d/part1 example))))

(def answers (delay (day-answers 2021 13)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
