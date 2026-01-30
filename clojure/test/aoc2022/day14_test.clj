;; Test for aoc2022.day14
(ns aoc2022.day14-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day14 :as d]
   [clojure.test :refer :all]))

(def example
  "498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9")

(deftest part1-example
  (is (= 24 (d/part1 example))))

(deftest part2-example
  (is (= 93 (d/part2 example))))

(def answers (delay (day-answers 2022 14)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
