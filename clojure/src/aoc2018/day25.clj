;; https://adventofcode.com/2018/day/25
(ns aoc2018.day25
  (:require [aoc.day :as d]
            [aoc.util.math :as m]
            [aoc.util.string :as s]
            [clojure.set :as set]))

(defn input [] (d/day-input 2018 25))

(defn parse [input]
  (partition 4 (s/parse-ints input)))

(defn connected? [star constellation]
  (some #(>= 3 (m/manhattan-distance star %)) constellation))

(defn add-star [constellations star]
  (loop [[group & groups] constellations, connected #{star}, others []]
    (if group
      (if (connected? star group)
        (recur groups (set/union connected group) others)
        (recur groups connected (conj others group)))
      (conj others connected))))

(defn constellations [points]
  (reduce add-star [] points))

(defn part1 [input]
  (count (constellations (parse input))))

(defn part2 [_] "🎄 Got em all! 🎉")
