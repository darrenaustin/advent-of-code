(ns aoc.util.matrix)

(defn transpose
  "Transposes a 2D collection (swaps rows and columns).
   If `pad-val` is provided, will pad shorter rows with `pad-val` to match the longest row.
   Without `pad-val`, behaves like `clojure.core/map` and truncates to shortest row.

   Example:
     (transpose [[1 2] [3 4]]) => [[1 3] [2 4]]
     (transpose [[1 2 3] [4]] nil) => [[1 4] [2 nil] [3 nil]]"
  ([coll]
   (apply mapv vector coll))
  ([coll pad-val]
   (let [width (reduce max 0 (map count coll))
         pad (fn [row] (concat row (repeat (- width (count row)) pad-val)))]
     (apply mapv vector (map pad coll)))))

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
