(ns aoc.util.collection)

(defn indexed [coll]
  (map-indexed vector coll))

(defn first-where [pred coll]
  (first (filter pred coll)))

(defn count-where [pred coll]
  (count (filter pred coll)))

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

(defn x-nth [n coll] (nth coll n))
