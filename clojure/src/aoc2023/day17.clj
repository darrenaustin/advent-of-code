;; https://adventofcode.com/2023/day/17
(ns aoc2023.day17
  (:require
   [aoc.day :as d]
   [aoc.util.char :as char]
   [aoc.util.grid :as g]
   [clojure.data.priority-map :refer [priority-map]]))

(defn input [] (d/day-input 2023 17))

(defn pos-hash ^long [^long x ^long y ^long dx ^long dy]
  (bit-or (bit-shift-left x 16)
          (bit-shift-left y 8)
          (bit-shift-left (inc dx) 4)
          (inc dy))) ;; +1 to handle negative directions

(defn- min-heat [grid min-steps max-steps]
  (assert (and (<= (g/width grid) 256) (<= (g/height grid) 256)))
  (let [goal (dec (g/width grid))]
    (loop [queue (priority-map [0 0 0 1] 0, [0 0 1 0] 0)
           seen (transient #{})]
      (let [[[x y dx dy] heat] (peek queue)]
        (if (= x y goal)
          heat
          (let [queue' (reduce
                        (fn [q [ndx ndy]]
                          (loop [q q, s 1, cur-heat heat, nx (+ x ndx), ny (+ y ndy)]
                            (if (or (> s max-steps) (not (grid [nx ny])))
                              q
                              (let [next-heat (+ cur-heat (grid [nx ny]))
                                    pos       [nx ny ndx ndy]]
                                (if (and (>= s min-steps) (not (seen (pos-hash nx ny ndx ndy))))
                                  (recur (assoc q pos (min next-heat (get q pos next-heat)))
                                         (inc s) next-heat (+ nx ndx) (+ ny ndy))
                                  (recur q (inc s) next-heat (+ nx ndx) (+ ny ndy)))))))
                        (pop queue)
                        [[(- dy) dx] [dy (- dx)]])]
            (recur queue' (conj! seen (pos-hash x y dx dy)))))))))

(defn part1 [input] (min-heat (g/->grid input char/digit) 1 3))

(defn part2 [input] (min-heat (g/->grid input char/digit) 4 10))
