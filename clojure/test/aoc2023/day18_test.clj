;; Test for aoc2023.day18
(ns aoc2023.day18-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day18 :as d]
   [clojure.test :refer :all]))

(def example
  "R 6 (#70c710)
D 5 (#0dc571)
L 2 (#5713f0)
D 2 (#d2c081)
R 2 (#59c680)
D 2 (#411b91)
L 5 (#8ceee2)
U 2 (#caa173)
L 1 (#1b58a2)
U 2 (#caa171)
R 2 (#7807d2)
U 3 (#a77fa3)
L 2 (#015232)
U 2 (#7a21e3)")

(deftest part1-example
  (is (= 62 (d/part1 example))))

(deftest part2-example
  (is (= 952408144115 (d/part2 example))))

(def answers (delay (day-answers 2023 18)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
