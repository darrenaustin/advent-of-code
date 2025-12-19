;; Test for aoc2015.day15
(ns aoc2015.day15-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2015.day15 :as d]
   [clojure.test :refer :all]))

(def example
  "Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3")

(deftest part1-example
  (is (= 62842880 (d/part1 example))))

(deftest part2-example
  (is (= 57600000 (d/part2 example))))

(def answers (delay (day-answers 2015 15)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
