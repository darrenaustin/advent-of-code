;; Test for aoc2019.day9
(ns aoc2019.day09-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2019.day09 :as d]
   [clojure.test :refer :all]))

(def answers (delay (day-answers 2019 9)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
