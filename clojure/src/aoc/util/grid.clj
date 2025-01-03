(ns aoc.util.grid
  (:require [clojure.string :as str]))

(def origin [0 0])

(def dir-nw [-1 -1])
(def dir-n [0 -1])
(def dir-ne [1 -1])
(def dir-e [1 0])
(def dir-se [1 1])
(def dir-s [0 1])
(def dir-sw [-1 1])
(def dir-w [-1 0])

(def dir-up dir-n)
(def dir-down dir-s)
(def dir-left dir-w)
(def dir-right dir-e)

(def cardinal-dirs
  [dir-nw dir-n dir-ne
   dir-w dir-e
   dir-sw dir-s dir-se])

(def orthogonal-dirs
  [dir-up dir-right dir-down dir-left])

(def origin3 [0 0 0])

(defn vec+
  ([a] a)
  ([a b] (mapv + a b))
  ([a b & more] (reduce vec+ (mapv + a b) more)))

(defn vec-
  ([[x y]] [(- x) (- y)])
  ([a b] (mapv - a b))
  ([a b & more] (reduce vec- (mapv - a b) more)))

(defn vec-n* [n v] (mapv (partial * n) v))

(defn orthogonal-from [[x y]]
  [[x (dec y)] [(inc x) y] [x (inc y)] [(dec x) y]])

(defn cardinal-from [[x y]]
  [[(dec x) (dec y)] [x (dec y)] [(inc x) (dec y)]
   [(dec x) y] [(inc x) y]
   [(dec x) (inc y)] [x (inc y)] [(inc x) (inc y)]])

(defn parse-grid
  ([input] (parse-grid input identity))
  ([input value-fn]
   (when-not (string? input)
     (throw (Exception. (format "Need a string: %s" input))))
   (let [lines (str/split-lines input)]
     (into {}
           (for [x (range (count (first lines)))
                 y (range (count lines))]
             [[x y] (value-fn (get-in lines [y x]))])))))

(defn init-grid [locs default]
  (into {} (for [loc locs] [loc default])))

(defn area-bounds [locs]
  (reduce (fn [[min-loc max-loc] loc]
            [(map min min-loc loc) (map max max-loc loc)])
          [[Integer/MAX_VALUE Integer/MAX_VALUE]
           [Integer/MIN_VALUE Integer/MIN_VALUE]]
          locs))

(defn bounds [grid]
  (area-bounds (keys grid)))

(defn in-bounds? [[[min-x min-y] [max-x max-y]] [x y]]
  (and (< min-x x max-x)
       (< min-y y max-y)))

(defn sub-grid [grid bounds]
  (into {} (filter #(in-bounds? bounds (first %))) grid))

(defn grid->str-vec
  ([grid] (grid->str-vec grid \space))
  ([grid empty-value]
   (let [[[min-x min-y] [max-x max-y]] (bounds grid)]
     (vec (for [y (range min-y (inc max-y))]
            (str/join (for [x (range min-x (inc max-x))]
                        (grid [x y] empty-value))))))))

(defn grid->str
  ([grid]
   (str/join "\n" (grid->str-vec grid)))
  ([grid empty-value]
   (str/join "\n" (grid->str-vec grid empty-value))))

(defn locs-where [grid pred]
  (map key (filter #(pred (val %)) grid)))

(defn loc-where [grid pred]
  (first (locs-where grid pred)))

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
