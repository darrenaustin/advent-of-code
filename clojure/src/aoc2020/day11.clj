;; https://adventofcode.com/2020/day/11
(ns aoc2020.day11
  (:require
   [aoc.day :as d]
   [aoc.util.collection :refer [count-where first-where]]
   [aoc.util.grid :as g]
   [aoc.util.pos :as p]))

(defn input [] (d/day-input 2020 11))

(defn- seat-graph [grid neighbor-fn]
  (let [seats (map key (filter #(not= (val %) \.) grid))]
    (reduce (fn [g pos]
              (assoc g pos (neighbor-fn grid pos)))
            {}
            seats)))

(defn- adjacent-seats [grid pos]
  (filter #(not= \. (grid %)) (p/adjacent-to pos)))

(defn- visible-seat [grid pos dir]
  (loop [p (p/pos+ pos dir)]
    (let [v (grid p)]
      (cond
        (= v \.) (recur (p/pos+ p dir))
        (nil? v) nil
        :else p))))

(defn- visible-seats [grid pos]
  (keep #(visible-seat grid pos %) p/adjacent-dirs))

(defn- next-round [seats crowd occupied]
  (reduce-kv (fn [occupied' seat neighbors]
               (let [n (count-where occupied neighbors)]
                 (if (occupied seat)
                   (if (>= n crowd) occupied' (conj occupied' seat))
                   (if (zero? n) (conj occupied' seat) occupied'))))
             #{}
             seats))

(defn- stable-seats [input neighbor-fn crowd]
  (let [grid (g/str->grid input)
        seats (seat-graph grid neighbor-fn)
        occupied (into #{} (keep (fn [[pos v]] (when (= v \#) pos))) grid)]
    (->> (iterate #(next-round seats crowd %) occupied)
         (partition 2 1)
         (first-where (partial apply =))
         first
         count)))

(defn part1 [input] (stable-seats input adjacent-seats 4))

(defn part2 [input] (stable-seats input visible-seats 5))
