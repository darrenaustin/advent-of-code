;; Test for aoc2020.day12
(ns aoc2020.day12-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2020.day12 :as d]
   [clojure.test :refer :all]))

(def example
  "F10
N3
F7
R90
F11")

(deftest part1-example
  (is (= 25 (d/part1 example))))

(deftest part2-example
  (is (= 286 (d/part2 example))))

(def answers (delay (day-answers 2020 12)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
