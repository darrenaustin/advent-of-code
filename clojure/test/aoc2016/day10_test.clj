;; Test for aoc2016.day10
(ns aoc2016.day10-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day10 :as d]
   [clojure.test :refer :all]))

(def example
  "value 5 goes to bot 2
bot 2 gives low to bot 1 and high to bot 0
value 3 goes to bot 1
bot 1 gives low to output 1 and high to bot 0
bot 0 gives low to output 2 and high to output 0
value 2 goes to bot 2")

(deftest part1-example
  (is (= 2 (d/part1 example #{5 2}))))

(deftest part2-example
  (is (= 30 (d/part2 example))))

(def answers (delay (day-answers 2016 10)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
