;; https://adventofcode.com/2020/day/10
(ns aoc2020.day10
  (:require
   [aoc.day :as d]
   [aoc.util.collection :refer [filter-keys]]
   [aoc.util.math :as m :refer [intervals product sum]]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2020 10))

(defn- parse-devices [input]
  (let [adapters (s/ints input)]
    (sort (conj adapters 0 (+ 3 (apply max adapters))))))

(defn part1 [input]
  (product
   (->> (parse-devices input)
        intervals
        frequencies
        (filter-keys #{1 3})
        vals)))

(defn part2 [input]
  (let [devices (parse-devices input)
        counts (reduce
                (fn [cs jolt]
                  (assoc cs jolt
                         (sum (map #(cs (- jolt %) 0) [1 2 3]))))
                {0 1}
                (rest devices))]
    (counts (last devices))))
