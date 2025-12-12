;; Test for aoc2017.day21
(ns aoc2017.day21-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day21 :as d]
   [clojure.test :refer :all]))

(def example
  "../.# => ##./#../...
.#./..#/### => #..#/..../..../#..#")

(deftest example-test
  (is (= 12 (d/on-after-iterations example 2))))

(def answers (delay (day-answers 2017 21)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
