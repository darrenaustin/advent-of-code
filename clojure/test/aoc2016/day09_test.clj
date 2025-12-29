;; Test for aoc2016.day9
(ns aoc2016.day09-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day09 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    6 "ADVENT"
    7 "A(1x5)BC"
    9 "(3x3)XYZ"
    11 "A(2x2)BCD(2x2)EFG"
    6 "(6x1)(1x3)A"
    18 "X(8x2)(3x3)ABCY"))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    9 "(3x3)XYZ"
    20 "X(8x2)(3x3)ABCY"
    241920 "(27x12)(20x12)(13x14)(7x10)(1x12)A"
    445 "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN"))

(def answers (delay (day-answers 2016 9)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
