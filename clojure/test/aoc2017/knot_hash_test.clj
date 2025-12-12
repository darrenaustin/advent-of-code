(ns aoc2017.knot-hash-test
  (:require
   [aoc2017.knot-hash :refer [knot-hash]]
   [clojure.test :refer :all]))

(deftest knot-hash-test
  (are [expected input]
       (= expected (knot-hash input))
    "a2582a3a0e66e6e86e3812dcb672a272" ""
    "33efeb34ea91902bb2f59c9920caa6cd" "AoC 2017"
    "3efbe78a8d82f29979031a4aa0b16a9d" "1,2,3"
    "63960835bcdc130f0b66d7ff4f6a5a8e" "1,2,4"))
