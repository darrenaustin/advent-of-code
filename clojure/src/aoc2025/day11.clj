;; https://adventofcode.com/2025/day/11
(ns aoc2025.day11
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.memoize :refer [let-memoized]]
   [clojure.string :as str]))

(defn input [] (d/day-input 2025 11))

(defn parse-devices [input]
  (into {} (map #(let [[name & connections] (re-seq #"\w+" %)]
                   [name connections])
                (str/split-lines input))))

(defn count-paths [start finish devices]
  (let-memoized
   [num-paths (fn [start]
                (if (= start finish)
                  1
                  (let [result (mapv num-paths (devices start))]
                    (m/sum result))))]
   (num-paths start)))

(defn count-paths-passing-through [start finish points-of-interest devices]
  (let-memoized
   [num-paths (fn [start seen]
                (if (= start finish)
                  (if (= seen points-of-interest) 1 0)
                  (let [seen' (if (points-of-interest start) (conj seen start) seen)
                        result (mapv #(num-paths % seen') (devices start))]
                    (m/sum result))))]
   (num-paths start #{})))

(defn part1 [input]
  (count-paths "you" "out" (parse-devices input)))

(defn part2 [input]
  (count-paths-passing-through "svr" "out" #{"dac" "fft"} (parse-devices input)))
