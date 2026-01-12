;; https://adventofcode.com/2016/day/13
(ns aoc2016.day13
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.math :as m]
   [aoc.util.pathfinding :as path]
   [aoc.util.pos :as p]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2016 13))

(defn- cell-value [[x y] n]
  (let [val (+ n (* x x) (* 3 x) (* 2 x y) y (* y y))
        bits (Long/bitCount val)]
    (if (even? bits) \. \#)))

(defn- neighbors-fn [n]
  (fn [pos]
    (for [[nx ny :as npos] (p/orthogonal-to pos)
          :when (and (not (neg? nx))
                     (not (neg? ny))
                     (= \. (cell-value npos n)))]
      npos)))

(defn- distance-fn [destination]
  (fn [pos]
    (m/manhattan-distance pos destination)))

(defn part1
  ([input] (part1 input [31 39]))
  ([input dest]
   (let [n (s/int input)]
     (path/a-star-cost [1 1]
                       (neighbors-fn n)
                       #{dest}
                       :heuristic (distance-fn dest)))))

(defn part2 [input]
  (let [neighbors (neighbors-fn (s/int input))]
    (loop [q (c/queue [[1 1] 0])
           visited #{[1 1]}]
      (if (empty? q)
        (count visited)
        (let [[current dist] (peek q)
              q (pop q)]
          (if (>= dist 50)
            (recur q visited)
            (let [next-steps (remove visited (neighbors current))
                  visited' (into visited next-steps)
                  q' (into q (map vector next-steps (repeat (inc dist))))]
              (recur q' visited'))))))))
