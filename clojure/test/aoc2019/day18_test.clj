;; Test for aoc2019.day18
(ns aoc2019.day18-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2019.day18 :as d]
   [clojure.test :refer :all]))

(def example1
  "#########
#b.A.@.a#
#########")

(def example2
  "########################
#f.D.E.e.C.b.A.@.a.B.c.#
######################.#
#d.....................#
########################")

(def example3
  "########################
#...............b.C.D.f#
#.######################
#.....@.a.B.c.d.A.e.F.g#
########################")

(def example4
  "#################
#i.G..c...e..H.p#
########.########
#j.A..b...f..D.o#
########@########
#k.E..a...g..B.n#
########.########
#l.F..d...h..C.m#
#################")

(def example5
  "########################
#@..............ac.GI.b#
###d#e#f################
###A#B#C################
###g#h#i################
########################")

(def example6
  "#######
#a.#Cd#
##...##
##.@.##
##...##
#cB#Ab#
#######")

(def example7
  "###############
#d.ABC.#.....a#
######...######
######.@.######
######...######
#b.....#.....c#
###############")

(def example8
  "#############
#DcBa.#.GhKl#
#.###...#I###
#e#d#.@.#j#k#
###C#...###J#
#fEbA.#.FgHi#
#############")

(def example9
  "#############
#g#f.D#..h#l#
#F###e#E###.#
#dCba...BcIJ#
#####.@.#####
#nK.L...G...#
#M###N#H###.#
#o#m..#i#jk.#
#############")

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    8 example1
    86 example2
    132 example3
    136 example4
    81 example5))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    8 example6
    24 example7
    32 example8
    72 example9))

(def answers (delay (day-answers 2019 18)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
