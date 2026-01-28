;; Test for aoc2022.day13
(ns aoc2022.day13-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day13 :as d]
   [clojure.test :refer :all]))

(def example
  "[1,1,3,1,1]
[1,1,5,1,1]

[[1],[2,3,4]]
[[1],4]

[9]
[[8,7,6]]

[[4,4],4,4]
[[4,4],4,4,4]

[7,7,7,7]
[7,7,7]

[]
[3]

[[[]]]
[[]]

[1,[2,[3,[4,[5,6,7]]]],8,9]
[1,[2,[3,[4,[5,6,0]]]],8,9]")

(deftest part1-example
  (is (= 13 (d/part1 example))))

(deftest part2-example
  (is (= 140 (d/part2 example))))

(def answers (delay (day-answers 2022 13)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
