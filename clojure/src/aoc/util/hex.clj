(ns aoc.util.hex
  "Hexagonal grid utilities using Cube coordinates (x, y, z).
   Constraint: x + y + z = 0.
   Reference: https://www.redblobgames.com/grids/hexagons/#coordinates-cube"
  (:require
   [aoc.util.math :as m]))

(def origin [0 0 0])

(def dirs
  {"n"  [+1, +0, -1]
   "ne" [+0, +1, -1]
   "se" [-1, +1, +0]
   "s"  [-1, +0, +1]
   "sw" [+0, -1, +1]
   "nw" [+1, -1, +0]})

(defn distance
  ([x] (distance origin x))
  ([x y] (/ (m/manhattan-distance x y) 2)))
