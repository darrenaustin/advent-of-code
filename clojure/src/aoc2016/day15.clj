;; https://adventofcode.com/2016/day/15
(ns aoc2016.day15
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2016 15))

(defn- parse-discs [input]
  (mapv #(let [[depth positions _ offset] (s/ints %)]
           {:depth depth, :positions positions, :offset offset})
        (s/lines input)))

(defn- open? [disc time]
  (let [{:keys [depth positions offset]} disc]
    (zero? (mod (+ time depth offset) positions))))

(defn- drop-time [discs]
  (first (filter (fn [t] (every? #(open? % t) discs)) (range))))

(defn part1 [input]
  (drop-time (parse-discs input)))

(defn part2 [input]
  (let [discs (parse-discs input)
        extra-disc {:depth (inc (count discs))
                    :positions 11
                    :offset 0}]
    (drop-time (conj discs extra-disc))))
