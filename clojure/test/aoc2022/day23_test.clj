;; Test for aoc2022.day23
(ns aoc2022.day23-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day23 :as d]
   [clojure.test :refer :all]))

(def example
  "....#..
..###.#
#...#.#
.#...##
#.###..
##.#.##
.#..#..")

(deftest part1-example
  (is (= 110 (d/part1 example))))

(deftest part2-example
  (is (= 20 (d/part2 example))))

(def answers (delay (day-answers 2022 23)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
