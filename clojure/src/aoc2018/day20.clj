;; https://adventofcode.com/2018/day/20
(ns aoc2018.day20
  (:require [aoc.day :as d]
            [aoc.util.collection :as c]
            [aoc.util.grid :refer :all]
            [aoc.util.pathfinding :as p]
            [aoc.util.vec :refer :all]))

(defn input [] (d/day-input 2018 20))

(def dirs {\N dir-n, \E dir-e, \S dir-s, \W dir-w})

(defn parse [input]
  (loop [stack '(), pos [0 0], doors {}, xs input]
    (if (empty? xs)
      (into {} (for [[k v] doors] [k (set v)]))
      (case (first xs)
        \^ (recur stack pos doors (rest xs))
        \$ (recur stack pos doors (rest xs))
        \( (recur (conj stack pos) pos doors (rest xs))
        \| (recur stack (peek stack) doors (rest xs))
        \) (recur (pop stack) (peek stack) doors (rest xs))
        (let [pos' (vec+ pos (dirs (first xs)))]
          (recur stack pos'
                 (-> doors
                     (update pos conj pos')
                     (update pos' conj pos))
                 (rest xs)))))))

(defn neighbors [doors pos]
  (into {} (for [n (doors pos)] [n 1])))

(defn distances-from [doors node]
  (map (fn [[_ [d _]]] d) (p/dijkstra-paths-map node (partial neighbors doors))))

(defn part1 [input]
  (apply max (distances-from (parse input) [0 0])))

(defn part2 [input]
  (c/count-where #(<= 1000 %) (distances-from (parse input) [0 0])))
