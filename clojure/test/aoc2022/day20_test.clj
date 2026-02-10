;; Test for aoc2022.day20
(ns aoc2022.day20-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day20 :as d]
   [clojure.test :refer :all]))

(def example
  "1
2
-3
3
-2
0
4")

(deftest part1-example
  (is (= 3 (d/part1 example))))

(deftest part2-example
  (is (= 1623178306 (d/part2 example))))

(def answers (delay (day-answers 2022 20)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
