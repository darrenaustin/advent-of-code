;; Test for aoc2017.day19
(ns aoc2017.day19-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2017.day19 :as d]
            [clojure.string :as str]
            [clojure.test :refer :all]))

(def example
  ;; Editor stripped ending spaces on lines,
  ;; so use "." and convert them to spaces
  ;; for the solutions.
  (str/replace
    ".....|..........
.....|..+--+....
.....A..|..C....
.F---|----E|--+.
.....|..|..|..D.
.....+B-+..+--+." "." " "))

(deftest part1-example
  (is (= "ABCDEF" (d/part1 example))))

(deftest part2-example
  (is (= 38 (d/part2 example))))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2017 19) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
