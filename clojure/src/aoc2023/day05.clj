;; https://adventofcode.com/2023/day/5
(ns aoc2023.day05
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2023 5))

(defn- parse-seeds [seed-line]
  (map (fn [s] [s s]) (s/ints seed-line)))

(defn- parse-seed-ranges [seed-line]
  (->> (s/ints seed-line)
       (partition 2)
       (map (fn [[start len]] [start (+ start len -1)]))))

(defn- parse-mappings [block]
  (->> (s/ints block)
       (partition 3)
       (map (fn [[dest src len]]
              [src (+ src len -1) (- dest src)]))))

(defn- clear-translated [ranges]
  (map #(vary-meta % dissoc :translated?) ranges))

(defn- mark-translated [x] (with-meta x {:translated? true}))

(defn- translated? [x] (:translated? (meta x)))

(defn- translate-range [[ms me d :as mapping] [rs re :as rng]]
  (cond
    (translated? rng) (list rng)
    (< re ms)         (list rng)
    (> rs me)         (list rng)
    (< rs ms)         (conj (translate-range mapping [ms re]) [rs (dec ms)])
    (> re me)         (conj (translate-range mapping [rs me]) [(inc me) re])
    :else             (list (mark-translated [(+ rs d) (+ re d)]))))

(defn- translate-ranges [ranges mappings]
  (clear-translated
   (reduce (fn [ranges mapping] (mapcat (partial translate-range mapping) ranges))
           ranges
           mappings)))

(defn- closest-location [input seed-parser]
  (let [[seed-line & map-blocks] (s/blocks input)
        seeds (seed-parser seed-line)
        maps (map parse-mappings map-blocks)]
    (reduce min (map first (reduce translate-ranges seeds maps)))))

(defn part1 [input] (closest-location input parse-seeds))

(defn part2 [input] (closest-location input parse-seed-ranges))
