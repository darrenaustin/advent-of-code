;; https://adventofcode.com/2017/day/6
 (ns aoc2017.day06
   (:require
    [aoc.day :as d]
    [aoc.util.math :as m]
    [aoc.util.string :as s]))

(defn input [] (d/day-input 2017 6))

(defn- redistribute [banks]
  (let [[idx blocks] (m/indexed-max banks)
        distribute-to (->> (range (count banks))
                           cycle
                           (drop (inc idx))
                           (take blocks))]
    (reduce #(update %1 %2 inc) (assoc banks idx 0) distribute-to)))

(defn- detect-cycle [coll]
  (reduce
   (fn [[iterations steps] current]
     (if-let [first-seen (iterations current)]
       (reduced {:steps steps
                 :cycle (- steps first-seen)})
       [(assoc iterations current steps) (inc steps)]))
   [{} 0]
   coll))

(defn- redistribute-cycle [input result-fn]
  (->> (s/ints input)
       (iterate redistribute)
       detect-cycle
       result-fn))

(defn part1 [input] (redistribute-cycle input :steps))

(defn part2 [input] (redistribute-cycle input :cycle))
