;; Test for aoc2022.day9
(ns aoc2022.day09-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day09 :as d]
   [clojure.test :refer :all]))

(def example1
  "R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2")

(def example2
  "R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20")

(deftest part1-example
  (is (= 13 (d/part1 example1))))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    1 example1
    36 example2))

(def answers (delay (day-answers 2022 9)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
