;; Test for aoc2017.day21
(ns aoc2017.day21-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2017.day21 :as d]
            [clojure.test :refer :all]))

(def example
  "../.# => ##./#../...
.#./..#/### => #..#/..../..../#..#")

(deftest example-test
  (is (= 12 (d/on-after-iterations example 2))))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2017 21) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
