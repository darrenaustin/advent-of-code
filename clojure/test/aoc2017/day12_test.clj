;; Test for aoc2017.day12
(ns aoc2017.day12-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day12 :as d]
   [clojure.test :refer :all]))

(def example
  "0 <-> 2
1 <-> 1
2 <-> 0, 3, 4
3 <-> 2, 4
4 <-> 2, 3, 6
5 <-> 6
6 <-> 4, 5")

(deftest part1-example
  (is (= 6 (d/part1 example))))

(deftest part2-example
  (is (= 2 (d/part2 example))))

(def answers (delay (day-answers 2017 12)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
