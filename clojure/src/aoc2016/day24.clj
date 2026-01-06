;; https://adventofcode.com/2016/day/24
   (ns aoc2016.day24
     (:require
      [aoc.day :as d]
      [aoc.util.collection :as c]
      [aoc.util.pos :as p]
      [aoc.util.string :as s]
      [clojure.math.combinatorics :as combo]))

(defn input [] (d/day-input 2016 24))

(defn- parse-grid [input]
  (apply concat
         (map-indexed (fn [y line]
                        (map-indexed (fn [x c] [[x y] c]) line))
                      (s/lines input))))

(defn- parse-ducts [input]
  (reduce (fn [ducts [pos chr]]
            (if (= chr \#)
              ducts
              (let [ducts' (update ducts :valid-pos conj pos)]
                (cond
                  (= chr \0) (assoc ducts' :start pos)
                  (Character/isDigit chr) (update ducts' :goals conj pos)
                  :else ducts'))))
          {:goals [], :valid-pos #{}}
          (parse-grid input)))

(defn- distance-between [ducts from to]
  (loop [q (into c/empty-queue [[from 0]])
         visited #{from}]
    (when-let [[current dist] (peek q)]
      (if (= current to)
        dist
        (let [steps (->> p/orthogonal-dirs
                         (map (partial p/pos+ current))
                         (filter (:valid-pos ducts))
                         (remove visited))]
          (recur (into (pop q) (map (fn [s] [s (inc dist)]) steps))
                 (into visited steps)))))))

(defn- node-distances [ducts]
  (reduce (fn [m [n1 n2]]
            (let [distance (distance-between ducts n1 n2)]
              (assoc m [n1 n2] distance [n2 n1] distance)))
          {}
          (combo/combinations (conj (:goals ducts) (:start ducts)) 2)))

(defn- min-steps [ducts & {:keys [round-trip?] :or {round-trip? false}}]
  (let [{:keys [start goals]} ducts
        distances (node-distances ducts)
        path-dist (fn [path]
                    (->> (partition 2 1 path)
                         (map distances)
                         (apply +)))
        paths (map (fn [p] (concat [start] p (when round-trip? [start])))
                   (combo/permutations goals))]
    (apply min (map path-dist paths))))

(defn part1 [input] (min-steps (parse-ducts input)))

(defn part2 [input] (min-steps (parse-ducts input) :round-trip? true))
