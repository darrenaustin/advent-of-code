;; https://adventofcode.com/2015/day/14
(ns aoc2015.day14
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2015 14))

(defn distance-at [[speed fly rest] time]
  (let [cycle (+ fly rest)]
    (* speed (+ (* fly (quot time cycle))
                (min fly (mod time cycle))))))

(defn score-at [reindeers second]
  (let [dists (map #(distance-at % second) reindeers)
        best  (apply max dists)]
    (keep-indexed (fn [i d] (when (= d best) i)) dists)))

(defn part1
  ([input] (part1 input 2503))
  ([input duration]
   (->> (s/lines input)
        (map s/ints)
        (map #(distance-at % duration))
        (apply max))))

(defn part2
  ([input] (part2 input 2503))
  ([input duration]
   (let [reindeers (map s/ints (s/lines input))]
     (->> (range 1 (inc duration))
          (mapcat (partial score-at reindeers))
          frequencies
          vals
          (apply max)))))
