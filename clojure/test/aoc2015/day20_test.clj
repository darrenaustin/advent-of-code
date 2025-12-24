;; Test for aoc2015.day20
(ns aoc2015.day20-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2015.day20 :as d]
   [clojure.test :refer :all]))

(def answers (delay (day-answers 2015 20)))
(def input (delay (d/input)))

(deftest ^:slow part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
