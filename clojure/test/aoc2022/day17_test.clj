;; Test for aoc2022.day17
(ns aoc2022.day17-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day17 :as d]
   [clojure.test :refer :all]))

(def example ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>")

(deftest part1-example
  (is (= 3068 (d/part1 example))))

(deftest part2-example
  (is (= 1514285714288 (d/part2 example))))

(def answers (delay (day-answers 2022 17)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
