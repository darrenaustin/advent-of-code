;; Test for aoc2025.day10
(ns aoc2025.day10-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2025.day10 :as d]
            [clojure.test :refer :all]))

(def example
  "")

(deftest part1-example
  (is (= nil (d/part1 example))))

(deftest part1-examples
  (are [expected input]
    (= expected (d/part1 input))
    nil ""))

(deftest part2-example
  (is (= nil (d/part2 example))))

(deftest part2-examples
  (are [expected input]
    (= expected (d/part2 input))
    nil ""))

(def answers (delay (day-answers 2025 10)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
