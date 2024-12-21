;; Test for aoc2024.day21
(ns aoc2024.day21-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day21 :as d]
            [clojure.test :refer :all]))

(def example
  "029A
980A
179A
456A
379A")

(deftest part1-example
  (is (= 126384 (d/part1 example))))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2024 21) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
