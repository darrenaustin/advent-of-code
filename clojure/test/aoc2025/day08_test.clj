;; Test for aoc2025.day8
(ns aoc2025.day08-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2025.day08 :as d]
   [clojure.test :refer :all]))

(def example
  "162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689")

(deftest part1-example
  (is (= 40 (d/part1 example 10))))

(deftest part2-example
  (is (= 25272 (d/part2 example))))

(def answers (delay (day-answers 2025 8)))
(def input (delay (d/input)))

(deftest ^:slow part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
