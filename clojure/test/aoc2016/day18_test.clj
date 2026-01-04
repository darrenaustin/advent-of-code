;; Test for aoc2016.day18
(ns aoc2016.day18-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day18 :as d]
   [clojure.test :refer :all]))

(def example ".^^.^.^^^^")

(deftest part1-example
  (is (= 38 (d/part1 example 10))))

(def answers (delay (day-answers 2016 18)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
