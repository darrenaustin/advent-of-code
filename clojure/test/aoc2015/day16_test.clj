;; Test for aoc2015.day16
(ns aoc2015.day16-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2015.day16 :as d]
   [clojure.test :refer :all]))

(def answers (delay (day-answers 2015 16)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
