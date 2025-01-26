;; Test for aoc2017.day23
(ns aoc2017.day23-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2017.day23 :as d]
            [clojure.test :refer :all]))

(def answers (delay (day-answers 2017 23)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
