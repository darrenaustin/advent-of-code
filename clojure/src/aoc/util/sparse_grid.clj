(ns aoc.util.sparse-grid
  "An immutable 2D sparse grid implementation backed by a map.
   Supports infinite dimensions (only stores occupied cells).
   Implements standard Clojure collection interfaces."
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
(def top-left b/top-left)
(def top-right b/top-right)
(def bottom-right b/bottom-right)
(def bottom-left b/bottom-left)
(def corners b/corners)

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
  b/Bounded
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
  "Creates a SparseGrid from a map of {[x y] value}."
  [m]
  (SparseGrid. m (calc-bounds m) nil))

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
