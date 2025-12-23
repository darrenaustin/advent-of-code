(ns aoc.util.collection
  (:import
   (clojure.lang Util)))

(defn first-where
  "Returns the first element in the collection that satisfies the predicate.
   Returns nil if no element matches."
  [pred coll]
  (first (filter pred coll)))

(defn count-where
  "Returns the number of elements in the collection that satisfy the predicate."
  [pred coll]
  (count (filter pred coll)))

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

(defn keys-when-val [pred m]
  (map first (filter (fn [[_ val]] (pred val)) m)))

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
  (loop [idx 0 [e & more] coll]
    (if (= e value)
      idx
      (when more
        (recur (inc idx) more)))))

(defn first-duplicate
  "Returns the first element in the collection that has appeared previously.
   Returns nil if no duplicates are found.
   Example: (first-duplicate [1 2 3 2 4]) => 2"
  [coll]
  (loop [[x & xs] coll, seen #{}]
    (when x
      (if (seen x)
        x
        (recur xs (conj seen x))))))

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
  (loop [x x, iter 0, seen {}]
    (if (contains? seen x)
      (let [offset (seen x), period (- iter offset)
            cycled-iter (+ offset (rem (- iteration offset) period))]
        (first (first-where (fn [[_ v]] (= v cycled-iter)) seen)))
      (recur (f x) (inc iter) (assoc seen x iter)))))

;; Allow (sort (by :surname asc :age desc) coll)
;;
;; from: https://www.reddit.com/r/Clojure/comments/ufa8e0/comment/i6s7zt5
(defn asc
  "Ascending comparison function for use with sort and by.
   Returns negative, zero, or positive based on comparison of a and b."
  [a b]
  (Util/compare a b))

(defn desc
  "Descending comparison function for use with sort and by.
   Returns positive, zero, or negative based on comparison of a and b."
  [a b]
  (Util/compare b a))

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
