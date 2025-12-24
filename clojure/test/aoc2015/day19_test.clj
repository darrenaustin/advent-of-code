;; Test for aoc2015.day19
(ns aoc2015.day19-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2015.day19 :as d]
   [clojure.test :refer :all]))

(def example1
  "H => HO
H => OH
O => HH

HOHOHO")

(def example2
  "e => H
e => O
H => HO
H => OH
O => HH

HOHOHO")

(deftest part1-example
  (is (= 7 (d/part1 example1))))

(deftest part2-example
  (is (= 6 (d/part2 example2))))

(def answers (delay (day-answers 2015 19)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
