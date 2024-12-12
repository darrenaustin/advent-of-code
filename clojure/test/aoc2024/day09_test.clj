(ns aoc2024.day09-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day09 :as d]
            [clojure.test :refer :all]))

(def example-input
  "2333133121414131402")

(deftest part1-example
  (is (= 1928 (d/part1 example-input))))

(deftest part2-example
  (is (= 2858 (d/part2 example-input))))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2024 9)]
    (is (= answer1 (d/part1 d/input)))
    (is (= answer2 (d/part2 d/input)))))
