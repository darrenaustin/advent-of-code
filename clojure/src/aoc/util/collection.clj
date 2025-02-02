(ns aoc.util.collection
  (:import (clojure.lang Util)))

(defn indexed [coll]
  (map-indexed vector coll))

(defn first-where [pred coll]
  (first (filter pred coll)))

(defn count-where [pred coll]
  (count (filter pred coll)))

(defn iteration-with-cycle [iteration f x]
  (loop [x x, iter 0, seen {}]
    (if (contains? seen x)
      (let [offset (seen x), period (- iter offset)
            cycled-iter (+ offset (rem (- iteration offset) period))]
        (first (first-where (fn [[_ v]] (= v cycled-iter)) seen)))
      (recur (f x) (inc iter) (assoc seen x iter)))))

(defn group-by-value [m]
  (reduce (fn [m [k v]] (update m v conj k)) {} m))

(defn val->key [m]
  (into {} (for [[k v] m] [v k])))

(defn transpose [coll]
  (apply mapv vector coll))

(defn flip-horizontal [coll]
  (mapv (comp vec reverse) coll))

(defn rotate-right [coll]
  ((comp flip-horizontal transpose) coll))

;; Allow (sort (by :surname asc :age desc) coll)
;;
;; from: https://www.reddit.com/r/Clojure/comments/ufa8e0/comment/i6s7zt5/?utm_source=share&utm_medium=web3x&utm_name=web3xcss&utm_term=1&utm_content=share_button
(defn asc [a b] (Util/compare a b))
(defn desc [a b] (Util/compare b a))

(defn by [& keys-orderings]
  (fn [a b]
    (loop [[key ordering & keys-orderings] keys-orderings]
      (let [order (ordering (key a) (key b))]
        (if (and (zero? order) keys-orderings)
          (recur keys-orderings)
          order)))))

(defn pad-left [coll n val]
  (let [padding (- n (count coll))]
    (if (pos? padding)
      (concat (repeat padding val) coll)
      coll)))
