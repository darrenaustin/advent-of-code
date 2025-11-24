;; https://adventofcode.com/2024/day/22
(ns aoc2024.day22
  (:require [aoc.day :as d]
            [aoc.util.math :as m]
            [aoc.util.string :as s]))

(defn input [] (d/day-input 2024 22))

(defn next-secret [s]
  (let [s1 (mod (bit-xor s (bit-shift-left s 6)) 16777216)
        s2 (mod (bit-xor s1 (bit-shift-right s1 5)) 16777216)
        s3 (mod (bit-xor s2 (bit-shift-left s2 11)) 16777216)]
    s3))

(defn secret-nums [start]
  (take 2000 (drop 1 (iterate next-secret start))))

(defn price-diffs [coll]
  (let [prices (map #(rem % 10) coll)]
    (mapv (fn [p1 p2] [p2 (- p2 p1)]) prices (rest prices))))

(def diff-hash
  (memoize
   (fn [[d1 d2 d3 d4]]
     (bit-or
      (bit-shift-left (+ d1 9) 15)
      (bit-shift-left (+ d2 9) 10)
      (bit-shift-left (+ d3 9) 5)
      (+ d4 9)))))

(defn part1 [input]
  (m/sum (map #(last (secret-nums %)) (s/parse-ints input))))

(defn part2 [input]
  (let [diffs (map #(price-diffs (secret-nums %)) (s/parse-ints input))]
    (loop [diffs diffs bananas {}]
      (if-let [buyer-diff (first diffs)]
        (recur (rest diffs)
               (loop [is (range 3 (count buyer-diff)) seen #{} bananas bananas]
                 (if-let [i (first is)]
                   (let [dh    (diff-hash (mapv second (subvec buyer-diff (- i 3) (inc i))))
                         price (first (buyer-diff i))]
                     (if (seen dh)
                       (recur (rest is) seen bananas)
                       (recur (rest is) (conj seen dh)
                              (assoc bananas dh (+ (get bananas dh 0) price)))))
                   bananas)))
        (apply max (vals bananas))))))
