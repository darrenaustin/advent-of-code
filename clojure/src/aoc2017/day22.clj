;; https://adventofcode.com/2017/day/22
(ns aoc2017.day22
  (:require [aoc.day :as d]
            [aoc.util.grid :as g]
            [aoc.util.vec :as v]))

(defn input [] (d/day-input 2017 22))

(defn parse [input]
  (let [grid (g/parse-grid input)
        [width height] (g/size grid)]
    {:grid             grid
     :virus-loc        [(quot width 2) (quot height 2)]
     :virus-dir        v/dir-up
     :blast-infections 0}))

(def turn-for
  {\# v/ortho-turn-right
   \W identity
   \. v/ortho-turn-left
   \F v/opposite-dir})

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
     :virus-loc        (v/vec+ virus-loc virus-dir')
     :virus-dir        virus-dir'
     :blast-infections (+ blast-infections (if (#{\#} cell') 1 0))}))

(defn solve [input cell-map iterations]
  (:blast-infections (nth (iterate (partial blast cell-map)
                                   (parse input))
                          iterations)))

(defn part1 [input] (solve input cell-map 10000))

(defn part2 [input] (solve input evolved-cell-map 10000000))
