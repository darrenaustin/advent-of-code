(ns aoc.util.grid-vec
  "An immutable 2D grid implementation backed by a flat vector.
   Supports efficient random access, updates, and sequence operations.
   Implements standard Clojure collection interfaces (Associative, Counted, Seqable, IFn).
   Supports transients for efficient bulk updates."
  (:refer-clojure :exclude [format])
  (:require
   [aoc.util.bounded :as b]
   [clojure.string :as str])
  (:import
   [clojure.lang
    Associative
    Counted
    IEditableCollection
    IFn
    IObj
    IPersistentCollection
    IPersistentMap
    ITransientAssociative2
    ITransientMap
    MapEntry
    Seqable]))

(def bounds b/bounds)
(def width b/width)
(def height b/height)
(def size b/size)
(def top-left b/top-left)
(def top-right b/top-right)
(def bottom-right b/bottom-right)
(def bottom-left b/bottom-left)
(def corners b/corners)

(definterface IKeyIndexed
  (^long key_index [key]))

(declare ->GridVec)

(deftype TransientGridVec [^:unsynchronized-mutable cells ^long width ^long height]
  ITransientMap
  (assoc [this k v]
    (let [[x y] k]
      (if (and (< -1 x width) (< -1 y height))
        (let [idx (+ (* y width) x)]
          (set! cells (assoc! cells idx v))
          this)
        this)))
  (conj [this o]
    (if (map? o)
      (reduce (fn [^ITransientMap g [k v]] (.assoc g k v)) this o)
      (if-let [[k v] (seq o)]
        (.assoc this k v)
        this)))
  (without [this _]
    ;; dissoc! is a no-op as the grid is a fixed size and coordinates cannot be removed.
    this)
  (persistent [_]
    (->GridVec (persistent! cells) width height nil))
  (valAt [this k]
    (.valAt this k nil))
  (valAt [_ k default]
    (let [[x y] k]
      (if (and (< -1 x width) (< -1 y height))
        (nth cells (+ (* y width) x))
        default)))
  (count [_] (count cells))

  ITransientAssociative2
  (containsKey [_ k]
    (let [[x y] k]
      (and (< -1 x width) (< -1 y height)))))

(deftype GridVec [cells ^long width ^long height _meta]
  b/Bounded
  (bounds [_] [[0 0] [(dec width) (dec height)]])
  (width [_] width)
  (height [_] height)

  IKeyIndexed
  (key_index [_ [x y]] (+ (* y width) x))

  IObj
  (meta [_] _meta)
  (withMeta [_ m] (GridVec. cells width height m))

  IEditableCollection
  (asTransient [_]
    (TransientGridVec. (transient cells) width height))

  IPersistentMap
  (without [this _]
    ;; dissoc! is a no-op as the grid is a fixed size and coordinates cannot be removed.
    this)
  (assocEx [this k v] (.assoc this k v))

  IPersistentCollection
  (cons [this o]
    (if (map? o)
      (reduce (fn [g [k v]] (.assoc g k v)) this o)
      (if-let [[k v] (seq o)]
        (.assoc this k v)
        this)))
  (empty [_] (GridVec. [] 0 0 nil))
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
    (if (.containsKey this k)
      (GridVec. (assoc cells (.key-index this k) v) width height _meta)
      this))
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
   (GridVec. (vec (repeat (* width height) default-value)) width height nil)))

