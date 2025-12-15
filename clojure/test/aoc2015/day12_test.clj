;; Test for aoc2015.day12
(ns aoc2015.day12-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2015.day12 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    6 "[1,2,3]"
    6 "{\"a\":2,\"b\":4}"
    3 "[[[3]]]"
    3 "{\"a\":{\"b\":4},\"c\":-1}"
    0 "{\"a\":[-1,1]}"
    0 "[-1,{\"a\":1}]"
    0 "[]"
    0 "{}"))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    6 "[1,2,3]"
    4 "[1,{\"c\":\"red\",\"b\":2},3]"
    0 "{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}"
    6 "[1,\"red\",5]"))

(def answers (delay (day-answers 2015 12)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
