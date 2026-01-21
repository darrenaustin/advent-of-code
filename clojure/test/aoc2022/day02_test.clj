;; Test for aoc2022.day2
(ns aoc2022.day02-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day02 :as d]
   [clojure.test :refer :all]))

(def example
  "A Y
B X
C Z")

(deftest part1-example
  (is (= 15 (d/part1 example))))

(deftest part2-example
  (is (= 12 (d/part2 example))))

(def answers (delay (day-answers 2022 2)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
