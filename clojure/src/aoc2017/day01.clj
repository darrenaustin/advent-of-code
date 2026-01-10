;; https://adventofcode.com/2017/day/1
(ns aoc2017.day01
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2017 1))

(defn- captcha [input step]
  (let [size (count input)]
    (->> input
         (keep-indexed
          (fn [i n] (when (= n (nth input (mod (+ i step) size)))
                      (s/char->digit n))))
         (reduce +))))

(defn part1 [input] (captcha input 1))

(defn part2 [input] (captcha input (/ (count input) 2)))
