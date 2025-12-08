;; https://adventofcode.com/2025/day/8
(ns aoc2025.day08
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.string :as s]
   [clojure.set :as set]
   [clojure.string :as str]))

(defn input [] (d/day-input 2025 8))

(defn parse-boxes [input]
  (mapv s/parse-ints (str/split-lines input)))

(defn sq-distance [[x1 y1 z1] [x2 y2 z2]]
  (let [dx (- x2 x1) dy (- y2 y1) dz (- z2 z1)]
    (+ (* dx dx) (* dy dy) (* dz dz))))

(defn compute-distances [boxes]
  (let [num-boxes (count boxes)]
    (for [b1-idx (range (dec num-boxes))
          b2-idx (range (inc b1-idx) num-boxes)
          :let [b1 (nth boxes b1-idx) b2 (nth boxes b2-idx)]]
      [[b2-idx b1-idx] (sq-distance b1 b2)])))

(defn connect-boxes [circuits [b1 b2]]
  (let [b1-c (circuits b1)
        b2-c (circuits b2)]
    (if (= b1-c b2-c)
      circuits
      (let [new-circuit (set/union b1-c b2-c)]
        (into circuits (map (fn [b] [b new-circuit]) new-circuit))))))

(defn part1
  ([input] (part1 input 1000))
  ([input n]
   (let [boxes (parse-boxes input)
         closest-n (map first (take n (sort-by second (compute-distances boxes))))
         circuits (into {} (map (fn [b] [b #{b}]) (range (count boxes))))]
     (apply * (take 3 (sort (c/by identity c/desc)
                            (map count
                                 (set (vals
                                       (reduce connect-boxes circuits closest-n))))))))))

(defn part2 [input]
  (let [boxes (parse-boxes input)
        closest (map first (sort-by second (compute-distances boxes)))
        circuits (into {} (map (fn [b] [b #{b}]) (range (count boxes))))]
    (loop [circuits circuits, box-pairs closest, [last-b1 last-b2] nil]
      (if (apply = (vals circuits))
        (* (first (nth boxes last-b1)) (first (nth boxes last-b2)))
        (when-let [pair (first box-pairs)]
          (recur (connect-boxes circuits pair) (rest box-pairs) pair))))))
