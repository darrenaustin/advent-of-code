;; https://adventofcode.com/2025/day/11
(ns aoc2025.day11
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.memoize :refer [letfn-mem]]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2025 11))

(defn parse-devices [input]
  (into {} (map #(let [[name & connections] (re-seq #"\w+" %)]
                   [name connections])
                (s/lines input))))

(defn count-paths
  ([start finish devices] (count-paths start finish nil devices))
  ([start finish mandatory devices]
   (letfn-mem
    [num-paths (fn [start seen]
                 (if (= start finish)
                   (if mandatory
                     (if (= seen mandatory) 1 0)
                     1)
                   (let [seen' (when mandatory
                                 (if (mandatory start) (conj seen start) seen))
                         result (mapv #(num-paths % seen') (devices start))]
                     (m/sum result))))]
    (num-paths start (when mandatory #{})))))

(defn part1 [input]
  (count-paths "you" "out" (parse-devices input)))

(defn part2 [input]
  (count-paths "svr" "out" #{"dac" "fft"} (parse-devices input)))
