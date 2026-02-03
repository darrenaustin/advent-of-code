(ns aoc.util.grid
  "An immutable 2D grid implementation.
   Provides support for both dense (Vector-backed) and sparse (Map-backed) grids.
   Both implementations support standard Clojure collection interfaces.

   The dense `Grid` is efficient for fixed-size, rectangular data.
   The `SparseGrid` is efficient for infinite or mostly-empty 2D spaces."
  (:refer-clojure :exclude [format])
  (:require
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
   [java.lang Iterable]
   [java.util Iterator]))

(defprotocol Bounded
  "Protocol for 2D spatial bounds and dimensions."
  (bounds [this] "Returns a pair of [[min-x min-y] [max-x max-y]] coordinates.")
  (width [this] "Returns the width (number of columns) of the grid.")
  (height [this] "Returns the height (number of rows) of the grid."))

(defn size
  "Returns the [width height] of the grid."
  [grid] [(width grid) (height grid)])

(defn top-left "Returns the [x y] coordinate of the top-left corner."
  [grid] (first (bounds grid)))

(defn top-right "Returns the [x y] coordinate of the top-right corner."
  [grid]
  (let [[[_ min-y] [max-x _]] (bounds grid)]
    [max-x min-y]))

(defn bottom-right "Returns the [x y] coordinate of the bottom-right corner."
  [grid] (second (bounds grid)))

(defn bottom-left "Returns the [x y] coordinate of the bottom-left corner."
  [grid]
  (let [[[min-x _] [_ max-y]] (bounds grid)]
    [min-x max-y]))

(defn top "Returns the minimum y coordinate (top edge)."
  [grid]
  (let [[[_ min-y] _] (bounds grid)]
    min-y))

(defn bottom "Returns the maximum y coordinate (bottom edge)."
  [grid]
  (let [[_ [_ max-y]] (bounds grid)]
    max-y))

(defn left "Returns the minimum x coordinate (left edge)."
  [grid]
  (let [[[min-x _] _] (bounds grid)]
    min-x))

(defn right "Returns the maximum x coordinate (right edge)."
  [grid]
  (let [[_ [max-x _]] (bounds grid)]
    max-x))

(defn corners "Returns a sequence of the four corner coordinates."
  [grid]
  (let [[[min-x min-y] [max-x max-y]] (bounds grid)]
    [[min-x min-y] [max-x min-y] [max-x max-y] [min-x max-y]]))

