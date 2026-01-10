;; Test for aoc2019.day3
(ns aoc2019.day03-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2019.day03 :as d]
   [clojure.test :refer :all]))

(def example1
  "R8,U5,L5,D3
U7,R6,D4,L4")

(def example2
  "R75,D30,R83,U83,L12,D49,R71,U7,L72
U62,R66,U55,R34,D71,R55,D58,R83")

(def example3
  "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    6 example1
    159 example2
    135 example3))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    30 example1
    610 example2
    410 example3))

(def answers (delay (day-answers 2019 3)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
