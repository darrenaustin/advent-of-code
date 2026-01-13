(ns aoc.util.grid
  "An immutable 2D grid implementation backed by a flat vector.
   Supports efficient random access, updates, and sequence operations.
   Implements standard Clojure collection interfaces (Associative, Counted, Seqable, IFn).
   Supports transients for efficient bulk updates."
  (:refer-clojure :exclude [format])
  (:require
   [aoc.util.bounded :as b]
   [aoc.util.string :as s]
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
    Seqable]
   [java.util Iterator]
   [java.lang Iterable]))

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

(declare ->Grid)

(deftype GridIterator [cells ^long width ^:unsynchronized-mutable ^long idx ^long len]
  Iterator
  (hasNext [_] (< idx len))
  (next [_]
    (if (< idx len)
      (let [val (nth cells idx)
            y (quot idx width)
            x (rem idx width)]
        (set! idx (inc idx))
        (MapEntry. [x y] val))
      (throw (java.util.NoSuchElementException.)))))

(deftype TransientGrid [^:unsynchronized-mutable cells ^long width ^long height]
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
    (->Grid (persistent! cells) width height nil))
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

(deftype Grid [cells ^long width ^long height _meta]
  b/Bounded
  (bounds [_] [[0 0] [(dec width) (dec height)]])
  (width [_] width)
  (height [_] height)

  IKeyIndexed
  (key_index [_ [x y]] (+ (* y width) x))

  IObj
  (meta [_] _meta)
  (withMeta [_ m] (Grid. cells width height m))

  IEditableCollection
  (asTransient [_]
    (TransientGrid. (transient cells) width height))

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
  (empty [_] (Grid. [] 0 0 nil))
  (equiv [_ o]
    (and (instance? Grid o)
         (= width (.width ^Grid o))
         (= height (.height ^Grid o))
         (= cells (.cells ^Grid o))))

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
      (Grid. (assoc cells (.key-index this k) v) width height _meta)
      this))
  (valAt [this k]
    (.valAt this k nil))
  (valAt [this k default]
    (if (.containsKey this k)
      (nth cells (.key-index this k))
      default))

  Iterable
  (iterator [_]
    (GridIterator. cells width 0 (count cells)))

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

(defn make-grid
  "Creates a new Grid of the specified dimensions, initialized with `default-value` (default nil)."
  ([width height] (make-grid width height nil))
  ([width height default-value]
   (Grid. (vec (repeat (* width height) default-value)) width height nil)))

