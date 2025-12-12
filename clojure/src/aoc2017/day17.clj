;; https://adventofcode.com/2017/day/17
(ns aoc2017.day17
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2017 17))

(defn part1 [input]
  (let [step (s/parse-int input)]
    (loop [buffer [0] pos 0 n 1]
      (if (= n 2018)
        (nth buffer (inc pos))
        (let [pos' (inc (mod (+ pos step) n))
              [before after] (split-at pos' buffer)]
          (recur (vec (concat before [n] after)) pos' (inc n)))))))

(defn part2 [input]
  (let [step (s/parse-int input)]
    (loop [pos 0 n 1 after-zero nil]
      (if (= n 50000000)
        after-zero
        (let [pos' (inc (mod (+ pos step) n))]
          (recur pos' (inc n) (if (= 1 pos') n after-zero)))))))
