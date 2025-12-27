;; Test for aoc2016.day3
(ns aoc2016.day03-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day03 :as d]
   [clojure.test :refer :all]))

(def answers (delay (day-answers 2016 3)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
