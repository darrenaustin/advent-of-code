;; https://adventofcode.com/2025/day/12
(ns aoc2025.day12
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.math :as m]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2025 12))

(defn parse [input]
  (let [blocks (s/blocks input)
        ;; As this we are just being trolled, we only need the number
        ;; of filled spaces for each present
        presents (mapv (fn [p] (c/count-where #{\#} p)) (pop blocks))
        regions (mapv (fn [line]
                        (let [[w h & quantities] (s/ints line)]
                          [[w h] quantities]))
                      (s/lines (peek blocks)))]
    [presents regions]))

(defn presents-fit? [[[w h] quantities] presents]
  (let [available-space (* w h)
        needed-space (m/sum (map * quantities presents))
        ;; Do the presents fit if we just stack them next to each other?
        easy-fit-space (m/sum (map #(* % 3 3) quantities))]
    (cond
      (<= easy-fit-space available-space) :yes
      (> needed-space available-space)    :no
      :else                               :maybe)))

(defn part1 [input]
  (let [[presents regions] (parse input)
        fit-groups (group-by identity (map #(presents-fit? % presents) regions))
        counts (reduce (fn [m k] (update m k count)) fit-groups [:yes :no :maybe])]
    (if (zero? (:maybe counts))
      ;; No need to go further
      (:yes counts)
      (throw (Exception. "Need to sort out non-obvious cases. He wasn't just trolling us!")))))

(defn part2 [_] "🎄 Got em all! 🎉")
