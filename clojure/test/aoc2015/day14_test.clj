;; Test for aoc2015.day14
(ns aoc2015.day14-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2015.day14 :as d]
   [clojure.test :refer :all]))

(def example
  "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds")

(deftest part1-example
  (is (= 1120 (d/part1 example 1000))))

(deftest part2-example
  (is (= 689 (d/part2 example 1000))))

(def answers (delay (day-answers 2015 14)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