(defprotocol KeyIndexed
  (^long key-index [this key]))

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
  Bounded
  (bounds [_] [[0 0] [(dec width) (dec height)]])
  (width [_] width)
  (height [_] height)

  KeyIndexed
  (key-index [_ [x y]] (+ (* y width) x))

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
      (MapEntry. k (nth cells (key-index this k)))))
  (assoc [this k v]
    (if (.containsKey this k)
      (Grid. (assoc cells (key-index this k) v) width height _meta)
      this))
  (valAt [this k]
    (.valAt this k nil))
  (valAt [this k default]
    (if (.containsKey this k)
      (nth cells (key-index this k))
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

(defn ->grid
  "Creates a Grid from various input types:
   - String: Creates a Grid from a string representation.
   - Sequence: Creates a Grid from a sequence of rows.
   - 2 integers: Creates a Grid of the specified width and height.

   Optionally takes a `value-fn` (default `identity`) when creating from String or Sequence.
   Optionally takes a `default-value` when creating with width and height."
  ([x]
   (cond
     (string? x) (str->grid x)
     (sequential? x) (rows->grid x)
     :else (throw (IllegalArgumentException. (str "Unsupported type for ->grid: " (type x))))))
  ([x y]
   (cond
     (and (integer? x) (integer? y)) (make-grid x y)
     (string? x) (str->grid x y)
     (sequential? x) (rows->grid x y)
     :else (throw (IllegalArgumentException. (str "Unsupported types for ->grid: " (type x) ", " (type y))))))
  ([w h default-value]
   (if (and (integer? w) (integer? h))
     (make-grid w h default-value)
     (throw (IllegalArgumentException. "->grid expects [width height default-value]")))))

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

(declare ->SparseGrid)

(defn- update-bounds-assoc [bounds [x y]]
  (if bounds
    (let [[[min-x min-y] [max-x max-y]] bounds]
      [[(min min-x x) (min min-y y)]
       [(max max-x x) (max max-y y)]])
    [[x y] [x y]]))

(defn- calc-bounds [cells]
  (when (seq cells)
    (loop [locs (keys cells)
           min-x Long/MAX_VALUE min-y Long/MAX_VALUE
           max-x Long/MIN_VALUE max-y Long/MIN_VALUE]
      (if (empty? locs)
        [[min-x min-y] [max-x max-y]]
        (let [[x y] (first locs)]
          (recur (rest locs)
                 (min min-x x) (min min-y y)
                 (max max-x x) (max max-y y)))))))

(defn- update-bounds-dissoc [bounds cells [x y]]
  (if (and bounds (contains? cells [x y]))
    (let [[[min-x min-y] [max-x max-y]] bounds]
      (if (or (= x min-x) (= x max-x) (= y min-y) (= y max-y))
        (calc-bounds (dissoc cells [x y]))
        bounds))
    bounds))

(deftype TransientSparseGrid [^:unsynchronized-mutable cells ^:unsynchronized-mutable bounds ^:unsynchronized-mutable dirty-bounds?]
  ITransientMap
  (assoc [this k v]
    (set! cells (assoc! cells k v))
    (set! bounds (update-bounds-assoc bounds k))
    this)
  (conj [this o]
    (if (map? o)
      (reduce (fn [^ITransientMap g [k v]] (.assoc g k v)) this o)
      (if-let [[k v] (seq o)]
        (.assoc this k v)
        this)))
  (without [this k]
    (if (contains? cells k)
      (do
        (set! cells (dissoc! cells k))
        (when (and bounds (not dirty-bounds?))
          (let [[[min-x min-y] [max-x max-y]] bounds
                [x y] k]
            (when (or (= x min-x) (= x max-x) (= y min-y) (= y max-y))
              (set! dirty-bounds? true))))
        this)
      this))
  (persistent [_]
    (let [p-cells (persistent! cells)
          final-bounds (if dirty-bounds? (calc-bounds p-cells) bounds)]
      (->SparseGrid p-cells final-bounds nil)))
  (valAt [this k]
    (.valAt this k nil))
  (valAt [_ k default]
    (get cells k default))
  (count [_] (count cells))

  ITransientAssociative2
  (containsKey [_ k]
    (contains? cells k)))

(deftype SparseGrid [cells bounds _meta]
  Bounded
  (bounds [_] bounds)
  (width [_]
    (if bounds
      (let [[[min-x _] [max-x _]] bounds]
        (inc (- max-x min-x)))
      0))
  (height [_]
    (if bounds
      (let [[[_ min-y] [_ max-y]] bounds]
        (inc (- max-y min-y)))
      0))

  IObj
  (meta [_] _meta)
  (withMeta [_ m] (SparseGrid. cells bounds m))

  IEditableCollection
  (asTransient [_]
    (TransientSparseGrid. (transient cells) bounds false))

  IPersistentMap
  (assoc [_ k v]
    (let [new-cells (assoc cells k v)
          new-bounds (update-bounds-assoc bounds k)]
      (SparseGrid. new-cells new-bounds _meta)))
  (assocEx [this k v]
    (if (contains? cells k)
      (throw (Exception. "Key already present"))
      (.assoc this k v)))
  (without [this k]
    (if (contains? cells k)
      (let [new-bounds (update-bounds-dissoc bounds cells k)
            new-cells (dissoc cells k)]
        (SparseGrid. new-cells new-bounds _meta))
      this))

  IPersistentCollection
  (cons [this o]
    (if (map? o)
      (reduce (fn [g [k v]] (.assoc g k v)) this o)
      (if-let [[k v] (seq o)]
        (.assoc this k v)
        this)))
  (empty [_] (SparseGrid. {} nil _meta))
  (equiv [_ o]
    (and (instance? SparseGrid o)
         (= cells (.cells ^SparseGrid o))))

  Counted
  (count [_] (count cells))

  Associative
  (containsKey [_ k] (contains? cells k))
  (entryAt [_ k]
    (when (contains? cells k)
      (MapEntry. k (get cells k))))
  (valAt [_ k] (get cells k))
  (valAt [_ k default] (get cells k default))

  Seqable
  (seq [_] (seq cells))

  IFn
  (invoke [this k] (.valAt this k))
  (invoke [this k default] (.valAt this k default))

  Object
  (toString [_]
    (if bounds
      (let [[[min-x min-y] [max-x max-y]] bounds]
        (str/join "\n"
                  (for [y (range min-y (inc max-y))]
                    (str/join (for [x (range min-x (inc max-x))]
                                (get cells [x y] \.)))))) ;; Default to . for visualization
      ""))
  (equals [this o] (.equiv this o))
  (hashCode [_] (hash cells)))

(defn make-sparse-grid
  "Creates a new empty SparseGrid."
  []
  (SparseGrid. {} nil nil))

(defn map->sparse-grid
  "Creates a SparseGrid from a map of {[x y] value}.
   Optionally takes a `value-fn` that will be mapped over each value.
   If `value-fn` returns nil, the cell is not added to the grid."
  ([m] (SparseGrid. m (calc-bounds m) nil))
  ([m value-fn]
   (let [new-m (reduce-kv (fn [acc k v]
                            (if-let [new-v (value-fn v)]
                              (assoc acc k new-v)
                              acc))
                          {}
                          m)]
     (SparseGrid. new-m (calc-bounds new-m) nil))))

(defn rows->sparse-grid
  "Creates a SparseGrid from a sequence of rows (strings or sequences).
   Optionally takes a `value-fn` that will be mapped over each element.
   If `value-fn` returns nil, the cell is not added to the grid."
  ([lines] (rows->sparse-grid lines identity))
  ([lines value-fn]
   (let [grid (transient (make-sparse-grid))]
     (doseq [[y row] (map-indexed vector lines)
             [x val] (map-indexed vector row)]
       (when-let [v (value-fn val)]
         (assoc! grid [x y] v)))
     (persistent! grid))))

(defn str->sparse-grid
  ([s] (str->sparse-grid s identity))
  ([s value-fn] (rows->sparse-grid (s/lines s) value-fn)))

(defn ->sparse-grid
  "Creates a SparseGrid from various input types:
   - No arguments: Creates an empty SparseGrid.
   - Map: Creates a SparseGrid from a map of {[x y] value}.
   - String: Creates a SparseGrid from a string representation.
   - Sequence: Creates a SparseGrid from a sequence of rows.

   Optionally takes a `value-fn` (default `identity`) when creating from String, Sequence, or Map."
  ([] (make-sparse-grid))
  ([x]
   (cond
     (map? x) (map->sparse-grid x)
     (string? x) (str->sparse-grid x)
     (sequential? x) (rows->sparse-grid x)
     :else (throw (IllegalArgumentException. (str "Unsupported type for ->sparse-grid: " (type x))))))
  ([x value-fn]
   (cond
     (map? x) (map->sparse-grid x value-fn)
     (string? x) (str->sparse-grid x value-fn)
     (sequential? x) (rows->sparse-grid x value-fn)
     :else (throw (IllegalArgumentException. (str "Unsupported type for ->sparse-grid with value-fn: " (type x)))))))

(defn format-rows
  "Formats the grid as a sequence of row strings.

   Options:
   - `:value-fn`: Function to transform cell values before printing (default: identity).
   - `:col-sep`: Separator string between columns (default: empty string).
   - `:default`: Value to use for missing cells (sparse grid holes, default: \\.)."
  [grid & {:keys [value-fn col-sep default]
           :or   {value-fn identity col-sep "" default \.}}]
  (let [row-formatter (fn [row-y min-x max-x val-getter]
                        (str/join col-sep
                                  (for [x (range min-x (inc max-x))]
                                    (value-fn (val-getter x row-y)))))]
    (if-let [b (bounds grid)]
      (let [[[min-x min-y] [max-x max-y]] b]
        (for [y (range min-y (inc max-y))]
          (row-formatter y min-x max-x (fn [x y] (get grid [x y] default)))))
      [])))

(defn format
  "Formats the grid (dense or sparse) as a string.

   Options:
   - `:value-fn`: Function to transform cell values before printing (default: identity).
   - `:col-sep`: Separator string between columns (default: empty string).
   - `:row-sep`: Separator string between rows (default: newline).
   - `:default`: Value to use for missing cells (sparse grid holes, default: \\.)."
  [grid & args]
  (let [{:keys [row-sep] :or {row-sep "\n"}} (apply hash-map args)]
    (str/join row-sep (apply format-rows grid args))))
