;; https://adventofcode.com/2025/day/5
 (ns aoc2025.day05
   (:require
    [aoc.day :as d]
    [aoc.util.math :as m]
    [aoc.util.string :as s]
    [clojure.string :as str]))

(defn input [] (d/day-input 2025 5))

(defn parse-ingredients [input]
  (let [[ranges ingredients] (s/split-blocks input)
        ranges (map s/parse-pos-ints (str/split-lines ranges))
        ingredients (s/parse-ints ingredients)]
    [ranges ingredients]))

(defn in-range? [[start end] ingredient]
  (and (<= start ingredient) (>= end ingredient)))

(defn in-ranges? [rs ingredient]
  (some #(in-range? % ingredient) rs))

(defn collapse-ranges [rs]
  (loop [current (first rs) collapsed [] [next & rest] (rest rs)]
    (if (nil? next)
      (conj collapsed current)
      (let [[cs ce] current [ns ne] next]
        (if (<= ns ce)
          (recur [cs (max ce ne)] collapsed rest)
          (recur next (conj collapsed current) rest))))))

(defn range-size [[start end]]
  (inc (- end start)))

(defn part1 [input]
  (let [[ranges ingredients] (parse-ingredients input)]
    (count (filter #(in-ranges? ranges %) ingredients))))

(defn part2 [input]
  (let [[ranges _] (parse-ingredients input)]
    (m/sum (map range-size (collapse-ranges (sort ranges))))))
