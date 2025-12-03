;; https://adventofcode.com/2025/day/3
(ns aoc2025.day03
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.math :as m]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2025 3))

(defn parse-battery-banks [input]
  (map s/digits (s/parse-ints input)))

(defn max-indexed-value [coll]
  (reduce (fn [[mi mv] [i v]] (if (> v mv) [i v] [mi mv]))
          (c/indexed coll)))

(defn max-power-n-batteries [bank n]
  (if (= n 1)
    (apply max bank)
    (let [[index digit] (max-indexed-value (take (inc (- (count bank) n)) bank))
          rest-num (max-power-n-batteries (subvec bank (inc index)) (dec n))]
      (s/parse-int (str digit rest-num)))))

(defn max-power-from-banks [input num-batteries]
  (m/sum (map #(max-power-n-batteries % num-batteries)
              (parse-battery-banks input))))

(defn part1 [input] (max-power-from-banks input 2))

(defn part2 [input] (max-power-from-banks input 12))
