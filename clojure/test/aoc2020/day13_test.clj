;; Test for aoc2020.day13
(ns aoc2020.day13-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2020.day13 :as d]
   [clojure.test :refer :all]))

(def example
  "939
7,13,x,x,59,x,31,19")

(deftest part1-example
  (is (= 295 (d/part1 example))))

(deftest part2-example
  (is (= 1068781 (d/part2 example))))

(def answers (delay (day-answers 2020 13)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
