;; Test for aoc2016.day13
(ns aoc2016.day13-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day13 :as d]
   [clojure.test :refer :all]))

(def example "10")

(deftest part1-example
  (is (= 11 (d/part1 example [7 4]))))

(def answers (delay (day-answers 2016 13)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
