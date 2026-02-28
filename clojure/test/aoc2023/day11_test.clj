;; Test for aoc2023.day11
(ns aoc2023.day11-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day11 :as d]
   [clojure.test :refer :all]))

(def example
  "...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....")

(deftest shortest-paths-example
  (are [expected input expansion]
       (= expected (d/shortest-paths input expansion))
    374 example 2
    1030 example 10
    8410 example 100))

(def answers (delay (day-answers 2023 11)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
