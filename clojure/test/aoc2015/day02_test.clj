(ns aoc2015.day02-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2015.day02 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    58 "2x3x4"
    43 "1x1x10"))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    34 "2x3x4"
    14 "1x1x10"))

(def answers (delay (day-answers 2015 2)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
