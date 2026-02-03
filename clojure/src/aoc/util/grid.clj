(ns aoc.util.grid
  "An immutable 2D grid implementation using the Strategy Pattern.
   Provides a single `Grid` type that delegates storage to a `GridStorage` protocol.
   Supports both dense (Vector-backed) and sparse (Map-backed) storage strategies.

   The dense strategy is efficient for fixed-size, rectangular data.
   The sparse strategy is efficient for infinite or mostly-empty 2D spaces."
  (:refer-clojure :exclude [format])
  (:require
   [aoc.util.string :as s]
   [clojure.string :as str])
  (:import
   [clojure.lang
    Associative
    Counted
    IFn
    IObj
    IPersistentCollection
    IPersistentMap
    MapEntry
    Seqable]
   [java.lang Iterable]))

;; --- Protocols ---

(defprotocol Bounded
  "Protocol for 2D spatial bounds and dimensions."
  (bounds [this] "Returns a pair of [[min-x min-y] [max-x max-y]] coordinates.")
  (width [this] "Returns the width (number of columns) of the grid.")
  (height [this] "Returns the height (number of rows) of the grid."))

(defprotocol GridStorage
  "Protocol for the underlying storage mechanism of a Grid."
  (storage-get [this x y default])
  (storage-assoc [this x y v])
  (storage-dissoc [this x y])
  (storage-bounds [this])
  (storage-width [this])
  (storage-height [this])
  (storage-count [this])
  (storage-seq [this])
  (storage-transient [this])
  (storage-meta [this])
  (storage-with-meta [this m])
  ;; Optimized Transformations
  (storage-rotate-clockwise [this])
  (storage-flip-horizontal [this])
  (storage-flip-vertical [this])
  (storage-map [this f]))

(defprotocol TransientGridStorage
  "Protocol for transient storage operations."
  (t-storage-assoc [this x y v])
  (t-storage-dissoc [this x y])
  (t-storage-get [this x y default])
  (t-storage-count [this])
  (t-storage-persistent [this]))

;; --- Forward Declarations ---

(declare ->Grid)

;; --- Helper Functions (Bounded) ---

