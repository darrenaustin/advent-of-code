;; Test for aoc2019.day12
(ns aoc2019.day12-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2019.day12 :as d]
   [clojure.test :refer :all]))

(def example1
  "<x=-1, y=0, z=2>
<x=2, y=-10, z=-7>
<x=4, y=-8, z=8>
<x=3, y=5, z=-1>")

(def example2
  "<x=-8, y=-10, z=0>
<x=5, y=5, z=10>
<x=2, y=-7, z=3>
<x=9, y=-8, z=-3>")

(deftest part1-example
  (is (= 179 (d/system-energy-for example1 10)))
  (is (= 1940 (d/system-energy-for example2 100))))

(deftest part2-example
  (is (= 2772 (d/part2 example1)))
  (is (= 4686774924 (d/part2 example2))))

(def answers (delay (day-answers 2019 12)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
