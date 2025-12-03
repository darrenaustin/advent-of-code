;; https://adventofcode.com/2025/day/3
(ns aoc2025.day03
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2025 3))

(defn parse-battery-banks [input]
  (map s/digits (s/parse-ints input)))

(defn max-joltage [bank batteries]
  (loop [bank bank b batteries max-jolt 0]
    (if (zero? b)
      max-jolt
      (let [options (if (= b 1)
                      bank
                      (subvec bank 0 (inc (- (count bank) b))))
            [max-idx max-battery] (m/indexed-max options)]
        (recur (subvec bank (inc max-idx))
               (dec b)
               (+ (* max-jolt 10) max-battery))))))

(defn max-joltage-from-banks [input num-batteries]
  (m/sum (map #(max-joltage % num-batteries)
              (parse-battery-banks input))))

(defn part1 [input] (max-joltage-from-banks input 2))

(defn part2 [input] (max-joltage-from-banks input 12))
