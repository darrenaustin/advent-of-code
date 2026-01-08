;; Test for aoc2017.day9
(ns aoc2017.day09-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day09 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    1 "{}"
    6 "{{{}}}"
    5 "{{},{}}"
    16 "{{{},{},{{}}}}"
    1 "{<a>,<a>,<a>,<a>}"
    9 "{{<ab>},{<ab>},{<ab>},{<ab>}}"
    9 "{{<!!>},{<!!>},{<!!>},{<!!>}}"
    3 "{{<a!>},{<a!>},{<a!>},{<ab>}}"))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    0 "<>"
    17 "<random characters>"
    3 "<<<<>"
    2 "<{!>}>"
    0 "<!!>"
    0 "<!!!>>"
    10 "<{o\"i!a,<{i<a>"))

(def answers (delay (day-answers 2017 9)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
