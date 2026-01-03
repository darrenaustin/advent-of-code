;; Test for aoc2016.day16
(ns aoc2016.day16-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day16 :as d]
   [clojure.test :refer :all]))

(deftest part1-example
  (is (= "01100" (d/part1 "10000" 20))))

(def answers (delay (day-answers 2016 16)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
