(ns aoc2015.day03-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2015.day03 :as d]
   [clojure.test :refer :all]))

(deftest part1-example
  (are [expected input]
       (= expected (d/part1 input))
    2 ">"
    4 "^>v<"
    2 "^v^v^v^v^v"))

(deftest part2-example
  (are [expected input]
       (= expected (d/part2 input))
    3 "^v"
    3 "^>v<"
    11 "^v^v^v^v^v"))

(def answers (delay (day-answers 2015 3)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
