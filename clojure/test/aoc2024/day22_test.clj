;; Test for aoc2024.day22
(ns aoc2024.day22-test
  (:require
   [aoc.day :refer [day-answers]]
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

(def answers (delay (day-answers 2024 22)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
