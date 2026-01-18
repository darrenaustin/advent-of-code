;; https://adventofcode.com/2020/day/12
(ns aoc2020.day12
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.pos :as p]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2020 12))

(defn- parse-action [s] [(first s) (s/int s)])

(defn- turn [dir angle]
  (let [turns (mod (/ angle 90) 4)]
    (nth (iterate (fn [[x y]] [(- y) x]) dir) turns)))

(defn- perform-action [move-key]
  (fn [ship [action mag]]
    (case action
      \L (update ship :dir turn (- mag))
      \R (update ship :dir turn mag)
      \F (update ship :pos p/pos+ (p/pos* mag (:dir ship)))
      (update ship move-key p/pos+ (p/pos* mag (p/dir->offset action))))))

(defn- navigate [input move-key dir]
  (->> (s/lines input)
       (map parse-action)
       (reduce (perform-action move-key) {:pos p/origin, :dir dir})
       :pos
       m/manhattan-distance))

(defn part1 [input] (navigate input :pos p/dir-e))

(defn part2 [input] (navigate input :dir [10 -1]))