(defn size "Returns the [width height] of the grid."
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

(defn key-index "Returns the linear index of a coordinate [x y] for a grid of a given width."
  [grid [x y]]
  (+ (* y (width grid)) x))

;; --- Helper Functions (Internal) ---

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

;; --- Implementations: Dense (Vector) Storage ---

(declare ->VectorStorage)

(deftype TransientVectorStorage [^:unsynchronized-mutable cells ^long width ^long height]
  TransientGridStorage
  (t-storage-assoc [this x y v]
    (if (and (< -1 x width) (< -1 y height))
      (let [idx (+ (* y width) x)]
        (set! cells (assoc! cells idx v))
        this)
      this))
  (t-storage-dissoc [this _ _]
    ;; Dissoc is a no-op for fixed-size dense grids
    this)
  (t-storage-get [_ x y default]
    (if (and (< -1 x width) (< -1 y height))
      (get cells (+ (* y width) x) default)
      default))
  (t-storage-count [_] (count cells))
  (t-storage-persistent [_]
    (->VectorStorage (persistent! cells) width height nil)))

(deftype VectorStorage [cells ^long width ^long height _meta]
  GridStorage
  (storage-get [_ x y default]
    (if (and (< -1 x width) (< -1 y height))
      (nth cells (+ (* y width) x))
      default))
  (storage-assoc [_ x y v]
    (if (and (< -1 x width) (< -1 y height))
      (VectorStorage. (assoc cells (+ (* y width) x) v) width height _meta)
      (VectorStorage. cells width height _meta)))
  (storage-dissoc [this _ _]
    this)
  (storage-bounds [_]
    [[0 0] [(dec width) (dec height)]])
  (storage-width [_] width)
  (storage-height [_] height)
  (storage-count [_] (count cells))
  (storage-seq [_]
    (map #(MapEntry. %1 %2)
         (for [y (range height) x (range width)] [x y])
         cells))
  (storage-transient [_]
    (TransientVectorStorage. (transient cells) width height))
  (storage-meta [_] _meta)
  (storage-with-meta [_ m] (VectorStorage. cells width height m))

  (storage-rotate-clockwise [_]
    (VectorStorage. (vec (for [x (range width)
                               y (range (dec height) -1 -1)]
                           (nth cells (+ (* y width) x))))
                    height width _meta))
  (storage-flip-horizontal [_]
    (VectorStorage. (vec (mapcat reverse (partition width cells))) width height _meta))
  (storage-flip-vertical [_]
    (VectorStorage. (vec (mapcat identity (reverse (partition width cells)))) width height _meta))
  (storage-map [this f]
    (VectorStorage. (vec (map f (storage-seq this))) width height _meta)))

;; --- Implementations: Sparse (Map) Storage ---

(declare ->MapStorage)

(deftype TransientMapStorage [^:unsynchronized-mutable cells ^:unsynchronized-mutable bounds ^:unsynchronized-mutable dirty-bounds?]
  TransientGridStorage
  (t-storage-assoc [this x y v]
    (set! cells (assoc! cells [x y] v))
    (set! bounds (update-bounds-assoc bounds [x y]))
    this)
  (t-storage-dissoc [this x y]
    (if (contains? cells [x y])
      (do
        (set! cells (dissoc! cells [x y]))
        (when (and bounds (not dirty-bounds?))
          (let [[[min-x min-y] [max-x max-y]] bounds]
            (when (or (= x min-x) (= x max-x) (= y min-y) (= y max-y))
              (set! dirty-bounds? true))))
        this)
      this))
  (t-storage-get [_ x y default]
    (get cells [x y] default))
  (t-storage-count [_] (count cells))
  (t-storage-persistent [_]
    (let [p-cells (persistent! cells)
          final-bounds (if dirty-bounds? (calc-bounds p-cells) bounds)]
      (->MapStorage p-cells final-bounds nil))))

(deftype MapStorage [cells bounds _meta]
  GridStorage
  (storage-get [_ x y default]
    (get cells [x y] default))
  (storage-assoc [_ x y v]
    (let [new-cells (assoc cells [x y] v)
          new-bounds (update-bounds-assoc bounds [x y])]
      (MapStorage. new-cells new-bounds _meta)))
  (storage-dissoc [this x y]
    (if (contains? cells [x y])
      (let [new-bounds (update-bounds-dissoc bounds cells [x y])
            new-cells (dissoc cells [x y])]
        (MapStorage. new-cells new-bounds _meta))
      this))
  (storage-bounds [_] bounds)
  (storage-width [_]
    (if bounds
      (let [[[min-x _] [max-x _]] bounds]
        (inc (- max-x min-x)))
      0))
  (storage-height [_]
    (if bounds
      (let [[[_ min-y] [_ max-y]] bounds]
        (inc (- max-y min-y)))
      0))
  (storage-count [_] (count cells))
  (storage-seq [_] (seq cells))
  (storage-transient [_]
    (TransientMapStorage. (transient cells) bounds false))
  (storage-meta [_] _meta)
  (storage-with-meta [_ m] (MapStorage. cells bounds m))

  (storage-rotate-clockwise [_]
    (let [new-cells (zipmap (map (fn [[x y]] [(- y) x]) (keys cells))
                            (vals cells))]
      (MapStorage. new-cells (calc-bounds new-cells) _meta)))

  (storage-flip-horizontal [_] ;; (x, y) -> (-x, y)
    (let [new-cells (zipmap (map (fn [[x y]] [(- x) y]) (keys cells))
                            (vals cells))]
      (MapStorage. new-cells (calc-bounds new-cells) _meta)))

  (storage-flip-vertical [_] ;; (x, y) -> (x, -y)
    (let [new-cells (zipmap (map (fn [[x y]] [x (- y)]) (keys cells))
                            (vals cells))]
      (MapStorage. new-cells (calc-bounds new-cells) _meta)))

  (storage-map [this f]
    (let [new-cells (into {} (map f (storage-seq this)))]
      (MapStorage. new-cells (calc-bounds new-cells) _meta))))

;; --- The Unified Grid Type ---

(declare ->Grid)
(declare format)
(declare make-sparse-grid)

(deftype TransientGrid [storage]
  clojure.lang.ITransientMap
  (assoc [this k v]
    (let [[x y] k]
      (t-storage-assoc storage x y v)
      this))
  (conj [this o]
    (if (map? o)
      (reduce (fn [^clojure.lang.ITransientMap g [k v]] (.assoc g k v)) this o)
      (if-let [[k v] (seq o)]
        (.assoc this k v)
        this)))
  (without [this k]
    (let [[x y] k]
      (t-storage-dissoc storage x y)
      this))
  (persistent [_]
    (->Grid (t-storage-persistent storage)))
  (valAt [this k]
    (.valAt this k nil))
  (valAt [_ k default]
    (let [[x y] k]
      (t-storage-get storage x y default)))
  (count [_]
    (t-storage-count storage)))

(deftype Grid [storage]
  Bounded
  (bounds [_] (storage-bounds storage))
  (width [_] (storage-width storage))
  (height [_] (storage-height storage))

  IObj
  (meta [_] (storage-meta storage))
  (withMeta [_ m] (Grid. (storage-with-meta storage m)))

  clojure.lang.IEditableCollection
  (asTransient [_]
    (TransientGrid. (storage-transient storage)))

  IPersistentMap
  (without [_ k]
    (let [[x y] k]
      (Grid. (storage-dissoc storage x y))))
  (assocEx [this k v]
    (if (.containsKey this k)
      (throw (Exception. "Key already present"))
      (.assoc this k v)))
  (assoc [_ k v]
    (let [[x y] k]
      (Grid. (storage-assoc storage x y v))))

  IPersistentCollection
  (cons [this o]
    (if (map? o)
      (reduce (fn [g [k v]] (.assoc g k v)) this o)
      (if-let [[k v] (seq o)]
        (.assoc this k v)
        this)))
  (empty [_] (make-sparse-grid))
  (equiv [_ o]
    (and (instance? Grid o)
         (= (storage-seq storage) (storage-seq (.storage ^Grid o)))))

  Counted
  (count [_] (storage-count storage))

  Associative
  (containsKey [_ k]
    (let [[x y] k
          v (storage-get storage x y ::not-found)]
      (not= v ::not-found)))
  (entryAt [_ k]
    (let [[x y] k
          v (storage-get storage x y ::not-found)]
      (when (not= v ::not-found)
        (MapEntry. k v))))
  (valAt [_ k]
    (let [[x y] k]
      (storage-get storage x y nil)))
  (valAt [_ k default]
    (let [[x y] k]
      (storage-get storage x y default)))

  Seqable
  (seq [_] (storage-seq storage))

  Iterable
  (iterator [_] (.iterator ^Iterable (storage-seq storage)))

  IFn
  (invoke [this k] (.valAt this k))
  (invoke [this k default] (.valAt this k default))

  Object
  (toString [this] (format this))
  (equals [this o] (.equiv this o))
  (hashCode [_] (hash (storage-seq storage))))

;; --- Constructors & Factories ---

;; Dense

(defn make-grid
  "Creates a new Dense Grid of the specified dimensions, initialized with `default-value` (default nil)."
  ([width height] (make-grid width height nil))
  ([width height default-value]
   (Grid. (VectorStorage. (vec (repeat (* width height) default-value)) width height nil))))

(defn rows->grid
  "Creates a Dense Grid from a sequence of rows (strings or sequences).
   Optionally takes a `value-fn` that will be mapped over each element
   before it is put into the grid. Assumes all rows have the same length."
  ([lines] (rows->grid lines identity))
  ([lines value-fn]
   (let [height (count lines)
         width (count (first lines))]
     (assert (every? #(= width (count %)) lines))
     (Grid. (VectorStorage. (vec (mapcat #(map value-fn %) lines)) width height nil)))))

(defn str->grid
  ([s] (str->grid s identity))
  ([s value-fn] (rows->grid (s/lines s) value-fn)))

(defn ->grid
  "Creates a Dense Grid from various input types:
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
     (throw (IllegalArgumentException. "3-arity ->grid expects [width height default-value]")))))

;; Sparse

(defn make-sparse-grid
  "Creates a new empty Sparse Grid."
  []
  (Grid. (MapStorage. {} nil nil)))

(defn map->sparse-grid
  "Creates a Sparse Grid from a map of {[x y] value}.
   Optionally takes a `value-fn` that will be mapped over each value.
   If `value-fn` returns nil, the cell is not added to the grid."
  ([m] (Grid. (MapStorage. m (calc-bounds m) nil)))
  ([m value-fn]
   (let [new-m (reduce-kv (fn [acc k v]
                            (if-let [new-v (value-fn v)]
                              (assoc acc k new-v)
                              acc))
                          {}
                          m)]
     (Grid. (MapStorage. new-m (calc-bounds new-m) nil)))))

(defn rows->sparse-grid
  "Creates a Sparse Grid from a sequence of rows (strings or sequences).
   Optionally takes a `value-fn` that will be mapped over each element.
   If `value-fn` returns nil, the cell is not added to the grid."
  ([lines] (rows->sparse-grid lines identity))
  ([lines value-fn]
   (let [m (reduce
            (fn [acc [y row]]
              (reduce
               (fn [acc [x val]]
                 (if-let [v (value-fn val)]
                   (assoc! acc [x y] v)
                   acc))
               acc
               (map-indexed vector row)))
            (transient {})
            (map-indexed vector lines))
         m (persistent! m)]
     (->Grid (MapStorage. m (calc-bounds m) nil)))))

(defn str->sparse-grid
  ([s] (str->sparse-grid s identity))
  ([s value-fn] (rows->sparse-grid (s/lines s) value-fn)))

(defn ->sparse-grid
  "Creates a Sparse Grid from various input types:
   - No arguments or nil: Creates an empty Sparse Grid.
   - Map: Creates a Sparse Grid from a map of {[x y] value}.
   - String: Creates a Sparse Grid from a string representation.
   - Sequence: Creates a Sparse Grid from a sequence of rows.

   Optionally takes a `value-fn` (default `identity`) when creating from String, Sequence, or Map."
  ([] (make-sparse-grid))
  ([x]
   (cond
     (nil? x) (make-sparse-grid)
     (map? x) (map->sparse-grid x)
     (string? x) (str->sparse-grid x)
     (sequential? x) (rows->sparse-grid x)
     :else (throw (IllegalArgumentException. (str "Unsupported type for ->sparse-grid: " (type x))))))
  ([x value-fn]
   (cond
     (nil? x) (make-sparse-grid)
     (map? x) (map->sparse-grid x value-fn)
     (string? x) (str->sparse-grid x value-fn)
     (sequential? x) (rows->sparse-grid x value-fn)
     :else (throw (IllegalArgumentException. (str "Unsupported type for ->sparse-grid with value-fn: " (type x)))))))

;; --- Formatting ---

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

;; --- Common Operations ---

;; Using generic operations now that support BOTH dense and sparse grids

(defn column "Returns a vector of the values in the column at x."
  [^Grid grid x]
  (let [h (height grid)]
    (mapv (fn [y] (get grid [x y])) (range h))))

(defn set-column
  "Sets the values of column `x` to `values`.
   If `values` is shorter than the height, only those cells are updated.
   If `values` is longer, the extra values are ignored."
  [^Grid grid x values]
  (let [h (height grid)]
    (loop [y 0
           g grid
           vs values]
      (if (and (< y h) (seq vs))
        (recur (inc y) (assoc g [x y] (first vs)) (rest vs))
        g))))

(defn row "Returns a vector the of values in the row at y."
  [^Grid grid y]
  (let [w (width grid)]
    (mapv (fn [x] (get grid [x y])) (range w))))

(defn set-row
  "Sets the values of row `y` to `values`.
   If `values` is shorter than the width, only those cells are updated.
   If `values` is longer, the extra values are ignored."
  [^Grid grid y values]
  (let [w (width grid)]
    (loop [x 0
           g grid
           vs values]
      (if (and (< x w) (seq vs))
        (recur (inc x) (assoc g [x y] (first vs)) (rest vs))
        g))))

(defn top-row "Returns the top row of the grid."
  [^Grid grid] (row grid 0))

(defn bottom-row "Returns the bottom row of the grid."
  [^Grid grid] (row grid (dec (height grid))))

(defn left-column "Returns the left-most column of the grid."
  [^Grid grid] (column grid 0))

(defn right-column "Returns the right-most column of the grid."
  [^Grid grid] (column grid (dec (width grid))))

(defn update-grid
  "Returns a new Grid with `entry-fn` applied to every cell.
   `entry-fn` receives a MapEntry of `[[x y] value]` and should return the new value
   for the entry."
  [^Grid grid entry-fn]
  (Grid. (storage-map (.storage grid) entry-fn)))

(defn rotate-clockwise
  "Returns a new grid rotated 90 degrees clockwise."
  [^Grid grid]
  (Grid. (storage-rotate-clockwise (.storage grid))))

(defn flip-horizontal
  "Returns a new grid flipped horizontally (across the y-axis)."
  [^Grid grid]
  (Grid. (storage-flip-horizontal (.storage grid))))

(defn flip-vertical
  "Returns a new grid flipped vertically (across the x-axis)."
  [^Grid grid]
  (Grid. (storage-flip-vertical (.storage grid))))

(defn sub-grid
  "Returns a new grid containing the rectangular region defined by top-left and bottom-right coordinates (inclusive)."
  [grid [min-x min-y] [max-x max-y]]
  (let [new-w (inc (- max-x min-x))
        cells (for [y (range min-y (inc max-y))
                    x (range min-x (inc max-x))]
                (get grid [x y]))]
    (rows->grid (partition new-w cells))))

(defn set-sub-grid
  "Returns a new grid with the `sub-grid` pasted at `top-left` coordinates.
   Parts of the sub-grid that fall outside the bounds of the base grid are ignored (for fixed-size grids)."
  [^Grid grid [start-x start-y] ^Grid sub-grid]
  (reduce (fn [g [pos val]]
            (let [[sx sy] pos
                  target-x (+ start-x sx)
                  target-y (+ start-y sy)]
              (assoc g [target-x target-y] val)))
          grid
          sub-grid))
