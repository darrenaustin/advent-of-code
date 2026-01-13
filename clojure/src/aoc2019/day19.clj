;; https://adventofcode.com/2019/day/19
 (ns aoc2019.day19
   (:require
    [aoc.day :as d]
    [aoc.util.collection :as c]
    [aoc2019.intcode :as ic]))

(defn input [] (d/day-input 2019 19))

(defn move [drone x y]
  (first (:output (ic/run drone [x y] nil))))

(defn part1 [input]
  (let [drone (ic/parse-program input)]
    (reduce + (for [x (range 50) y (range 50)]
                (move drone x y)))))

(defn part2 [input]
  ;; After manually inspecting the output of the drone program,
  ;; the tractor beam is between two lines (like in the example),
  ;; so we can determine the slope of both lines by sampling
  ;; points in each axis for the first 1. That gives us the
  ;; intercepts of the lines, which gives us the slopes,
  ;; which we can then use to compute the upper-left corner of
  ;; the needed square.
  (let [drone (ic/parse-program input)
        sleigh (dec 100)
        sample 1000
        ;; lower (top) bound intercept
        [lx ly] (first (c/first-where #(= 1 (second %))
                                      (for [y (range)]
                                        [[sample y] (move drone sample y)])))
        ;; upper (bottom) bound intercept
        [ux uy] (first (c/first-where #(= 1 (second %))
                                      (for [x (range)]
                                        [[x sample] (move drone x sample)])))
        lower-slope (/ ly lx)
        upper-slope (/ uy ux)
        x (/ (* sleigh (inc lower-slope)) (- upper-slope lower-slope))
        y (/ (* sleigh lower-slope (inc upper-slope)) (- upper-slope lower-slope))]
    (+ (* (int x) 10000) (int y))))

