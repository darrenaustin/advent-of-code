;; Test for aoc2016.day21
(ns aoc2016.day21-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day21 :as d]
   [clojure.test :refer :all]))

(def example
  "swap position 4 with position 0
swap letter d with letter b
reverse positions 0 through 4
rotate left 1
move position 1 to position 4
move position 3 to position 0
rotate based on position of letter b
rotate based on position of letter d")

(deftest scramble-example
  (is (= "decab" (d/scramble "abcde" example))))

(deftest un-scramble-example
  (is (= "abcde" (d/un-scramble "decab" example))))

(def answers (delay (day-answers 2016 21)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
