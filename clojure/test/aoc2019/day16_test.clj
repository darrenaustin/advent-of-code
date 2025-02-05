;; Test for aoc2019.day16
(ns aoc2019.day16-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2019.day16 :as d]
            [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
    (= expected (d/part1 input))
    24176176 "80871224585914546619083218645595"
    73745418 "19617804207202209144916044189917"
    52432133 "69317163492948606335995924319873"))

(deftest part2-examples
  (are [expected input]
    (= expected (d/part2 input))
    84462026 "03036732577212944063491565474664"
    78725270 "02935109699940807407585447034323"
    53553731 "03081770884921959731165446850517"))

(def answers (delay (day-answers 2019 16)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
