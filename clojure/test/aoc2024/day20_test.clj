;; Test for aoc2024.day20
(ns aoc2024.day20-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc.util.pos :as p]
   [aoc2024.day20 :as d]
   [clojure.test :refer :all]))

(def example
  "###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#######.#.#.###
#######.#.#...#
#######.#.###.#
###..E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############")

(deftest cheat-saves-examples
  (let [dists (d/distance-map (d/parse-track example))]
    (are [expected cheat-locs]
         (= expected (apply d/cheat-saves dists cheat-locs))
      ;; Part 1 examples
      12 [[7 1] [9 1]]
      20 [[9 7] [11 7]]
      38 [[8 7] [8 9]]
      64 [[7 7] [5 7]]
      ;; Part 2 example
      76 [[1 3] [3 7]])))

(deftest num-cheats-examples1
  (let [cheats (frequencies (d/num-cheats example #(p/diamond-around 2 2 %) 0))]
    (are [expected num]
         (= expected (cheats num))
      14 2
      14 4
      2 6
      4 8
      2 10
      3 12
      1 20
      1 36
      1 38
      1 40
      1 64)))

(deftest num-cheats-examples2
  (let [cheats (frequencies (d/num-cheats example #(p/diamond-around 2 20 %) 50))]
    (are [expected num]
         (= expected (cheats num))
      32 50
      31 52
      29 54
      39 56
      25 58
      23 60
      20 62
      19 64
      12 66
      14 68
      12 70
      22 72
      4 74
      3 76)))

(def answers (delay (day-answers 2024 20)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
