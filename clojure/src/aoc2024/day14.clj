;; https://adventofcode.com/2024/day/14
(ns aoc2024.day14
  (:require
    [aoc.day :as d]
    [aoc.util.grid :refer :all]
    [aoc.util.string :as s]))

(def input (d/day-input 2024 14))

(defn parse-robots [input]
  (reduce (fn [[pos vel] [p v]] [(conj pos p) (conj vel v)])
          [[] []]
          (partition 2 (partition 2 (s/parse-ints input)))))

(defn simulate [robots velocities bounds seconds]
  (map (fn [r v]
         (mapv mod (vec+ r (vec-n* seconds v)) bounds))
       robots velocities))

(defn seconds-until-tree [[robots velocities] bounds]
  ;; It appears the best way to check this is to see
  ;; if no robots are at the same location.
  (first (filter
           (fn [sec] (let [locs (simulate robots velocities bounds sec)]
                     (= (count locs) (count (set locs)))))
           (range (apply * bounds)))))

(defn quadrants [bounds]
  (let [[bx by] bounds
        [mx my] (mapv #(quot % 2) bounds)]
    [[[-1 -1] [mx my]]
     [[mx -1] [bx my]]
     [[-1 my] [mx by]]
     [[mx my] [bx by]]]))

(defn robot-quadrant [robot quadrants]
  (first (filter (fn [quadrant] (in-bounds? quadrant robot)) quadrants)))

(defn safety [robots quadrants]
  (apply * (vals (frequencies (keep #(robot-quadrant % quadrants) robots)))))

(defn part1
  ([input] (part1 input [101 103]))
  ([input bounds]
   (let [[robots velocities] (parse-robots input)]
     (safety (simulate robots velocities bounds 100)
             (quadrants bounds)))))

(defn part2
  ([input] (part2 input [101 103]))
  ([input bounds]
   (seconds-until-tree (parse-robots input) bounds)))
