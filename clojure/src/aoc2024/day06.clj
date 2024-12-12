;; https://adventofcode.com/2024/day/6
(ns aoc2024.day06
  (:require
   [aoc.day :as d]
   [aoc.util.grid :refer :all]))

(def input (d/day-input 2024 6))

(def right-turn
  {dir-up dir-right
   dir-right dir-down
   dir-down dir-left
   dir-left dir-up})

(defn walk-guard [grid guard]
  (loop [guard guard dir dir-up visited #{guard}]
    (let [next-loc (vec+ guard dir)]
      (cond
        ;; Walked off edge
        (not (contains? grid next-loc)) visited

        ;; Blocked, so turn right
        (= \# (grid next-loc)) (recur guard (right-turn dir) visited)

        ;; Walk to the next position and add the current to visited
        :else (recur next-loc dir (conj visited next-loc))))))

(defn looping-guard? [grid guard]
  (loop [guard guard dir dir-up visited #{}]
    (if (contains? visited [guard dir])
      true
      (let [next-loc (vec+ guard dir)]
        (cond
          ;; Walked off edge.
          (not (contains? grid next-loc)) false

          ;; Blocked, so turn right
          (= \# (grid next-loc)) (recur guard (right-turn dir) visited)

          ;; Walk to the next position and add the current to visited
          :else (recur next-loc dir (conj visited [guard dir])))))))

(defn place-obstruction [grid loc]
  (assoc grid loc \#))

(defn guard-location [grid]
  (loc-where grid (partial = \^)))

(defn part1 [input]
  (let [grid (parse-grid input)
        guard (guard-location grid)]
    (count (walk-guard grid guard))))

;; TODO: this is *way* too slow.
;; Big performance suggestion from reddit discussion:
;; https://www.reddit.com/r/adventofcode/comments/1h7tovg/2024_day_6_solutions/
;; Only need to put obstacles in the path from part 1 as the guard
;; won't run into the others.
(defn part2 [input]
  (let [grid (parse-grid input)
        guard (guard-location grid)
        guard-path (disj (walk-guard grid guard) guard)]
    (count (filter #(looping-guard? (place-obstruction grid %) guard)
                   guard-path))))
