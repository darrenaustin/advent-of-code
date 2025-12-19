;; https://adventofcode.com/2015/day/17
(ns aoc2015.day17
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2015 17))

(def container-counts
  (memoize
   (fn [sizes target]
     (cond
       (zero? target) {0 1}
       (or (neg? target) (empty? sizes)) {}
       :else (merge-with +
                         (update-keys (container-counts
                                       (rest sizes)
                                       (- target (first sizes)))
                                      inc)
                         (container-counts (rest sizes) target))))))

(defn part1
  ([input] (part1 input 150))
  ([input total]
   (->> (container-counts (s/ints input) total)
        vals
        (apply +))))

(defn part2
  ([input] (part2 input 150))
  ([input total]
   (let [counts (container-counts (s/ints input) total)
         min-containers (apply min (keys counts))]
     (get counts min-containers))))

