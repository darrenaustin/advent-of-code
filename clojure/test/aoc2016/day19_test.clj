;; Test for aoc2016.day19
(ns aoc2016.day19-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day19 :as d]
   [clojure.test :refer :all]))

(def example "5")

(deftest part1-example
  (is (= 3 (d/part1 example))))

(deftest part2-example
  (is (= 2 (d/part2 example))))

(def answers (delay (day-answers 2016 19)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
