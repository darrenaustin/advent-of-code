(ns aoc.util.pos
  "Utilities for 2D position arithmetic and grid navigation.
   Includes vector addition/subtraction, direction constants, neighbor generation,
   and rotation helpers."
  (:require
   [aoc.util.math :as m]
   [clojure.math :as math]))

(defn pos+
  "Adds two or more position vectors."
  ([a] a)
  ([a b] (mapv + a b))
  ([a b & more] (reduce pos+ (mapv + a b) more)))

(defn pos-
  "Subtracts position vectors or negates one."
  ([[x y]] [(- x) (- y)])
  ([a b] (mapv - a b))
  ([a b & more] (reduce pos- (mapv - a b) more)))

(defn pos*
  "Multiplies a position vector by a scalar.
   Supports commutative multiplication: (pos* n v) or (pos* v n)."
  [a b]
  (cond
    (and (number? a) (sequential? b)) (mapv (partial * a) b)
    (and (sequential? a) (number? b)) (mapv (partial * b) a)
    :else (throw (IllegalArgumentException. "Arguments must be a number and a vector."))))

(defn unit-step?
  "Returns true if the position vector's components are all within {-1, 0, 1}."
  [p]
  (every? #(<= -1 % 1) p))

(defn sign
  "Returns a new position vector where each component is clamped to -1, 0, or 1."
  [p]
  (mapv m/sign p))

(defn dot
  "Calculates the dot product of two vectors."
  [x y]
  (reduce + (map * x y)))

(defn theta [[x y]] (math/atan2 y x))

(defn angle [p1 p2]
  (mod (math/to-degrees (theta (pos- p2 p1))) 360.0))

(defn distance-sq [[x1 y1] [x2 y2]]
  (let [dx (- x2 x1)
        dy (- y2 y1)]
    (+ (* dx dx) (* dy dy))))

#_{:clojure-lsp/ignore [:clojure-lsp/unused-public-var]}
(defn distance [p1 p2]
  (math/sqrt (distance-sq p1 p2)))

(def origin [0 0])

(def dir-nw [-1 -1])
(def dir-n  [+0 -1])
(def dir-ne [+1 -1])
(def dir-e  [+1 +0])
(def dir-se [+1 +1])
(def dir-s  [+0 +1])
(def dir-sw [-1 +1])
(def dir-w  [-1 +0])

(def dir-up   dir-n)
(def dir-down dir-s)
(def dir-left dir-w)
(def dir-right dir-e)

(def dir->offset
  "Common letter mappings to direction offsets."
  {\U dir-up
   \D dir-down
   \L dir-left
   \R dir-right

   \N dir-n
   \S dir-s
   \E dir-e
   \W dir-w

   \^ dir-up
   \v dir-down
   \< dir-left
   \> dir-right})

(def orthogonal-dirs
  "List of orthogonal direction vectors (up, right, down, left)."
  [dir-up dir-right dir-down dir-left])

(defn orthogonal-to
  "Returns the 4 orthogonal neighbors of a position `[x y]`."
  [[x y]]
  [[x (dec y)] [(inc x) y] [x (inc y)] [(dec x) y]])

(def diagonal-dirs
  "List of diagonal direction vectors."
  [dir-ne dir-se dir-sw dir-nw])

(defn diagonal-to
  "Returns the 4 diagonal neighbors of a position `[x y]`."
  [[x y]]
  [[(dec x) (dec y)] [(inc x) (dec y)]
   [(dec x) (inc y)] [(inc x) (inc y)]])

(def adjacent-dirs
  "List of all 8 adjacent direction vectors."
  [dir-nw dir-n dir-ne
   dir-w        dir-e
   dir-sw dir-s dir-se])

(defn adjacent-to
  "Returns the 8 adjacent neighbors of a position `[x y]`."
  [[x y]]
  [[(dec x) (dec y)] [x (dec y)] [(inc x) (dec y)]
   [(dec x) y]                   [(inc x) y]
   [(dec x) (inc y)] [x (inc y)] [(inc x) (inc y)]])

(defn opposite-dir
  "Returns the opposite direction vector."
  [dir]
  (pos- dir))

(def turn-left
  "Map for turning left 90 degrees."
  {dir-up    dir-left
   dir-left  dir-down
   dir-down  dir-right
   dir-right dir-up})

(def turn-right
  "Map for turning right 90 degrees."
  (into {} (map (comp vec reverse) turn-left)))

(defn region-pos [[start-x start-y] [end-x end-y]]
  (for [y (range start-y (inc end-y))
        x (range start-x (inc end-x))]
    [x y]))

(defn pos-bounds [positions]
  (let [[xs ys] (apply mapv vector positions)]
    [[(apply min xs) (apply min ys)]
     [(apply max xs) (apply max ys)]]))

(defn in-region? [[[min-x min-y] [max-x max-y]] [x y]]
  (and (< min-x x max-x) (< min-y y max-y)))

(defn diamond-around [min-radius max-radius [x y]]
  (for [radius (range min-radius (inc max-radius))
        ry     (range (inc radius))
        :let   [rx (- radius ry)]
        n      (cond
                 (= 0 rx ry) [[x y]]
                 (zero? rx) [[x (+ y ry)] [x (- y ry)]]
                 (zero? ry) [[(+ x rx) y] [(- x rx) y]]
                 :else [[(+ x rx) (+ y ry)]
                        [(+ x rx) (- y ry)]
                        [(- x rx) (+ y ry)]
                        [(- x rx) (- y ry)]])]
    n))
