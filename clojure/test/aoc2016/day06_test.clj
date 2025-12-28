;; Test for aoc2016.day6
(ns aoc2016.day06-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day06 :as d]
   [clojure.test :refer :all]))

(def example
  "eedadn
drvtee
eandsr
raavrd
atevrs
tsrnev
sdttsa
rasrtv
nssdts
ntnada
svetve
tesnvt
vntsnd
vrdear
dvrsen
enarar")

(deftest part1-example
  (is (= "easter" (d/part1 example))))

(deftest part2-example
  (is (= "advent" (d/part2 example))))

(def answers (delay (day-answers 2016 6)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
