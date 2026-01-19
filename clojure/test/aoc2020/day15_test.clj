;; Test for aoc2020.day15
(ns aoc2020.day15-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2020.day15 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    436  "0,3,6"
    1    "1,3,2"
    10   "2,1,3"
    27   "1,2,3"
    78   "2,3,1"
    438  "3,2,1"
    1836 "3,1,2"))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    175594  "0,3,6"

    ;; These all pass, but slow the unit tests down
    ;; 2578    "1,3,2"
    ;; 3544142 "2,1,3"
    ;; 261214  "1,2,3"
    ;; 6895259 "2,3,1"
    ;; 18      "3,2,1"
    ;; 362     "3,1,2"
    ))

(def answers (delay (day-answers 2020 15)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
