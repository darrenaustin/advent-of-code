;; Test for aoc2020.day8
(ns aoc2020.day08-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2020.day08 :as d]
   [clojure.test :refer :all]))

(def example
  "nop +0
acc +1
jmp +4
acc +3
jmp -3
acc -99
acc +1
jmp -4
acc +6")

(deftest part1-example
  (is (= 5 (d/part1 example))))

(deftest part2-example
  (is (= 8 (d/part2 example))))

(def answers (delay (day-answers 2020 8)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
