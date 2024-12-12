(ns aoc.util.collection)

(defn transpose [coll]
  (apply map vector coll))

(defn indexed [coll]
  (mapv vector (range (count coll)) coll))

(defn group-by-value [m]
  (reduce (fn [m [k v]] (update m v conj k)) {} m))
