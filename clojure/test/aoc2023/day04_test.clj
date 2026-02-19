;; Test for aoc2023.day4
(ns aoc2023.day04-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day04 :as d]
   [clojure.test :refer :all]))

(def example
  "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11")

(deftest part1-example
  (is (= 13 (d/part1 example))))

(deftest part2-example
  (is (= 30 (d/part2 example))))

(def answers (delay (day-answers 2023 4)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
