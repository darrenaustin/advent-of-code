;; https://adventofcode.com/2025/day/5
 (ns aoc2025.day05
   (:require
    [aoc.day :as d]
    [aoc.util.math :as m]
    [aoc.util.string :as s]))

(defn input [] (d/day-input 2025 5))

(defn parse-ingredients [input]
  (let [[ranges ingredients] (s/blocks input)
        ranges (map s/pos-ints (s/lines ranges))
        ingredients (s/ints ingredients)]
    [ranges ingredients]))

(defn range-size [[start end]]
  (inc (- end start)))

(defn in-ranges? [ranges ingredient]
  (some (fn [[start end]] (<= start ingredient end)) ranges))

(defn collapse-ranges [ranges]
  (loop [current (first ranges) collapsed [] [next & rest] (rest ranges)]
    (if (nil? next)
      (conj collapsed current)
      (let [[cs ce] current [ns ne] next]
        (if (<= ns ce)
          (recur [cs (max ce ne)] collapsed rest)
          (recur next (conj collapsed current) rest))))))

(defn part1 [input]
  (let [[ranges ingredients] (parse-ingredients input)]
    (count (filter #(in-ranges? ranges %) ingredients))))

(defn part2 [input]
  (let [[ranges _] (parse-ingredients input)]
    (m/sum (map range-size (collapse-ranges (sort ranges))))))
