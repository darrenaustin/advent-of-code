;; Test for aoc2018.day22
(ns aoc2018.day22-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2018.day22 :as d]
   [clojure.test :refer :all]))

(def example
  "depth: 510
target: 10,10")

(deftest part1-example
  (is (= 114 (d/part1 example))))

(deftest part2-example
  (is (= 45 (d/part2 example))))

(def answers (delay (day-answers 2018 22)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
