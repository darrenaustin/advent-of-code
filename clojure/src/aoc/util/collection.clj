(ns aoc.util.collection)

(defn transpose [coll]
  (apply map vector coll))

(defn indexed [coll]
  (map-indexed vector coll))

(defn find-where [pred coll]
  (first (filter pred coll)))

(defn count-of [pred coll]
  (count (filter pred coll)))

(defn group-by-value [m]
  (reduce (fn [m [k v]] (update m v conj k)) {} m))

(defn val->key [m]
  (into {} (for [[k v] m] [v k])))
