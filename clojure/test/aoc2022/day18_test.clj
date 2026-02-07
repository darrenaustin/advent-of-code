;; Test for aoc2022.day18
(ns aoc2022.day18-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day18 :as d]
   [clojure.test :refer :all]))

(def example
  "2,2,2
1,2,2
3,2,2
2,1,2
2,3,2
2,2,1
2,2,3
2,2,4
2,2,6
1,2,5
3,2,5
2,1,5
2,3,5")

(deftest part1-example
  (is (= 64 (d/part1 example))))

(deftest part2-example
  (is (= 58 (d/part2 example))))

(def answers (delay (day-answers 2022 18)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
