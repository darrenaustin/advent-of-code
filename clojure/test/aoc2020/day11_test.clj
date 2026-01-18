;; Test for aoc2020.day11
(ns aoc2020.day11-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2020.day11 :as d]
   [clojure.test :refer :all]))

(def example
  "L.LL.LL.LL
LLLLLLL.LL
L.L.L..L..
LLLL.LL.LL
L.LL.LL.LL
L.LLLLL.LL
..L.L.....
LLLLLLLLLL
L.LLLLLL.L
L.LLLLL.LL")

(deftest part1-example
  (is (= 37 (d/part1 example))))

(deftest part2-example
  (is (= 26 (d/part2 example))))

(def answers (delay (day-answers 2020 11)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
