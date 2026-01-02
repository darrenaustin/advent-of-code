;; https://adventofcode.com/2016/day/23
 (ns aoc2016.day23
   (:require
    [aoc.day :as d]
    [aoc2016.assembunny :refer [assembunny]]))

(defn input [] (d/day-input 2016 23))

(defn part1 [input] ((assembunny input {:a 7}) :a))

(defn part2 [input] ((assembunny input {:a 12}) :a))
