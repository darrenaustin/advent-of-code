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
  (map #(vec (s/parse-ints %)) (str/split-lines input)))

(defn neighbors [grid]
  (fn [loc]
    (init-grid
      (filter grid (orthogonal-from loc))
      1)))

(defn part1
  ([input] (part1 input 1024 [70 70]))
  ([input bytes goal]
   (let [grid
         (set/difference (set (for [x (range (inc (first goal)))
                                    y (range (inc (second goal)))]
                                [x y]))
                         (set (take bytes (parse input))))]
     (p/dijkstra-distance [0 0]
                          (neighbors grid)
                          #{goal}))))


(defn part2
  ([input] (part2 input 1024 [70 70]))
  ([input skip-bytes goal]
   (let [[pre-drops drops] (split-at skip-bytes (parse input))
         grid (set/difference (set (for [x (range (inc (first goal)))
                                         y (range (inc (second goal)))]
                                     [x y]))
                              (set pre-drops))]
     (loop [grid grid drops drops]
       (when-let [drop (first drops)]
         (let [grid' (disj grid drop)]
           (if (p/dijkstra-distance [0 0]
                                    (neighbors grid')
                                    #{goal})
             (recur grid' (rest drops))
             (str/join "," drop))))))))
