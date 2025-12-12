(ns aoc.util.pathfinding
  (:require
   [aoc.util.math :as m]
   [clojure.data.priority-map :refer [priority-map priority-map-keyfn]]))

;; Modified from a blog post with an elegant Dijkstra implementation:
;; https://ummels.de/2014/06/08/dijkstra-in-clojure/

(defn- map-vals [m f]
  (into {} (for [[k v] m] [k (f v)])))

(defn- remove-keys [m pred]
  (select-keys m (remove pred (keys m))))

(defn dijkstra-distance [start neighbors-fn goal-fn?]
  (loop [q (priority-map start 0) r {}]
    (when-let [[v d] (peek q)]
      (if (goal-fn? v)
        d
        (let [dist (-> (neighbors-fn v) (remove-keys r) (map-vals (partial + d)))]
          (recur (merge-with min (pop q) dist) (assoc r v d)))))))

(defn- min-paths [a b]
  (let [[a-d a-paths] a [b-d b-paths] b]
    (case (compare a-d b-d)
      -1 [a-d a-paths]
      0 [a-d (concat a-paths b-paths)]
      1 [b-d b-paths])))

(defn dijkstra-paths-map [start neighbors-fn]
  (loop [q (priority-map-keyfn first start [0 []]) r {}]
    (if-let [[v [d ps]] (peek q)]
      (let [dist (-> (neighbors-fn v)
                     (remove-keys r)
                     (map-vals (fn [d'] [(+ d d') [v]])))]
        (recur (merge-with min-paths (pop q) dist) (assoc r v [d ps])))
      r)))

(defn- paths-from [paths-map loc start]
  (if (= loc start)
    [[start]]
    (let [min-locs (second (paths-map loc))]
      (map #(conj % loc) (mapcat #(paths-from paths-map % start) min-locs)))))

(defn dijkstra-paths [start neighbors-fn goal-fn?]
  (let [paths-map (dijkstra-paths-map start neighbors-fn)
        goals     (filter goal-fn? (keys paths-map))
        min-goals (second (m/mins-by #(first (paths-map %)) goals))]
    (mapcat #(paths-from paths-map % start) min-goals)))
