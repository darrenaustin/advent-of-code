;; Test for aoc2024.day19
(ns aoc2024.day19-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day19 :as d]
            [clojure.test :refer :all]))

(def example
  "r, wr, b, g, bwu, rb, gb, br

brwrr
bggr
gbbr
rrbgbr
ubwu
bwurrg
brgr
bbrgwb")

(deftest part1-example
  (is (= 6 (d/part1 example))))

(deftest part2-example
  (is (= 16 (d/part2 example))))

(def answers (delay (day-answers 2024 19)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
