;; Test for aoc2015.day18
(ns aoc2015.day18-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2015.day18 :as d]
   [clojure.test :refer :all]))

(def example
  ".#.#.#
...##.
#....#
..#...
#.#..#
####..")

(deftest part1-example
  (is (= 4 (d/part1 example 4))))

(deftest part2-example
  (is (= 17 (d/part2 example 5))))

(def answers (delay (day-answers 2015 18)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
