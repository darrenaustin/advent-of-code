(ns aoc.util.collection)

(defn transpose [coll]
  (apply map vector coll))

(defn indexed [coll]
  (map-indexed vector coll))

(defn group-by-value [m]
  (reduce (fn [m [k v]] (update m v conj k)) {} m))

(defn val->key [m]
  (into {} (for [[k v] m] [v k])))
