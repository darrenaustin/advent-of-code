(ns aoc2024.day02-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2024.day02 :as d]
   [clojure.test :refer :all]))

(def example-input
  "7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9")

(deftest part1-test-example
  (is (= 2 (d/part1 example-input))))

(deftest part2-test-example
  (is (= 4 (d/part2 example-input))))

(def answers (delay (day-answers 2024 2)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
