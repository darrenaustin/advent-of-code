;; https://adventofcode.com/2020/day/15
(ns aoc2020.day15
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2020 15))

;; Original implementation used a transient map for
;; tracking the seen values. It worked, but was
;; very slow. Resorting to an int array to get
;; it fast enough for part2.

(defn- nth-spoken [input n]
  (let [init-nums (s/ints input)
        seen      (int-array n 0)]
    (doseq [[i num] (map-indexed vector (pop init-nums))]
      (aset seen (int num) (inc i)))
    (loop [turn     (int (count init-nums))
           last-num (int (peek init-nums))]
      (if (== turn n)
        last-num
        (let [prev-turn (aget seen last-num)
              next-num  (if (zero? prev-turn) 0 (- turn prev-turn))]
          (aset seen last-num turn)
          (recur (unchecked-inc turn) next-num))))))

(defn part1 [input] (nth-spoken input 2020))

(defn part2 [input] (nth-spoken input 30000000))
