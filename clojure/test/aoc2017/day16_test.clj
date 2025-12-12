;; Test for aoc2017.day16
(ns aoc2017.day16-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day16 :as d]
   [clojure.test :refer :all]))

(def example "s1,x3/4,pe/b")

(deftest part1-example
  (is (= "baedc" (d/part1 example "abcde"))))

(def answers (delay (day-answers 2017 16)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
