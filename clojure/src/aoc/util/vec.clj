(ns aoc.util.vec)

(defn vec+
  "Adds two or more 2D vectors component-wise.

  Examples:
    (vec+ [1 2] [3 4]) => [4 6]
    (vec+ [1 2] [3 4] [5 6]) => [9 12]"
  ([a] a)
  ([a b] (mapv + a b))
  ([a b & more] (reduce vec+ (mapv + a b) more)))

(defn vec-
  "Subtracts vectors component-wise, or negates a single vector.

  Examples:
    (vec- [5 3]) => [-5 -3]
    (vec- [5 3] [2 1]) => [3 2]
    (vec- [10 8] [3 2] [1 1]) => [6 5]"
  ([[x y]] [(- x) (- y)])
  ([a b] (mapv - a b))
  ([a b & more] (reduce vec- (mapv - a b) more)))

(defn vec-n*
  "Multiplies a vector by a scalar.

  Example:
    (vec-n* 3 [2 5]) => [6 15]"
  [n v]
  (mapv (partial * n) v))

(defn opposite-dir
  "Returns the opposite direction vector.

  Example:
    (opposite-dir dir-up) => dir-down
    (opposite-dir [1 2]) => [-1 -2]"
  [dir]
  (vec- dir))

(def origin
  "The origin point [0 0]."
  [0 0])

;; Cardinal and intercardinal direction vectors
;; Using grid coordinates where y increases downward
(def dir-nw [-1 -1])
(def dir-n  [+0 -1])
(def dir-ne [+1 -1])
(def dir-e  [+1 +0])
(def dir-se [+1 +1])
(def dir-s  [+0 +1])
(def dir-sw [-1 +1])
(def dir-w  [-1 +0])

(def dir-up    dir-n)
(def dir-down  dir-s)
(def dir-left  dir-w)
(def dir-right dir-e)

(def adjacent-dirs
  "All 8 direction vectors including diagonals (NW, N, NE, W, E, SW, S, SE)."
  [dir-nw dir-n dir-ne
   dir-w        dir-e
   dir-sw dir-s dir-se])

(def orthogonal-dirs
  "The 4 orthogonal direction vectors (up, right, down, left)."
  [dir-up dir-right dir-down dir-left])

(defn adjacent-to
  "Returns all 8 adjacent positions (including diagonals) for a given position.

  Example:
    (adjacent-to [5 5]) => [[4 4] [5 4] [6 4] [4 5] [6 5] [4 6] [5 6] [6 6]]"
  [[x y]]
  [[(dec x) (dec y)] [x (dec y)] [(inc x) (dec y)]
   [(dec x) y]                   [(inc x) y]
   [(dec x) (inc y)] [x (inc y)] [(inc x) (inc y)]])

(defn orthogonal-to
  "Returns the 4 orthogonally adjacent positions (no diagonals) for a given position.
  Returns positions in the order: up, right, down, left.

  Example:
    (ortho-adjacent-to [5 5]) => [[5 4] [6 5] [5 6] [4 5]]"
  [[x y]]
  [[x (dec y)] [(inc x) y] [x (inc y)] [(dec x) y]])

(def ortho-turn-left
  "Map of orthogonal directions to their 90-degree counter-clockwise turn.

  Example:
    (ortho-turn-left dir-up) => dir-left"
  {dir-up    dir-left
   dir-left  dir-down
   dir-down  dir-right
   dir-right dir-up})

(def ortho-turn-right
  "Map of orthogonal directions to their 90-degree clockwise turn.

  Example:
    (ortho-turn-right dir-up) => dir-right"
  (into {} (map (comp vec reverse) ortho-turn-left)))
