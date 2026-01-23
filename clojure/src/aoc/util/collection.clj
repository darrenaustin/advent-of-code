(ns aoc.util.collection
  "Utility functions for working with collections, including sequences and maps."
  (:import
   [clojure.lang PersistentQueue]))

(def empty-queue
  "An empty PersistentQueue instance, useful as a starting point for `conj` operations."
  PersistentQueue/EMPTY)

(defn queue
  "Returns a PersistentQueue containing the optional items.
   PersistentQueue supports efficient adding to the rear (conj) and removing from the front (pop/peek).
   Example: (queue 1 2 3) => <-(1 2 3)-<"
  [& xs]
  (into PersistentQueue/EMPTY xs))

(defn first-where
  "Returns the first element in the collection that satisfies the predicate.
   Returns nil if no element matches."
  {:inline (fn [pred coll] `(first (filter ~pred ~coll)))}
  [pred coll]
  (first (filter pred coll)))

(defn count-where
  "Returns the number of elements in the collection that satisfy the predicate."
  {:inline (fn [pred coll] `(count (filter ~pred ~coll)))}
  [pred coll]
  (count (filter pred coll)))

;; Snagged from medley.core:
;; https://github.com/weavejester/medley/blob/1.9.0/src/medley/core.cljc#L412
(defn take-upto
  "Returns a sequence of items from coll as long as (pred item) returns true.
   Also includes the first item for which (pred item) returns false.
   Returns a transducer when no collection is provided."
  ([pred]
   (fn [rf]
     (fn
       ([] (rf))
       ([result] (rf result))
       ([result input]
        (if (pred input)
          (rf result input)
          (ensure-reduced (rf result input)))))))
  ([pred coll]
   (lazy-seq
    (when-let [s (seq coll)]
      (let [x (first s)]
        (if (pred x)
          (cons x (take-upto pred (rest s)))
          (list x)))))))

;; Snagged from medley.core:
;; https://github.com/weavejester/medley/blob/1.9.0/src/medley/core.cljc#L432
(defn drop-upto
  "Returns a sequence of items starting after the first item for which
   (pred item) returns true.
   Returns a transducer when no collection is provided."
  ([pred]
   (fn [rf]
     (let [dropping? (volatile! true)]
       (fn
         ([] (rf))
         ([result] (rf result))
         ([result input]
          (if @dropping?
            (if (pred input)
              (do (vreset! dropping? false) result)
              result)
            (rf result input)))))))
  ([pred coll]
   (rest (drop-while (complement pred) coll))))

(defn nth>>
  "Returns the nth item of the collection. Flipped version of `nth`, useful for threading macros.
   Example: (->> [1 2 3] (nth>> 1)) => 2"
  [n coll]
  (nth coll n))

(defn split
  "Splits a collection into a lazy sequence of sequences, using the predicate
   `seperator-fn` to identify delimiters. Delimiters are not included in the
   result. Empty segments are omitted.

   Example:
     (split zero? [1 2 0 3 4]) => ((1 2) (3 4))
     (split zero? [1 2 0 0 3 4]) => ((1 2) (3 4))
     (split odd? [1 2 0 0 3 4]) => ((2 0 0) (4))"
  [seperator-fn coll]
  (let [coll (drop-while seperator-fn coll)]
    (when (seq coll)
      (let [not-seperator-fn (complement seperator-fn)
            chunk (take-while not-seperator-fn coll)
            remaining (drop-while not-seperator-fn coll)]
        (lazy-seq
         (cons chunk (split seperator-fn remaining)))))))

(defn partition-starting [pred coll]
  (let [indices (rest (reductions (fn [n x] (if (pred x) (inc n) n)) 0 coll))]
    (->> (map vector indices coll)
         (partition-by first)
         (mapv (partial mapv second)))))

(defn group-by-value
  "Groups keys by their values in a map.
   Example: (group-by-value {:a 1 :b 2 :c 1}) => {1 [:a :c], 2 [:b]}"
  [m]
  (reduce (fn [m [k v]] (update m v conj k)) {} m))

(defn vals->keys
  "Inverts a map, swapping keys and values.
   Example: (val->key {:a 1 :b 2}) => {1 :a, 2 :b}"
  [m]
  (into {} (for [[k v] m] [v k])))

(defn keys-when-val
  "Returns a sequence of keys in map `m` where the value satisfies `pred`."
  [pred m]
  (keep (fn [[k v]] (when (pred v) k)) m))

(defn filter-map
  "Filters a map by retaining only entries that satisfy the predicate `pred`.
   `pred` should be a function of one argument (a MapEntry or vector [k v]).
   Returns a new map, or a transducer if `m` is not provided."
  ([pred] (filter pred))
  ([pred m] (into {} (filter pred) m)))

(defn filter-keys
  "Filters a map by retaining only entries where the key satisfies the predicate `pred`.
   Returns a new map, or a transducer if `m` is not provided."
  ([pred] (filter (comp pred key)))
  ([pred m] (into {} (filter (comp pred key)) m)))

(defn filter-vals
  "Filters a map by retaining only entries where the value satisfies the predicate `pred`.
   Returns a new map, or a transducer if `m` is not provided."
  ([pred] (filter (comp pred val)))
  ([pred m] (into {} (filter (comp pred val)) m)))

