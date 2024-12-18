;; https://adventofcode.com/2024/day/18
(ns aoc2024.day18
  (:require
    [aoc.day :as d]
    [aoc.util.grid :refer :all]
    [aoc.util.pathfinding :as p]
    [aoc.util.string :as s]
    [clojure.set :as set]
    [clojure.string :as str]))

(def input (d/day-input 2024 18))

(defn parse [input]
  (mapv #(vec (s/parse-ints %)) (str/split-lines input)))

(defn neighbors [grid]
  (fn [loc]
    (init-grid
      (filter grid (orthogonal-from loc))
      1)))

(defn min-distance [grid bytes goal]
  (let [grid' (set/difference grid (set bytes))]
    (p/dijkstra-distance [0 0]
                         (neighbors grid')
                         #{goal})))

(defn part1
  ([input] (part1 input 1024 [70 70]))
  ([input bytes goal]
   (let [grid (set (for [x (range (inc (first goal)))
                       y (range (inc (second goal)))]
                   [x y]))]
     (min-distance grid (take bytes (parse input)) goal))))


(defn part2
  ([input] (part2 input 1024 [70 70]))
  ([input bytes goal]
   (let [grid (set (for [x (range (inc (first goal)))
                         y (range (inc (second goal)))]
                     [x y]))
         drops (parse input)]
      (loop [low bytes high (inc (count drops))]
        (if (< low high)
          (let [mid (quot (+ low high) 2)]
            (if (min-distance grid (take (inc mid) drops) goal)
              (recur (inc mid) high)
              (recur low mid)))
          (str/join "," (nth drops low)))))))