(defn rows->grid
  "Creates a Grid from a sequence of rows (strings or sequences).
   Optionally takes a `value-fn` that will be mapped over each element
   before it is put into the grid. Assumes all rows have the same length."
  ([lines] (rows->grid lines identity))
  ([lines value-fn]
   (let [height (count lines)
         width (count (first lines))]
     (assert (every? #(= width (count %)) lines))
     (Grid. (vec (mapcat #(map value-fn %) lines)) width height nil))))

(defn str->grid
  ([s] (str->grid s identity))
  ([s value-fn] (rows->grid (s/lines s) value-fn)))

(defn column "Returns a vector of the values in the column at x."
  [^Grid grid x]
  (let [w (.width grid)
        cells (.cells grid)]
    (mapv #(nth cells (+ (* % w) x)) (range (.height grid)))))

(defn set-column
  "Sets the values of column `x` to `values`.
   If `values` is shorter than the height, only those cells are updated.
   If `values` is longer, the extra values are ignored."
  [^Grid grid x values]
  (let [w (.width grid)
        h (.height grid)]
    (if (or (neg? x) (>= x w))
      grid
      (let [cells (reduce (fn [c [y v]]
                            (if (< y h)
                              (assoc! c (+ (* y w) x) v)
                              c))
                          (transient (.cells grid))
                          (map-indexed vector values))]
        (Grid. (persistent! cells) w h (meta grid))))))

(defn row "Returns a vector the of values in the row at y."
  [^Grid grid y]
  (let [w (.width grid)
        start (* y w)]
    (subvec (.cells grid) start (+ start w))))

(defn set-row
  "Sets the values of row `y` to `values`.
   If `values` is shorter than the width, only those cells are updated.
   If `values` is longer, the extra values are ignored."
  [^Grid grid y values]
  (let [w (.width grid)
        h (.height grid)]
    (if (or (neg? y) (>= y h))
      grid
      (let [start-idx (* y w)
            cells (reduce (fn [c [x v]]
                            (if (< x w)
                              (assoc! c (+ start-idx x) v)
                              c))
                          (transient (.cells grid))
                          (map-indexed vector values))]
        (Grid. (persistent! cells) w h (meta grid))))))

(defn top-row "Returns the top row of the grid."
  [^Grid grid] (row grid 0))

(defn bottom-row "Returns the bottom row of the grid."
  [^Grid grid] (row grid (dec (.height grid))))

(defn left-column "Returns the left-most column of the grid."
  [^Grid grid] (column grid 0))

(defn right-column "Returns the right-most column of the grid."
  [^Grid grid] (column grid (dec (.width grid))))

(defn update-grid
  "Returns a new Grid with `entry-fn` applied to every cell.
   `entry-fn` receives a MapEntry of `[[x y] value]` and should return the new value
   for the entry."
  [^Grid grid entry-fn]
  (Grid. (vec (map entry-fn grid)) (width grid) (height grid) (meta grid)))

(defn rotate-clockwise
  "Returns a new grid rotated 90 degrees clockwise."
  [^Grid grid]
  (let [w (.width grid)
        h (.height grid)
        cells (.cells grid)]
    (Grid. (vec (for [x (range w)
                      y (range (dec h) -1 -1)]
                  (nth cells (+ (* y w) x))))
           h w (meta grid))))

(defn flip-horizontal
  "Returns a new grid flipped horizontally (across the y-axis)."
  [^Grid grid]
  (let [w (.width grid)
        h (.height grid)
        cells (.cells grid)]
    (Grid. (vec (mapcat reverse (partition w cells))) w h (meta grid))))

(defn flip-vertical
  "Returns a new grid flipped vertically (across the x-axis)."
  [^Grid grid]
  (let [w (.width grid)
        h (.height grid)
        cells (.cells grid)]
    (Grid. (vec (mapcat identity (reverse (partition w cells)))) w h (meta grid))))

(defn sub-grid
  "Returns a new grid containing the rectangular region defined by top-left and bottom-right coordinates (inclusive)."
  [^Grid grid [min-x min-y] [max-x max-y]]
  (let [w (.width grid)
        new-w (inc (- max-x min-x))
        new-h (inc (- max-y min-y))
        cells (.cells grid)
        new-cells (vec (mapcat (fn [y]
                                 (let [start (+ (* y w) min-x)]
                                   (subvec cells start (+ start new-w))))
                               (range min-y (inc max-y))))]
    (Grid. new-cells new-w new-h (meta grid))))

(defn set-sub-grid
  "Returns a new grid with the `sub-grid` pasted at `top-left` coordinates.
   Parts of the sub-grid that fall outside the bounds of the base grid are ignored."
  [^Grid grid [start-x start-y] ^Grid sub-grid]
  (let [w (.width grid)
        h (.height grid)
        sub-w (.width sub-grid)
        sub-h (.height sub-grid)
        sub-cells (.cells sub-grid)]
    (loop [y 0
           cells (transient (.cells grid))]
      (if (= y sub-h)
        (Grid. (persistent! cells) w h (meta grid))
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
  [^Grid grid & {:keys [value-fn col-sep] :as _options}]
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
  [^Grid grid & {:keys [row-sep] :or {row-sep "\n"} :as options}]
  (str/join row-sep (apply format-rows grid (mapcat identity options))))
