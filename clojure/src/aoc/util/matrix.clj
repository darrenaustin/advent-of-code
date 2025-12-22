(ns aoc.util.matrix)

(defn transpose
  "Transposes a 2D collection (swaps rows and columns).
   Example: (transpose [[1 2] [3 4]]) => [[1 3] [2 4]]"
  [coll]
  (apply mapv vector coll))

(defn flip-horizontal
  "Flips a 2D collection horizontally (reverses each row).
   Example: (flip-horizontal [[1 2] [3 4]]) => [[2 1] [4 3]]"
  [coll]
  (mapv (comp vec reverse) coll))

(defn rotate-right
  "Rotates a 2D collection 90 degrees clockwise.
   Example: (rotate-right [[1 2] [3 4]]) => [[3 1] [4 2]]"
  [coll]
  ((comp flip-horizontal transpose) coll))
