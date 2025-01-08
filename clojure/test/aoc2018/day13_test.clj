;; Test for aoc2018.day13
(ns aoc2018.day13-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day13 :as d]
            [clojure.string :as str]
            [clojure.test :refer :all]))

;; Editor stripped ending spaces on lines,
;; so use "." and convert them to spaces
;; for the solutions.
(def example1
  (str/replace
    "/->-\\........
|...|../----\\
|./-+--+-\\..|
|.|.|..|.v..|
\\-+-/..\\-+--/
..\\------/..." "." " "))

(def example2
  (str/replace
    "/>-<\\..
|...|..
|./<+-\\
|.|.|.v
\\>+</.|
..|...^
..\\<->/" "." " "))

(deftest part1-example
  (is (= "7,3" (d/part1 example1))))

(deftest part2-example
  (is (= "6,4" (d/part2 example2))))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2018 13) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