(defn rows->grid-vec
  "Creates a GridVec from a sequence of rows (strings or sequences).
   Optionally takes a `value-fn` that will be mapped over each element
   before it is put into the grid. Assumes all rows have the same length."
  ([lines] (rows->grid-vec lines identity))
  ([lines value-fn]
   (let [height (count lines)
         width (count (first lines))]
     (assert (every? #(= width (count %)) lines))
     (GridVec. (vec (mapcat #(map value-fn %) lines)) width height nil))))

(defn column "Returns a vector of the values in the column at x."
  [^GridVec grid x]
  (let [w (.width grid)
        cells (.cells grid)]
    (mapv #(nth cells (+ (* % w) x)) (range (.height grid)))))

(defn row "Returns a vector the of values in the row at y."
  [^GridVec grid y]
  (let [w (.width grid)
        start (* y w)]
    (subvec (.cells grid) start (+ start w))))

(defn top-row "Returns the top row of the grid."
  [^GridVec grid] (row grid 0))

(defn bottom-row "Returns the bottom row of the grid."
  [^GridVec grid] (row grid (dec (.height grid))))

(defn left-column "Returns the left-most column of the grid."
  [^GridVec grid] (column grid 0))

(defn right-column "Returns the right-most column of the grid."
  [^GridVec grid] (column grid (dec (.width grid))))

(defn update-grid
  "Returns a new GridVec with `entry-fn` applied to every cell.
   `entry-fn` receives a MapEntry of `[[x y] value]` and should return the new value
   for the entry."
  [^GridVec grid entry-fn]
  (GridVec. (vec (map entry-fn grid)) (width grid) (height grid) (meta grid)))

(defn rotate-clockwise
  "Returns a new grid rotated 90 degrees clockwise."
  [^GridVec grid]
  (let [w (.width grid)
        h (.height grid)
        cells (.cells grid)]
    (GridVec. (vec (for [x (range w)
                         y (range (dec h) -1 -1)]
                     (nth cells (+ (* y w) x))))
              h w (meta grid))))

(defn flip-horizontal
  "Returns a new grid flipped horizontally (across the y-axis)."
  [^GridVec grid]
  (let [w (.width grid)
        h (.height grid)
        cells (.cells grid)]
    (GridVec. (vec (mapcat reverse (partition w cells))) w h (meta grid))))

(defn flip-vertical
  "Returns a new grid flipped vertically (across the x-axis)."
  [^GridVec grid]
  (let [w (.width grid)
        h (.height grid)
        cells (.cells grid)]
    (GridVec. (vec (mapcat identity (reverse (partition w cells)))) w h (meta grid))))

(defn sub-grid
  "Returns a new grid containing the rectangular region defined by top-left and bottom-right coordinates (inclusive)."
  [^GridVec grid [min-x min-y] [max-x max-y]]
  (let [w (.width grid)
        new-w (inc (- max-x min-x))
        new-h (inc (- max-y min-y))
        cells (.cells grid)
        new-cells (vec (mapcat (fn [y]
                                 (let [start (+ (* y w) min-x)]
                                   (subvec cells start (+ start new-w))))
                               (range min-y (inc max-y))))]
    (GridVec. new-cells new-w new-h (meta grid))))

(defn set-sub-grid
  "Returns a new grid with the `sub-grid` pasted at `top-left` coordinates.
   Parts of the sub-grid that fall outside the bounds of the base grid are ignored."
  [^GridVec grid [start-x start-y] ^GridVec sub-grid]
  (let [w (.width grid)
        h (.height grid)
        sub-w (.width sub-grid)
        sub-h (.height sub-grid)
        sub-cells (.cells sub-grid)]
    (loop [y 0
           cells (transient (.cells grid))]
      (if (= y sub-h)
        (GridVec. (persistent! cells) w h (meta grid))
        (let [target-y (+ start-y y)]
          (if-not (< -1 target-y h)
            (recur (inc y) cells)
            (recur (inc y)
                   (reduce (fn [c x]
                             (let [target-x (+ start-x x)]
                               (if-not (< -1 target-x w)
                                 c
                                 (assoc! c (+ (* target-y w) target-x)
                                         (nth sub-cells (+ (* y sub-w) x))))))
                           cells
                           (range sub-w)))))))))

(defn format-rows
  "Formats the grid as a vector of strings, one for each row.
   Options:
   - `:value-fn`: Function to transform cell values before printing (default: identity).
   - `:col-sep`: Separator string between columns (default: \"\")."
  [^GridVec grid & {:keys [value-fn col-sep] :as _options}]
  (let [value-fn (or value-fn identity)
        col-sep (or col-sep "")]
    (vec (for [y (range (height grid))]
           (str/join col-sep
                     (for [x (range (width grid))] (value-fn (grid [x y]))))))))

(defn format-grid
  "Formats the grid as a string.
   Options:
   - `:value-fn`: Function to transform cell values before printing (default: identity).
   - `:col-sep`: Separator string between columns (default: \"\").
   - `:row-sep`: Separator string between rows (default: \"\\n\")."
  [^GridVec grid & {:keys [row-sep] :or {row-sep "\n"} :as options}]
  (str/join row-sep (apply format-rows grid (mapcat identity options))))
