(ns aoc.util.vec)

(defn vec+
  ([a] a)
  ([a b] (mapv + a b))
  ([a b & more] (reduce vec+ (mapv + a b) more)))

(defn vec-
  ([[x y]] [(- x) (- y)])
  ([a b] (mapv - a b))
  ([a b & more] (reduce vec- (mapv - a b) more)))

(defn vec-n* [n v] (mapv (partial * n) v))

(def origin [0 0])

(def dir-nw [-1 -1])
(def dir-n  [+0 -1])
(def dir-ne [+1 -1])
(def dir-e  [+1 +0])
(def dir-se [+1 +1])
(def dir-s  [+0 +1])
(def dir-sw [-1 +1])
(def dir-w  [-1 +0])

(def dir-up dir-n)
(def dir-down dir-s)
(def dir-left dir-w)
(def dir-right dir-e)

(def cardinal-dirs
  [dir-nw dir-n dir-ne
   dir-w        dir-e
   dir-sw dir-s dir-se])

(def orthogonal-dirs
  [dir-up dir-right dir-down dir-left])

(def ortho-turn-left
  {dir-up    dir-left
   dir-left  dir-down
   dir-down  dir-right
   dir-right dir-up})

(def ortho-turn-right (into {} (map (comp vec reverse) ortho-turn-left)))

(defn opposite-dir [dir] (vec- dir))

(defn orthogonal-from [[x y]]
  [[x (dec y)] [(inc x) y] [x (inc y)] [(dec x) y]])

(defn cardinal-from [[x y]]
  [[(dec x) (dec y)] [x (dec y)] [(inc x) (dec y)]
   [(dec x) y]                   [(inc x) y]
   [(dec x) (inc y)] [x (inc y)] [(inc x) (inc y)]])
