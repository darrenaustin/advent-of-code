;; Test for aoc2020.day10
(ns aoc2020.day10-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2020.day10 :as d]
   [clojure.test :refer :all]))

(def example1
  "16
10
15
5
1
11
7
19
6
12
4")

(def example2
  "28
33
18
42
31
14
46
20
48
47
24
23
49
45
19
38
39
11
1
32
25
35
8
17
7
9
4
2
34
10
3")

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    35 example1
    220 example2))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    8 example1
    19208 example2))

(def answers (delay (day-answers 2020 10)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
