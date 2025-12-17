;; Test for aoc2023.day1
(ns aoc2023.day01-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day01 :as d]
   [clojure.test :refer :all]))

(def example1
  "1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet")

(deftest part1-example
  (is (= 142 (d/part1 example1))))

(def example2
  "two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen")

(deftest part2-example
  (is (= 281 (d/part2 example2))))

(def answers (delay (day-answers 2023 1)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
