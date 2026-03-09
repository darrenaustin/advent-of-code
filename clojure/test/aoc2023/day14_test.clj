;; Test for aoc2023.day14
(ns aoc2023.day14-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day14 :as d]
   [clojure.test :refer :all]))

(def example
  "O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....")

(deftest part1-example
  (is (= 136 (d/part1 example))))

(deftest part2-example
  (is (= 64 (d/part2 example))))

(def answers (delay (day-answers 2023 14)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
