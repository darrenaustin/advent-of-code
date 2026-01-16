;; Test for aoc2020.day3
(ns aoc2020.day03-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2020.day03 :as d]
   [clojure.test :refer :all]))

(def example
  "..##.......
#...#...#..
.#....#..#.
..#.#...#.#
.#...##..#.
..#.##.....
.#.#.#....#
.#........#
#.##...#...
#...##....#
.#..#...#.#")

(deftest part1-example
  (is (= 7 (d/part1 example))))

(deftest part2-example
  (is (= 336 (d/part2 example))))

(def answers (delay (day-answers 2020 3)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
