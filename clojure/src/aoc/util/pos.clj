(ns aoc.util.pos
  "Utilities for 2D position arithmetic and grid navigation.
   Includes vector addition/subtraction, direction constants, neighbor generation,
   and rotation helpers.")

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
  [x y]
  (cond
    (and (number? x) (sequential? y)) (mapv (partial * x) y)
    (and (sequential? x) (number? y)) (mapv (partial * y) x)
    :else (throw (IllegalArgumentException. "Arguments must be a number and a vector."))))

(defn dot
  "Calculates the dot product of two vectors."
  [x y]
  (reduce + (map * x y)))

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
        :let [rx (- radius ry)]
        n      (cond
                 (= 0 rx ry) [[x y]]
                 (zero? rx) [[x (+ y ry)] [x (- y ry)]]
                 (zero? ry) [[(+ x rx) y] [(- x rx) y]]
                 :else [[(+ x rx) (+ y ry)]
                        [(+ x rx) (- y ry)]
                        [(- x rx) (+ y ry)]
                        [(- x rx) (- y ry)]])]
    n))
