;; Test for aoc2019.day17
(ns aoc2019.day17-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc.util.grid :as g]
   [aoc2019.day17 :as d]
   [clojure.test :refer :all]))

(def example1-grid
  "..#..........
..#..........
#######...###
#.#...#...#.#
#############
..#...#...#..
..#####...^..")

(deftest part1-example
  (is (= 76 (d/sum-intersections (g/parse-grid example1-grid)))))

(def answers (delay (day-answers 2019 17)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
