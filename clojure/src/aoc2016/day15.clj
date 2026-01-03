;; https://adventofcode.com/2016/day/15
(ns aoc2016.day15
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2016 15))

(defn- parse-disc [line]
  (let [[depth positions _ offset] (s/ints line)]
    [depth positions offset]))

(defn- parse-discs [input]
  (mapv parse-disc (s/lines input)))

(defn- open? [discs time]
  (every?
   (fn [[depth positions offset]]
     (zero? (mod (+ time depth offset) positions)))
   discs))

(defn- drop-time [discs]
  (first (filter (partial open? discs) (range))))

(defn part1 [input]
  (drop-time (parse-discs input)))

(defn part2 [input]
  (let [discs (parse-discs input)
        extra-disc [(inc (count discs)) 11 0]]
    (drop-time (conj discs extra-disc))))
