;; Test for aoc2020.day14
(ns aoc2020.day14-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2020.day14 :as d]
   [clojure.test :refer :all]))

(def example1
  "mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
mem[8] = 11
mem[7] = 101
mem[8] = 0")

(def example2
  "mask = 000000000000000000000000000000X1001X
mem[42] = 100
mask = 00000000000000000000000000000000X0XX
mem[26] = 1")

(deftest part1-example
  (is (= 165 (d/part1 example1))))

(deftest part2-example
  (is (= 208 (d/part2 example2))))

(def answers (delay (day-answers 2020 14)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
