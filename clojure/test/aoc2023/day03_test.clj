;; Test for aoc2023.day3
(ns aoc2023.day03-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day03 :as d]
   [clojure.test :refer :all]))

(def example
  "467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..")

(deftest part1-example
  (is (= 4361 (d/part1 example))))

(deftest part2-example
  (is (= 467835 (d/part2 example))))

(def answers (delay (day-answers 2023 3)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
