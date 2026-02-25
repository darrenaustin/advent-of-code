;; Test for aoc2023.day7
(ns aoc2023.day07-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day07 :as d]
   [clojure.test :refer :all]))

(def example
  "32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483")

(deftest part1-example
  (is (= 6440 (d/part1 example))))

(deftest part2-example
  (is (= 5905 (d/part2 example))))

(def answers (delay (day-answers 2023 7)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
