;; Test for aoc2023.day13
(ns aoc2023.day13-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day13 :as d]
   [clojure.test :refer :all]))

(def example
  "#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#")

(deftest part1-example
  (is (= 405 (d/part1 example))))

(deftest part2-example
  (is (= 400 (d/part2 example))))

(def answers (delay (day-answers 2023 13)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
