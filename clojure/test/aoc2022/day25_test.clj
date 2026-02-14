;; Test for aoc2022.day25
(ns aoc2022.day25-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day25 :as d]
   [clojure.test :refer :all]))

(def example
  "1=-0-2
12111
2=0=
21
2=01
111
20012
112
1=-1=
1-12
12
1=
122")

(deftest snafu->int-test
  (are [expected input]
       (= expected (d/snafu->int input))
    0        "0"
    1747     "1=-0-2"
    906      "12111"
    198      "2=0="
    11       "21"
    201      "2=01"
    31       "111"
    1257     "20012"
    32       "112"
    353      "1=-1="
    107      "1-12"
    7        "12"
    3        "1="
    37       "122"))

(deftest int->snafu-test
  (are [expected input]
       (= expected (d/int->snafu input))
    "0"              0
    "1"              1
    "2"              2
    "1="             3
    "1-"             4
    "10"             5
    "11"             6
    "12"             7
    "2="             8
    "2-"             9
    "20"             10
    "1=0"            15
    "1-0"            20
    "1=11-2"         2022
    "1-0---0"        12345
    "1121-1110-1=0"  314159265))

(deftest part1-example
  (is (= "2=-1=0" (d/part1 example))))

(def answers (delay (day-answers 2022 25)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
