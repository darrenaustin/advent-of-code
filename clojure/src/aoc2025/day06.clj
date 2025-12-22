;; https://adventofcode.com/2025/day/6
 (ns aoc2025.day06
   (:require
    [aoc.day :as d]
    [aoc.util.collection :as c]
    [aoc.util.math :as m]
    [aoc.util.matrix :as mat]
    [aoc.util.string :as s]
    [clojure.string :as str]))

(defn input [] (d/day-input 2025 6))

(defn calculate [operator numbers]
  (apply ({\+ +, \* *} operator) numbers))

(defn part1 [input]
  (let [lines (s/lines input)
        operators (remove #{\space} (last lines))
        numbers (mat/transpose (map s/ints (drop-last lines)))]
    (m/sum (map calculate operators numbers))))

(defn part2 [input]
  (let [lines (s/lines input)
        operators (remove #{\space} (last lines))
        numbers (c/split nil?
                         (map #(s/int (str/join %))
                              (mat/transpose (drop-last lines))))]
    (m/sum (map calculate operators numbers))))
