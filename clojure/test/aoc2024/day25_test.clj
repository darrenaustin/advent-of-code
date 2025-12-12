;; Test for aoc2024.day25
(ns aoc2024.day25-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2024.day25 :as d]
   [clojure.test :refer :all]))

(def example
  "#####
.####
.####
.####
.#.#.
.#...
.....

#####
##.##
.#.##
...##
...#.
...#.
.....

.....
#....
#....
#...#
#.#.#
#.###
#####

.....
.....
#.#..
###..
###.#
###.#
#####

.....
.....
.....
#....
#.#..
#.#.#
#####")

(deftest fit?-examples
  (are [expected lock-key]
       (= expected (d/fit? lock-key))
    false [[0 5 3 4 3] [5 0 2 1 3]]
    false [[0 5 3 4 3] [4 3 4 0 2]]
    true  [[0 5 3 4 3] [3 0 2 0 1]]
    false [[1 2 0 5 3] [5 0 2 1 3]]
    true  [[1 2 0 5 3] [4 3 4 0 2]]
    true  [[1 2 0 5 3] [3 0 2 0 1]]))

(deftest part1-example
  (is (= 3 (d/part1 example))))

(def answers (delay (day-answers 2024 25)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
