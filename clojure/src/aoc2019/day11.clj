;; https://adventofcode.com/2019/day/11
(ns aoc2019.day11
  (:require
   [aoc.day :as d]
   [aoc.util.ascii-art :as ascii]
   [aoc.util.pos :as p]
   [aoc.util.sparse-grid :as sg]
   [aoc2019.intcode :as ic]))

(defn input [] (d/day-input 2019 11))

(def black 0)
(def white 1)

(defn- turn-dir [turn dir]
  (case turn
    0 (p/turn-left dir)
    1 (p/turn-right dir)))

(defn- hull-painting [input starting-color]
  (let [program (ic/parse input)
        grid (assoc (sg/make-sparse-grid) p/origin starting-color)
        machine (ic/run program [starting-color] [])]
    (loop [m machine, g grid, pos p/origin, dir p/dir-up]
      (let [{:keys [status output]} m
            [color turn] output
            g' (assoc g pos color)
            dir' (turn-dir turn dir)
            pos' (p/pos+ pos dir')]
        (if (= status :halted)
          g'
          (recur (ic/execute (ic/update-io m [(g' pos' black)] []))
                 g' pos' dir'))))))

(defn part1 [input]
  (count (hull-painting input black)))

(defn part2 [input]
  (ascii/ocr (sg/format-rows (hull-painting input white)) :on \1))
