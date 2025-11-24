;; Test for aoc2018.day25
(ns aoc2018.day25-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day25 :as d]
            [clojure.test :refer :all]))

(def example1
  " 0,0,0,0
 3,0,0,0
 0,3,0,0
 0,0,3,0
 0,0,0,3
 0,0,0,6
 9,0,0,0
12,0,0,0")

(def example2
  "-1,2,2,0
0,0,2,-2
0,0,0,-2
-1,2,0,0
-2,-2,-2,2
3,0,2,-1
-1,3,2,2
-1,0,-1,0
0,2,1,-2
3,0,0,0")

(def example3
  "1,-1,0,1
2,0,-1,0
3,2,-1,0
0,0,3,1
0,0,-1,-1
2,3,-2,0
-2,2,0,0
2,-2,0,-1
1,-1,0,-1
3,2,0,2")

(def example4
  "1,-1,-1,-2
-2,-2,0,1
0,2,1,3
-2,3,-2,1
0,2,3,-2
-1,-1,1,-2
0,-2,-1,0
-2,2,3,-1
1,2,2,0
-1,-2,0,-2")

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    2 example1
    4 example2
    3 example3
    8 example4))

(def answers (delay (day-answers 2018 25)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
