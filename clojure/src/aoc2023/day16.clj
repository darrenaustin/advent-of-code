;; https://adventofcode.com/2023/day/16
(ns aoc2023.day16
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2023 16))

(defn energized [grid x y dx dy]
  (let [size (count grid)]
    (loop [beams-seen      (transient #{})
           cells-visited   (transient #{})
           stack           (list [x y dx dy])]
      (if-let [[x y dx dy] (peek stack)]
        (if (or (not (< -1 x size)) (not (< -1 y size)))
          (recur beams-seen cells-visited (pop stack))
          (let [beam-hash (+ (* 12345 x) (* 1234 y) (* 123 dx) dy)
                stack' (pop stack)]
            (if (beams-seen beam-hash)
              (recur beams-seen cells-visited stack')
              (recur (conj! beams-seen beam-hash)
                     (conj! cells-visited (+ x (* size y)))
                     (case ((grid y) x)
                       \. (conj stack' [(+ x dx) (+ y dy) dx dy])
                       \/ (conj stack' [(- x dy) (- y dx) (- dy) (- dx)])
                       \\ (conj stack' [(+ x dy) (+ y dx) dy dx])
                       \| (if (zero? dx)
                            (conj stack' [x (+ y dy) 0 dy])
                            (conj stack' [x (dec y) 0 -1] [x (inc y) 0 1]))
                       \- (if (zero? dy)
                            (conj stack' [(+ x dx) y dx 0])
                            (conj stack' [(dec x) y -1 0] [(inc x) y 1 0])))))))
        (count cells-visited)))))

(defn part1 [input]
  (energized (mapv vec (s/lines input)) 0 0 1 0))

(defn part2 [input]
  (let [grid (mapv vec (s/lines input))
        size (count grid)
        e (dec size)]
    (assert (= size (count (first grid))) (<= size 1000))
    (->> (pmap (juxt #(energized grid % 0  0  1)
                     #(energized grid 0 %  1  0)
                     #(energized grid % e  0 -1)
                     #(energized grid e % -1  0))
               (range size))
         flatten
         (reduce max))))
