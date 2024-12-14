;; https://adventofcode.com/2024/day/14
(ns aoc2024.day14
  (:require
    [aoc.util.collection :as c]
    [aoc.day :as d]
    [aoc.util.grid :refer :all]
    [aoc.util.string :as s]))

(def input (d/day-input 2024 14))

(defn parse-robots [input]
  (reduce (fn [[pos vel] [p v]] [(conj pos p) (conj vel v)])
          [[] []]
          (partition 2 (partition 2 (s/parse-full-nums input)))))

(defn move [bounds pos vel]
  (mapv mod (vec+ pos vel) bounds))

(defn simulate [bounds velocities robots]
  (map (partial move bounds) robots velocities))

(defn seconds-until-tree [[robots velocities] bounds]
  ;; It appears the best way to check this is to see
  ;; if no robots are at the same location.
  (loop [robots robots seconds 0]
    (if (not= (count robots) (count (set robots)))
      (recur (simulate bounds velocities robots)
             (inc seconds))
      seconds)))

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
     (safety (nth (iterate (partial simulate bounds velocities) robots) 100)
             (quadrants bounds)))))

(defn part2
  ([input] (part2 input [101 103]))
  ([input bounds]
   (seconds-until-tree (parse-robots input) bounds)))
