;; https://adventofcode.com/2025/day/12
(ns aoc2025.day12
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2025 12))

(defn parse-presents [input]
  (let [blocks (drop-last (s/split-blocks input))]
    ;; As this we are just being trolled, we only need the number
    ;; of filled spaces for each present
    (mapv (fn [block] (c/count-where #{\#} block)) blocks)))

(defn parse-regions [input]
  (mapv (fn [line]
          (let [[w h & quantities] (s/parse-ints line)]
            [[w h] quantities]))
        (str/split-lines (last (s/split-blocks input)))))

(defn presents-fit? [[[w h] quantities] presents]
  (let [available-space (* w h)
        needed-space (m/sum (map-indexed (fn [i q] (* q (nth presents i))) quantities))
        ;; Do the presents fit if we just stack them next to each other?
        easy-fit-space (m/sum (map (fn [q] (* q 3 3)) quantities))]
    (cond
      (<= easy-fit-space available-space) :yes
      (> needed-space available-space)    :no
      :else                               :maybe)))

(defn part1 [input]
  (let [presents (parse-presents input)
        regions (parse-regions input)
        fits (map #(presents-fit? % presents) regions)]
    (if (zero? (c/count-where #{:maybe} fits))
      ;; No need to go further
      (c/count-where #{:yes} fits)
      (throw (Exception. "Need to sort out non-obvious cases. He wasn't just trolling us!")))))

(defn part2 [_] "🎄 Got em all! 🎉")
