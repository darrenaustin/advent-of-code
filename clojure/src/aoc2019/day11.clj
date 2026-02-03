;; https://adventofcode.com/2019/day/11
(ns aoc2019.day11
  (:require
   [aoc.day :as d]
   [aoc.util.ascii-art :as ascii]
   [aoc.util.grid :as g]
   [aoc.util.pos :as p]
   [aoc2019.intcode :as ic]))

(defn input [] (d/day-input 2019 11))

(def ^:private black 0)
(def ^:private white 1)

(defn- turn-dir [turn dir]
  (case turn
    0 (p/turn-left dir)
    1 (p/turn-right dir)))

(defn- hull-painting [input starting-color]
  (let [program (ic/parse-program input)
        grid (assoc (g/->sparse-grid) p/origin starting-color)
        machine (ic/run program [starting-color] [])]
    (loop [m machine, g grid, pos p/origin, dir p/dir-up]
      (if (= (:status m) :halted)
        g
        (let [[color turn] (:output m)
              g'   (assoc g pos color)
              dir' (turn-dir turn dir)
              pos' (p/pos+ pos dir')]
          (recur (ic/execute (ic/update-io m [(g' pos' black)] []))
                 g' pos' dir'))))))

(defn part1 [input]
  (count (hull-painting input black)))

(defn part2 [input]
  (ascii/ocr (g/format-rows (hull-painting input white)) :on \1))
