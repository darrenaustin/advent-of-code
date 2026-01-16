;; Test for aoc2020.day2
(ns aoc2020.day02-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2020.day02 :as d]
   [clojure.test :refer :all]))

(def example
  "1-3 a: abcde
1-3 b: cdefg
2-9 c: ccccccccc")

(deftest part1-example
  (is (= 2 (d/part1 example))))

(deftest part2-example
  (is (= 1 (d/part2 example))))

(def answers (delay (day-answers 2020 2)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
