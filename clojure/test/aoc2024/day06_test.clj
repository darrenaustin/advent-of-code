(ns aoc2024.day06-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2024.day06 :as d]
   [clojure.test :refer :all]))

(def example-input
  "....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...")

(deftest part1-test-example
  (is (= 41 (d/part1 example-input))))

(deftest part2-test-example
  (is (= 6 (d/part2 example-input))))

(def answers (delay (day-answers 2024 6)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
