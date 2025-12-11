;; Test for aoc2025.day10
(ns aoc2025.day10-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2025.day10 :as d]
            [clojure.test :refer :all]))

(def example
  "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}")

(deftest part1-example
  (is (= 7 (d/part1 example))))

(deftest part2-example
  (is (= 33 (d/part2 example))))

(def answers (delay (day-answers 2025 10)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
