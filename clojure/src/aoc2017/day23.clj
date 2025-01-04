;; https://adventofcode.com/2017/day/23
(ns aoc2017.day23
  (:require [aoc.day :as d]
            [aoc.util.string :as s]
            [clojure.math :as math]
            [clojure.string :as str]))

(defn input [] (d/day-input 2017 23))

(defn parse [input] (vec (str/split-lines input)))

(defn next-pc [process]
  (update process :pc inc))

(defn arg-val [process arg]
  (if (re-find #"[a-z]" arg) (get (:reg process) arg 0) (s/parse-int arg)))

(defn op-set [process arg1 arg2]
  (-> process
      (assoc-in [:reg arg1] (arg-val process arg2))
      next-pc))

(defn op-sub [process arg1 arg2]
  (-> process
      (assoc-in [:reg arg1] (- (arg-val process arg1) (arg-val process arg2)))
      next-pc))

(defn op-mul [process arg1 arg2]
  (-> process
      (assoc-in [:reg arg1] (* (arg-val process arg1) (arg-val process arg2)))
      (update-in [:mul-count] inc)
      next-pc))

(defn op-jump [process arg1 arg2]
  (if-not (zero? (arg-val process arg1))
    (update process :pc + (arg-val process arg2))
    (next-pc process)))

(defn execute [process commands]
  (loop [process process]
    (if (>= (:pc process) (count commands))
      process
      (let [[op arg1 arg2] (str/split (nth commands (:pc process)) #" ")]
        (recur ((get (:ops process) op) process arg1 arg2))))))

(defn part1 [input]
  (->> input
       parse
       (execute {:reg {} :pc 0 :mul-count 0
                 :ops {"set" op-set
                       "sub" op-sub
                       "mul" op-mul
                       "jnz" op-jump}})
       :mul-count))

;; Analyzing the program it appears to be incrementing 'h' when
;; it finds a non-prime number between 'b' and 'c' incrementing by 17.
;; In my input b is 105700 and c is 122700

;; Slow but simple prime check.
(defn prime? [n]
  (or (< 1 n 4)
      (every? #(not= 0 (mod n %)) (range 2 (inc (math/sqrt n))))))

(defn part2 [_]
  (count (remove prime? (range 105700 (inc 122700) 17))))
