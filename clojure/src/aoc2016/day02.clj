;; https://adventofcode.com/2016/day/2
 (ns aoc2016.day02
   (:require
    [aoc.day :as d]
    [aoc.util.grid :as g]
    [aoc.util.pos :as p]
    [aoc.util.string :as s]
    [clojure.string :as str]))

(defn input [] (d/day-input 2016 2))

(def nine-pad
  (g/->sparse-grid
   ["123"
    "456"
    "789"]))

(def diamond-pad
  (g/->sparse-grid
   [[nil nil "1" nil nil]
    [nil "2" "3" "4" nil]
    ["5" "6" "7" "8" "9"]
    [nil "A" "B" "C" nil]
    [nil nil "D" nil nil]]))

(defn bathroom-code [input keypad start]
  (letfn [(step [pos dir]
            (let [next-pos (p/pos+ pos (p/dir->offset dir))]
              (if (keypad next-pos) next-pos pos)))
          (move [pos dirs] (reduce step pos dirs))]
    (->> (s/lines input)
         (reductions move start)
         rest
         (map keypad)
         str/join)))

(defn part1 [input] (bathroom-code input nine-pad [1 1]))

(defn part2 [input] (bathroom-code input diamond-pad [0 2]))
