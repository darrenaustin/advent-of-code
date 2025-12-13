(ns aoc.util.grid
  (:require
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn parse-grid
  ([input] (parse-grid input identity))
  ([input value-fn]
   (when-not (string? input)
     (throw (Exception. (format "Need a string: %s" input))))
   (let [lines (s/lines input)]
     (into {}
           (for [x (range (count (first lines)))
                 y (range (count lines))]
             [[x y] (value-fn (get-in lines [y x]))])))))

(defn init-grid [locs default]
  (into {} (for [loc locs] [loc default])))

(defn area-locs [[start-x start-y] [end-x end-y]]
  (for [y (range start-y (inc end-y))
        x (range start-x (inc end-x))]
    [x y]))

(defn area-bounds [locs]
  (loop [min-x nil min-y nil max-x nil max-y nil locs locs]
    (if (empty? locs)
      [[min-x min-y] [max-x max-y]]
      (let [[x y] (first locs)]
        (recur (min (or min-x x) x)
               (min (or min-y y) y)
               (max (or max-x x) x)
               (max (or max-y y) y)
               (rest locs))))))

(defn bounds [grid]
  (area-bounds (keys grid)))

(defn in-bounds? [[[min-x min-y] [max-x max-y]] [x y]]
  (and (< min-x x max-x)
       (< min-y y max-y)))

(defn size [grid]
  (let [[[min-x min-y] [max-x max-y]] (bounds grid)]
    [(- max-x min-x) (- max-y min-y)]))

(defn width [grid]
  (let [[[min-x _] [max-x _]] (bounds grid)]
    (- max-x min-x)))

(defn height [grid]
  (let [[[_ min-y] [_ max-y]] (bounds grid)]
    (- max-y min-y)))

(defn top [grid]
  (let [[[_ min-y] _] (bounds grid)]
    min-y))

(defn bottom [grid]
  (let [[_ [_ max-y]] (bounds grid)]
    max-y))

(defn sub-grid [grid bounds]
  (into {} (filter #(in-bounds? bounds (first %))) grid))

(defn grid->str-vec
  ([grid] (grid->str-vec grid \space ""))
  ([grid empty-value separator]
   (if (empty? grid)
     ""
     (let [[[min-x min-y] [max-x max-y]] (bounds grid)]
       (vec (for [y (range min-y (inc max-y))]
              (str/join separator
                        (for [x (range min-x (inc max-x))]
                          (grid [x y] empty-value)))))))))

(defn grid->str
  ([grid] (grid->str grid \space ""))
  ([grid empty-value separator]
   (str/join "\n" (grid->str-vec grid empty-value separator))))

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
