;; Test for aoc2015.day4
(ns aoc2015.day04-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2015.day04 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    609043 "abcdef"
    1048970 "pqrstuv"))

(def answers (delay (day-answers 2015 4)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
