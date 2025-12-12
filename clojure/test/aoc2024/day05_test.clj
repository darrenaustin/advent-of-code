(ns aoc2024.day05-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2024.day05 :as d]
   [clojure.test :refer :all]))

(def example-input
  "47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47")

(deftest part1-example
  (is (= 143 (d/part1 example-input))))

(deftest part2-example
  (is (= 123 (d/part2 example-input))))

(def answers (delay (day-answers 2024 5)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
