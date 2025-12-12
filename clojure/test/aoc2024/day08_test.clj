(ns aoc2024.day08-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2024.day08 :as d]
   [clojure.test :refer :all]))

(def example-input
  "............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............")

(deftest part1-example
  (is (= 14 (d/part1 example-input))))

(deftest part2-example
  (is (= 34 (d/part2 example-input))))

(def answers (delay (day-answers 2024 8)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
