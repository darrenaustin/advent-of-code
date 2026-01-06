;; https://adventofcode.com/2017/day/2
 (ns aoc2017.day02
   (:require
    [aoc.day :as d]
    [aoc.util.collection :as c]
    [aoc.util.math :as m]
    [aoc.util.string :as s]))

(defn input [] (d/day-input 2017 2))

(defn max-diff [row]
  (- (apply max row) (apply min row)))

(defn even-div [row]
  (first (keep (fn [[a b]]
                 (cond
                   (m/div? a b) (/ a b)
                   (m/div? b a) (/ b a)))
               (c/pairs row))))

(defn checksum [input row-fn]
  (->> (s/lines input)
       (map s/ints)
       (map row-fn)
       (reduce +)))

(defn part1 [input] (checksum input max-diff))

(defn part2 [input] (checksum input even-div))
