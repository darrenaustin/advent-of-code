;; Test for aoc2017.day4
(ns aoc2017.day04-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day04 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    1 "aa bb cc dd ee"
    0 "aa bb cc dd aa"
    1 "aa bb cc dd aaa"))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    1 "abcde fghij"
    0 "abcde xyz ecdab"
    1 "a ab abc abd abf abj"
    1 "iiii oiii ooii oooi oooo"
    0 "oiii ioii iioi iiio"))

(def answers (delay (day-answers 2017 4)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
