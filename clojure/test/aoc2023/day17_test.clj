;; Test for aoc2023.day17
(ns aoc2023.day17-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day17 :as d]
   [clojure.test :refer :all]))

(def example
  "2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533")

(deftest part1-example
  (is (= 102 (d/part1 example))))

(deftest part2-example
  (is (= 94 (d/part2 example))))

(def answers (delay (day-answers 2023 17)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
