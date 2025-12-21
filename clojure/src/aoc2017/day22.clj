;; https://adventofcode.com/2017/day/22
(ns aoc2017.day22
  (:require
   [aoc.day :as d]
   [aoc.util.pos :as p]
   [aoc.util.sparse-grid :as sg]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2017 22))

(defn parse [input]
  (let [grid (sg/rows->sparse-grid (s/lines input))
        [width height] (sg/size grid)]
    {:grid             grid
     :virus-loc        [(quot width 2) (quot height 2)]
     :virus-dir        p/dir-up
     :blast-infections 0}))

(def turn-for
  {\# p/turn-right
   \W identity
   \. p/turn-left
   \F p/opposite-dir})

(def cell-map
  {\# \.
   \. \#})

(def evolved-cell-map
  {\. \W
   \W \#
   \# \F
   \F \.})

(defn blast [cell-map {:keys [grid virus-loc virus-dir blast-infections]}]
  (let [cell       (get grid virus-loc \.)
        virus-dir' ((turn-for cell) virus-dir)
        cell'      (cell-map cell)]
    {:grid             (assoc grid virus-loc cell')
     :virus-loc        (p/pos+ virus-loc virus-dir')
     :virus-dir        virus-dir'
     :blast-infections (+ blast-infections (if (= \# cell') 1 0))}))

(defn solve [input cell-map iterations]
  (:blast-infections (nth (iterate (partial blast cell-map)
                                   (parse input))
                          iterations)))

(defn part1 [input] (solve input cell-map 10000))

(defn part2 [input] (solve input evolved-cell-map 10000000))
