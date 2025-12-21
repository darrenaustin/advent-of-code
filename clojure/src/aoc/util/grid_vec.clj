(ns aoc.util.grid-vec
  "An immutable 2D grid implementation backed by a flat vector.
   Supports efficient random access, updates, and sequence operations.
   Implements standard Clojure collection interfaces (Associative, Counted, Seqable, IFn)."
  (:refer-clojure :exclude [format])
  (:require
   [clojure.string :as str])
  (:import
   [clojure.lang
    Associative
    Counted
    IFn
    IPersistentCollection
    MapEntry
    Seqable]))

(defprotocol Bounded
  "Protocol for 2D spatial bounds and dimensions."
  (bounds [this] "Returns a pair of [[min-x min-y] [max-x max-y]] coordinates.")
  (width [this] "Returns the width (number of columns) of the grid.")
  (height [this] "Returns the height (number of rows) of the grid.")
  (top-left [this] "Returns the [x y] coordinate of the top-left corner.")
  (top-right [this] "Returns the [x y] coordinate of the top-right corner.")
  (bottom-right [this] "Returns the [x y] coordinate of the bottom-right corner.")
  (bottom-left [this] "Returns the [x y] coordinate of the bottom-left corner.")
  (corners [this] "Returns a sequence of the four corner coordinates."))

(definterface IKeyIndexed
  (^long key_index [key]))

(deftype GridVec [cells ^long width ^long height]
  Bounded
  (bounds [_] [[0 0] [(dec width) (dec height)]])
  (width [_] width)
  (height [_] height)
  (top-left [_] [0 0])
  (top-right [_] [(dec width) 0])
  (bottom-right [_] [(dec width) (dec height)])
  (bottom-left [_] [0 (dec height)])
  (corners [_] [[0 0] [(dec width) 0] [(dec width) (dec height)] [0 (dec height)]])

  IKeyIndexed
  (key_index [_ [x y]] (+ (* y width) x))

  IPersistentCollection
  (cons [this o]
    (if (map? o)
      (reduce (fn [g [k v]] (.assoc g k v)) this o)
      (if-let [[k v] (seq o)]
        (.assoc this k v)
        this)))
  (empty [_] (GridVec. [] 0 0))
  (equiv [_ o]
    (and (instance? GridVec o)
         (= width (.width ^GridVec o))
         (= height (.height ^GridVec o))
         (= cells (.cells ^GridVec o))))

  Counted
  (count [_] (count cells))

  Associative
  (containsKey [_ k]
    (let [[x y] k]
      (and (< -1 x width) (< -1 y height))))
  (entryAt [this k]
    (when (.containsKey this k)
      (MapEntry. k (nth cells (.key-index this k)))))
  (assoc [this k v]
    (GridVec. (assoc cells (.key-index this k) v) width height))
  (valAt [this k]
    (.valAt this k nil))
  (valAt [this k default]
    (if (.containsKey this k)
      (nth cells (.key-index this k))
      default))

  Seqable
  (seq [_]
    (map #(MapEntry. %1 %2)
         (for [y (range height) x (range width)] [x y])
         cells))

  IFn
  (invoke [this k] (.valAt this k))
  (invoke [this k default] (.valAt this k default))

  Object
  (toString [this]
    (str/join "\n" (for [y (range height)]
                     (str/join (for [x (range width)] (this [x y]))))))
  (equals [this o] (.equiv this o))
  (hashCode [_] (hash [cells width height])))

(defn make-grid-vec
  "Creates a new GridVec of the specified dimensions, initialized with `default-value` (default nil)."
  ([width height] (make-grid-vec width height nil))
  ([width height default-value]
   (GridVec. (vec (repeat (* width height) default-value)) width height)))

(defn rows->grid-vec
  "Creates a GridVec from a sequence of rows (strings or sequences).
   Optionally takes a `value-fn` that will be mapped over each element
   before it is put into the grid. Assumes all rows have the same length."
  ([lines] (rows->grid-vec lines identity))
  ([lines value-fn]
   (let [height (count lines)
         width (count (first lines))]
     (assert (every? #(= width (count %)) lines))
     (GridVec. (vec (mapcat #(map value-fn %) lines)) width height))))

(defn update-grid
  "Returns a new GridVec with `entry-fn` applied to every cell.
   `entry-fn` receives a MapEntry of `[[x y] value]` and should return the new value
   for the entry."
  [grid ^GridVec entry-fn]
  (GridVec. (vec (map entry-fn grid)) (width grid) (height grid)))

(defn format-grid
  "Formats the grid as a string.
   Options:
   - `:value-fn`: Function to transform cell values before printing (default: identity).
   - `:col-sep`: Separator string between columns (default: \"\").
   - `:row-sep`: Separator string between rows (default: \"\\n\")."
  [grid ^GridVec & {:keys [value-fn col-sep row-sep] :as _options}]
  (let [value-fn (or value-fn identity)
        col-sep (or col-sep "")
        row-sep (or row-sep "\n")]
    (str/join row-sep
              (for [y (range (height grid))]
                (str/join col-sep
                          (for [x (range (width grid))] (value-fn (grid [x y]))))))))
