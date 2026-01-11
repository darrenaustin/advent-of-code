;; Test for aoc2019.day6
(ns aoc2019.day06-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2019.day06 :as d]
   [clojure.test :refer :all]))

(def example1
  "COM)B
B)C
C)D
D)E
E)F
B)G
G)H
D)I
E)J
J)K
K)L")

(def example2
  "COM)B
B)C
C)D
D)E
E)F
B)G
G)H
D)I
E)J
J)K
K)L
K)YOU
I)SAN")

(deftest part1-example
  (is (= 42 (d/part1 example1))))

(deftest part2-example
  (is (= 4 (d/part2 example2))))

(def answers (delay (day-answers 2019 6)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
