;; Test for aoc2024.day22
(ns aoc2024.day22-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day22 :as d]
            [clojure.test :refer :all]))

(def example
  "1
10
100
2024")

(deftest secret-number
  (is (= [15887950
          16495136
          527345
          704524
          1553684
          12683156
          11100544
          12249484
          7753432
          5908254] (take 10 (d/secret-nums 123)))))

(deftest part1-example
  (is (= 37327623 (d/part1 example))))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2024 22) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
