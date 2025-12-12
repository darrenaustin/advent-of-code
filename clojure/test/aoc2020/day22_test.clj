;; Test for aoc2020.day22
(ns aoc2020.day22-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2020.day22 :as d]
   [clojure.test :refer :all]))

(def example
  "Player 1:
9
2
6
3
1

Player 2:
5
8
4
7
10")

(deftest part1-example
  (is (= 306 (d/part1 example))))

(deftest part2-example
  (is (= 291 (d/part2 example))))

(def answers (delay (day-answers 2020 22)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
