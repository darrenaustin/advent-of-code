;; Test for aoc2020.day5
(ns aoc2020.day05-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2020.day05 :as d]
   [clojure.test :refer :all]))

(deftest seat-id-examples
  (are [expected input]
       (= expected (d/seat-id input))
    357 "FBFBBFFRLR"
    567 "BFFFBBFRRR"
    119 "FFFBBBFRRR"
    820 "BBFFBBFRLL"))

(def answers (delay (day-answers 2020 5)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
