;; Test for aoc2019.day8
(ns aoc2019.day08-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2019.day08 :as d]
   [clojure.test :refer :all]))

(def example
  "123456789012")

(deftest part1-example
  (is (= 1 (d/part1 example))))

(def answers (delay (day-answers 2019 8)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
