(ns aoc2024.day10-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2024.day10 :as d]
   [clojure.test :refer :all]))

(def example-input
  "89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732")

(deftest part1-example
  (is (= 36 (d/part1 example-input))))

(deftest part2-example
  (is (= 81 (d/part2 example-input))))

(def answers (delay (day-answers 2024 10)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
