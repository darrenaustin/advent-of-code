;; https://adventofcode.com/2025/day/1
 (ns aoc2025.day01
   (:require
    [aoc.day :as d]
    [aoc.util.string :as s]
    [clojure.string :as str]))

(defn input [] (d/day-input 2025 1))

(defn parse-rotations [input]
  (map (fn [rotation]
         (let [direction (if (str/starts-with? rotation "L") -1 1)
               distance  (s/int rotation)]
           (* direction distance)))
       (s/lines input)))

(defn part1 [input]
  (loop [pos 50 zero-hits 0 rotations (parse-rotations input)]
    (if (empty? rotations)
      zero-hits
      (let [new-pos (mod (+ pos (first rotations)) 100)]
        (recur new-pos
               (if (zero? new-pos)
                 (inc zero-hits)
                 zero-hits)
               (rest rotations))))))

(defn part2 [input]
  (loop [pos 50 zero-crossings 0 rotations (parse-rotations input)]
    (if (empty? rotations)
      zero-crossings
      (let [rotation (first rotations)
            full-rotations (quot (abs rotation) 100)
            remaining (rem rotation 100)
            new-pos (+ pos remaining)
            crossed-zero? (and
                           (not= 0 pos) ;; Didn't start at zero
                           (or
                            (zero? new-pos) ;; Hit zero exactly
                            (and (pos? remaining) (> new-pos 99)) ;; Crossed moving right
                            (and (neg? remaining) (neg? new-pos)))) ;; Crossed moving left
            final-pos (mod new-pos 100)
            crossings (+ full-rotations (if crossed-zero? 1 0))]
        (recur final-pos
               (+ zero-crossings crossings)
               (rest rotations))))))
