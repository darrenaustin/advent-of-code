;; Test for aoc2024.day18
(ns aoc2024.day18-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day18 :as d]
            [clojure.test :refer :all]))

(def example
  "5,4
4,2
4,5
3,0
2,1
6,3
2,4
1,5
0,6
3,3
2,6
5,1
1,2
5,5
2,5
6,5
1,4
0,4
6,4
1,1
6,1
1,0
0,5
1,6
2,0")

(deftest part1-example
  (is (= 22 (d/part1 example 12 [6 6]))))

(deftest part2-example
  (is (= "6,1" (d/part2 example 12 [6 6]))))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2024 18) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
