;; https://adventofcode.com/2018/day/18
(ns aoc2018.day18
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.grid :as g]
   [aoc.util.vec :as v]))

(defn input [] (d/day-input 2018 18))

(defn update-acre [acre neighbors]
  (case acre
    \. (if (<= 3 (c/count-where #{\|} neighbors)) \| \.)
    \| (if (<= 3 (c/count-where #{\#} neighbors)) \# \|)
    \# (if (and (pos? (c/count-where #{\#} neighbors))
                (pos? (c/count-where #{\|} neighbors))) \# \.)))

(defn update-area [area]
  (into {} (for [[l a] area]
             [l (update-acre a (keep area (v/adjacent-to l)))])))

(defn resource-value [area]
  (* (count (g/locs-where area #{\|}))
     (count (g/locs-where area #{\#}))))

(defn part1 [input]
  (->> input
       g/parse-grid
       (iterate update-area)
       (drop 10)
       first
       resource-value))

(defn part2 [input]
  (->> input
       g/parse-grid
       (c/iteration-with-cycle 1000000000 update-area)
       resource-value))