(defn map-by
  "Returns a map where the keys are the result of applying `f` to each element of `coll`,
   and the values are the elements themselves."
  [f coll]
  (into {} (map (fn [e] [(f e) e])) coll))

(defn map-accum
  "Applies a function f to an accumulator and the elements of a collection.
   f should be a function of [acc x] returning [new-acc new-x].
   Returns a vector [final-acc result-coll].

   The result-coll is realized eagerly."
  [f init coll]
  (reduce (fn [[acc result] x]
            (let [[new-acc new-x] (f acc x)]
              [new-acc (conj result new-x)]))
          [init []]
          coll))

(defn dissoc-in
  "Dissociates a value in a nested map structure, similar to `assoc-in` but for removal.
   If the path doesn't exist, the map is returned unchanged."
  [m [k & ks]]
  (if ks
    (update m k dissoc-in ks)
    (dissoc m k)))

(defn indexed
  "Returns a sequence of [index value] pairs from the collection.
   Example: (indexed [:a :b :c]) => ([0 :a] [1 :b] [2 :c])"
  [coll]
  (map-indexed vector coll))

(defn index
  "Returns the index of the first occurrence of `value` in `coll`.
   Returns nil if not found.
   Example: (index [:a :b :c] :b) => 1"
  [coll value]
  (first (keep-indexed (fn [i x] (when (= x value) i)) coll)))

(defn indexes-by
  "Returns a sequence of indices of elements in `coll` that satisfy the predicate `f`."
  [f coll]
  (keep-indexed (fn [i x] (when (f x) i)) coll))

(defn first-duplicate
  "Returns the first element in the collection that has appeared previously.
   Returns nil if no duplicates are found.
   Example: (first-duplicate [1 2 3 2 4]) => 2"
  [coll]
  (loop [s (seq coll), seen #{}]
    (when s
      (let [x (first s)]
        (if (seen x)
          x
          (recur (next s) (conj seen x)))))))

(defn rotate-left
  "Returns a seq with the elements of the collection rotated to the
   left by n places, wrapping around to the beginning."
  [n xs]
  (let [split-at (mod n (count xs))]
    (concat (drop split-at xs) (take split-at xs))))

(defn rotate-right
  "Returns a seq with the elements of the collection rotated to the
  right by n places, wrapping around to the end."
  [n xs]
  (rotate-left (- n) xs))

(defn pairs
  "Returns a lazy sequence of all unique pairs (combinations of size 2) from the collection.
   Example: (pairs [1 2 3]) => ([1 2] [1 3] [2 3])"
  [coll]
  (lazy-seq
   (when-let [s (seq coll)]
     (let [h (first s)
           t (rest s)]
       (concat (map vector (repeat h) t)
               (pairs t))))))

(defn adjacent-pairs
  "Returns a lazy sequence of adjacent pair elements of the collection.
   Example: (adjacent-pairs [1 2 3]) => ((1 2) (2 3))"
  [coll]
  (partition 2 1 coll))

(defn cyclic-adjacent-pairs
  "Returns a lazy sequence of adjacent pairs elements of the collection,
   treating the collection as circular.
   Example: (adjacent-pairs-cyclic [1 2 3]) => ((1 2) (2 3) (3 1))"
  [coll]
  (partition 2 1 coll coll))

(defn nth-iteration
  "Applies function f to x exactly n times and returns the result.
   Example: (iterate-n inc 5 3) => 8"
  [f x n]
  (loop [x x, i 0]
    (if (< i n)
      (recur (f x) (inc i))
      x)))

(defn iteration-with-cycle
  "Efficiently computes the result of applying function f to x for a given iteration number,
   detecting cycles to avoid computing all intermediate values.
   Useful for problems involving large iteration counts with repeating patterns."
  [iteration f x]
  (loop [x x, iter 0, seen {}, history []]
    (if-let [offset (seen x)]
      (let [period (- iter offset)
            remaining (- iteration offset)
            cycled-iter (+ offset (mod remaining period))]
        (nth history cycled-iter))
      (recur (f x) (inc iter) (assoc seen x iter) (conj history x)))))

;; Allow (sort (by :surname asc :age desc) coll)
;;
;; from: https://www.reddit.com/r/Clojure/comments/ufa8e0/comment/i6s7zt5
(defn asc
  "Ascending comparison function for use with sort and by.
   Returns negative, zero, or positive based on comparison of a and b."
  [a b]
  (compare a b))

(defn desc
  "Descending comparison function for use with sort and by.
   Returns positive, zero, or negative based on comparison of a and b."
  [a b]
  (compare b a))

(defn by
  "Creates a comparator function for multi-key sorting.
   Takes alternating key functions and ordering functions (asc/desc).
   Example: (sort (by :surname asc :age desc) people)"
  [& keys-orderings]
  (fn [a b]
    (loop [[key ordering & keys-orderings] keys-orderings]
      (let [order (ordering (key a) (key b))]
        (if (and (zero? order) keys-orderings)
          (recur keys-orderings)
          order)))))

(defn pad-left
  "Pads a collection on the left with the given value until it reaches length n.
   If the collection is already n or longer, returns it unchanged.
   Example: (pad-left [1 2 3] 5 0) => (0 0 1 2 3)"
  [coll n val]
  (let [padding (- n (count coll))]
    (if (pos? padding)
      (concat (repeat padding val) coll)
      coll)))
