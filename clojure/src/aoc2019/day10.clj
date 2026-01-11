;; https://adventofcode.com/2019/day/10
 (ns aoc2019.day10
   (:require
    [aoc.day :as d]
    [aoc.util.math :as m]
    [aoc.util.matrix :as mat]
    [aoc.util.pos :as p]
    [aoc.util.string :as s]
    [clojure.math :as math]))

(defn input [] (d/day-input 2019 10))

(defn- parse-field [input]
  (let [lines (s/lines input)
        width (count (first lines))
        height (count lines)]
    (set (for [x (range width) y (range height)
               :let [cell (nth (nth lines y) x)]
               :when (= cell \#)]
           [x y]))))

(defn- clockwise-up-angle [p1 p2]
  (let [[x y] (p/pos- p2 p1)]
    (mod (math/to-degrees (math/atan2 x (- y))) 360.0)))

(defn- num-detected-from [field pos]
  (->> (disj field pos)
       (map #(clockwise-up-angle pos %))
       set
       count))

(defn- nuke-asteroids [field station]
  (let [targets (disj field station)
        by-angle (group-by #(clockwise-up-angle station %) targets)
        sorted-groups (->> (sort-by key by-angle)
                           (map val)
                           (map #(sort-by (partial p/distance-sq station) %)))]
    (->> (mat/transpose sorted-groups nil)
         (apply concat)
         (remove nil?))))

(defn part1 [input]
  (let [field (parse-field input)]
    (m/max-of #(num-detected-from field %) field)))

(defn part2 [input]
  (let [field (parse-field input)
        station (m/max-by #(num-detected-from field %) field)
        [ax ay] (nth (nuke-asteroids field station) 199)]
    (+ (* 100 ax) ay)))
