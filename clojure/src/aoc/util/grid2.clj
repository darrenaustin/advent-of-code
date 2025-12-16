(ns aoc.util.grid2
  (:require
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn make-grid
  ([width height] (make-grid width height nil))
  ([width height fill-value] (vec (repeat height (vec (repeat width fill-value))))))

(defn map-grid [grid f]
  (vec (map-indexed (fn [y row]
                      (vec (map-indexed (fn [x cell]
                                          (f [x y] cell))
                                        row)))
                    grid)))

(defn width [grid] (count (first grid)))

(defn height [grid] (count grid))

(defn col [grid x] (map #(nth % x) grid))

(defn row [grid y] (nth grid y))

(defn top-row [grid] (row grid 0))

(defn bottom-row [grid] (row grid (dec (height grid))))

(defn left-column [grid] (col grid 0))

(defn right-column [grid] (col grid (dec (width grid))))

(defn in-grid? [grid [x y]]
  (and (< -1 x (width grid))
       (< -1 y (height grid))))

(defn cell
  ([grid [x y]] (nth (nth grid y) x))
  ([grid [x y] empty] (or (nth (nth grid y) x) empty)))

(defn set-cell [grid [x y] value] (assoc-in grid [y x] value))

(defn parse-grid
  ([input] (parse-grid input identity))
  ([input value-fn]
   (when-not (or (string? input) (coll? input))
     (throw (Exception. (format "Need a string or collection: %s" input))))
   (let [lines (if (string? input) (s/lines input) input)]
     (mapv #(vec (map value-fn %)) lines))))

(defn grid->str-vec
  ([grid] (grid->str-vec grid \space ""))
  ([grid empty-value separator]
   (if (empty? grid)
     ""
     (vec (for [y (range (height grid))]
            (str/join separator
                      (for [x (range (width grid))]
                        (or (cell grid [x y]) empty-value))))))))

#_{:clojure-lsp/ignore [:clojure-lsp/unused-public-var]}
(defn grid->str
  ([grid] (grid->str grid \space ""))
  ([grid empty-value separator]
   (str/join "\n" (grid->str-vec grid empty-value separator))))

(defn rotate-clockwise [grid]
  (map-grid grid (fn [[x y] _]
                   (cell grid [y (- (width grid) x 1)]))))

(defn flip-horizontal [grid]
  (mapv #(vec (reverse %)) grid))

(defn flip-vertical [grid]
  (vec (reverse grid)))

(defn sub-grid [grid top-left bottom-right]
  (let [[x1 y1] top-left
        [x2 y2] bottom-right]
    (vec (for [y (range y1 (inc y2))]
           (subvec (nth grid y) x1 (inc x2))))))

(defn set-sub-grid [grid top-left sub-grid]
  (let [[start-x start-y] top-left
        sub-height (height sub-grid)
        sub-width  (width sub-grid)]
    (reduce (fn [g y]
              (reduce (fn [g2 x]
                        (set-cell g2 [(+ start-x x) (+ start-y y)] (cell sub-grid [x y])))
                      g
                      (range sub-width)))
            grid
            (range sub-height))))

(defn locations-where [grid predicate]
  (for [y (range (height grid))
        x (range (width grid))
        :when (predicate (cell grid [x y]))]
    [x y]))
