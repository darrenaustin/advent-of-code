;; Test for aoc2020.day25
(ns aoc2020.day25-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2020.day25 :as d]
            [clojure.test :refer :all]))

(def example
  "5764801
17807724")

(deftest part1-example
  (is (= 14897079 (d/part1 example))))

(def answers (delay (day-answers 2020 25)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
