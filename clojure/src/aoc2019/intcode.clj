(ns aoc2019.intcode
  (:require [aoc.util.string :as s]))

(defn parse [input]
  (vec (s/parse-ints input)))

(defn execute [program]
  (loop [mem program, pc 0]
    (let [pc' (min (+ pc 4) (count mem))
          [op a b c] (subvec mem pc pc')]
      (case op
        1 (recur (assoc mem c (+ (mem a) (mem b))) pc')
        2 (recur (assoc mem c (* (mem a) (mem b))) pc')
        99 mem))))
