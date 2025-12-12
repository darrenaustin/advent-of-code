;; Test for aoc2018.day15
(ns aoc2018.day15-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2018.day15 :as d]
   [clojure.string :as str]
   [clojure.test :refer :all]))

(def example1
  (str/join
   "\n"
   ["#######"
    "#.G...#"
    "#...EG#"
    "#.#.#G#"
    "#..G#E#"
    "#.....#"
    "#######"]))

(def example2
  (str/join
   "\n"
   ["#######"
    "#G..#E#"
    "#E#E.E#"
    "#G.##.#"
    "#...#E#"
    "#...E.#"
    "#######"]))

(def example3
  (str/join
   "\n"
   ["#######"
    "#E..EG#"
    "#.#G.E#"
    "#E.##E#"
    "#G..#.#"
    "#..E#.#"
    "#######"]))

(def example4
  (str/join
   "\n"
   ["#######"
    "#E.G#.#"
    "#.#G..#"
    "#G.#.G#"
    "#G..#.#"
    "#...E.#"
    "#######"]))

(def example5
  (str/join
   "\n"
   ["#######"
    "#.E...#"
    "#.#..G#"
    "#.###.#"
    "#E#G#G#"
    "#...#G#"
    "#######"]))

(def example6
  (str/join
   "\n"
   ["#########"
    "#G......#"
    "#.E.#...#"
    "#..##..G#"
    "#...##..#"
    "#...#...#"
    "#.G...G.#"
    "#.....G.#"
    "#########"]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    27730 example1
    36334 example2
    39514 example3
    27755 example4
    28944 example5
    18740 example6))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    4988 example1
    31284 example3
    3478 example4
    6474 example5
    1140 example6))

(def answers (delay (day-answers 2018 15)))
(def input (delay (d/input)))

(deftest ^:slow part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
