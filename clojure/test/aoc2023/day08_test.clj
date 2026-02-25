;; Test for aoc2023.day8
(ns aoc2023.day08-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day08 :as d]
   [clojure.test :refer :all]))

(def example1
  "RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)")

(def example2
  "LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)")

(def example3
  "LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)")

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    2 example1
    6 example2))

(deftest part2-example
  (is (= 6 (d/part2 example3))))

(def answers (delay (day-answers 2023 8)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
