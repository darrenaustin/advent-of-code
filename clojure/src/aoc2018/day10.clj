;; https://adventofcode.com/2018/day/10
 (ns aoc2018.day10
   (:require
    [aoc.day :as d]
    [aoc.util.ascii-art :as ascii-art]
    [aoc.util.grid :as g]
    [aoc.util.pos :as p]
    [aoc.util.string :as s]))

(defn input [] (d/day-input 2018 10))

(defn parse-stars [input]
  (map #(let [[x y vx vy] (s/ints %)]
          [[x y] [vx vy]])
       (s/lines input)))

(defn advance-stars
  ([stars] (advance-stars stars 1))
  ([stars time] (map (fn [[p v]] [(p/pos+ p (p/pos* time v)) v]) stars)))

(defn star-grid [stars]
  (into (g/->sparse-grid) (map (fn [[p _]] [p \#]) stars)))

;; The stars are all moving at a constant velocity. Assuming every
;; star makes up part of the message, they would have to local minimum
;; height at the point of the message. This calculates how much
;; time it will take to reach that minimum based on the relative
;; heights of the first two grids.
(defn time-for-message [stars]
  (let [height (dec (g/height (star-grid stars)))
        height' (dec (g/height (star-grid (advance-stars stars))))
        delta (- height height')]
    (int (/ height delta))))

(defn message [input]
  (let [stars (parse-stars input)
        time (time-for-message stars)]
    (g/format-rows (star-grid (advance-stars stars time)))))

(defn part1 [input]
  (ascii-art/ocr (message input)))

(defn part2 [input] (time-for-message (parse-stars input)))
