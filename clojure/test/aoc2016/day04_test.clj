;; Test for aoc2016.day4
(ns aoc2016.day04-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day04 :as d]
   [clojure.test :refer :all]))

(def example
  "aaaaa-bbb-z-y-x-123[abxyz]
a-b-c-d-e-f-g-h-987[abcde]
not-a-real-room-404[oarel]
totally-real-room-200[decoy]")

(deftest part1-example
  (is (= (+ 123 987 404) (d/part1 example))))

(def answers (delay (day-answers 2016 4)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
