;; Test for aoc2017.day11
(ns aoc2017.day11-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day11 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    3 "ne,ne,ne"
    0 "ne,ne,sw,sw"
    2 "ne,ne,s,s"
    3 "se,sw,se,sw,sw"))

(def answers (delay (day-answers 2017 11)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
