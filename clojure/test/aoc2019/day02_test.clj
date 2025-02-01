;; Test for aoc2019.day2
(ns aoc2019.day02-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2019.day02 :as d]
            [clojure.test :refer :all]))

(def answers (delay (day-answers 2019 2)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
