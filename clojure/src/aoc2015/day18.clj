;; https://adventofcode.com/2015/day/18
 (ns aoc2015.day18
   (:require
    [aoc.day :as d]
    [aoc.util.collection :as c]
    [aoc.util.grid :as g]
    [aoc.util.pos :as p]))

(defn input [] (d/day-input 2015 18))

(defn parse-grid [input]
  (g/str->grid input {\. false \# true}))

(defn neighbors-on [grid pos]
  (count (filter grid (p/adjacent-to pos))))

(defn conway [on? num-neighbors-on]
  (if on?
    (or (= num-neighbors-on 2) (= num-neighbors-on 3))
    (= num-neighbors-on 3)))

(defn step-grid
  ([grid]
   (g/update-grid grid (fn [[pos on?]]
                         (conway on? (neighbors-on grid pos))))))

(defn corners-on [grid]
  (reduce #(assoc %1 %2 true) grid (g/corners grid)))

(defn lights-on [grid step-fn steps]
  (count (filter second (c/nth-iteration step-fn grid steps))))

(defn part1
  ([input] (part1 input 100))
  ([input steps] (lights-on (parse-grid input) step-grid steps)))

(defn part2
  ([input] (part2 input 100))
  ([input steps]
   (lights-on (corners-on (parse-grid input))
              (comp corners-on step-grid)
              steps)))
