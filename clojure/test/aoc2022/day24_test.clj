;; Test for aoc2022.day24
(ns aoc2022.day24-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day24 :as d]
   [clojure.test :refer :all]))

(def example
  "#.######
#>>.<^<#
#.<..<<#
#>v.><>#
#<^v^^>#
######.#")

(deftest part1-example
  (is (= 18 (d/part1 example))))

(deftest part2-example
  (is (= 54 (d/part2 example))))

(def answers (delay (day-answers 2022 24)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
