;; Test for aoc2018.day14
(ns aoc2018.day14-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2018.day14 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    "5158916779" "9"
    "0124515891" "5"
    "9251071085" "18"
    "5941429882" "2018"))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    9 "51589"
    5 "01245"
    18 "92510"
    2018 "59414"))

(def answers (delay (day-answers 2018 14)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
