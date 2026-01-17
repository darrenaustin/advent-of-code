;; https://adventofcode.com/2020/day/9
(ns aoc2020.day09
  (:require
   [aoc.day :as d]
   [aoc.util.collection :refer [first-where]]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2020 9))

(defn- pair-sum? [window]
  (let [target (last window)
        nums (set (butlast window))]
    (some #(and (not= % (- target %))
                (nums (- target %)))
          nums)))

(defn- weakness-nums [target nums]
  (let [sums (vec (reductions + 0 nums))
        seen (zipmap sums (range))]
    (some (fn [[i sum]]
            (when-let [start (seen (- sum target))]
              (when (>= (- i start) 2)
                (subvec nums start i))))
          (map-indexed vector sums))))

(defn first-invalid [nums window-size]
  (->> (partition (inc window-size) 1 nums)
       (first-where #(not (pair-sum? %)))
       last))

(defn encryption-weakness [nums window-size]
  (let [invalid (first-invalid nums window-size)
        contiguous (weakness-nums invalid nums)]
    (+ (apply min contiguous) (apply max contiguous))))

(defn part1 [input] (first-invalid (s/ints input) 25))

(defn part2 [input] (encryption-weakness (s/ints input) 25))
