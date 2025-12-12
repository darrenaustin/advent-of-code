;; https://adventofcode.com/2019/day/16
(ns aoc2019.day16
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2019 16))

(defn sum-range [xs start end]
  (let [end (min end (count xs))]
    (loop [sum 0, idx start]
      (if (< idx end)
        (recur (+ sum (xs idx)) (inc idx))
        sum))))

(defn nth-digit [xs n]
  (let [pattern [0 1 0 -1]
        end     (count xs)]
    (loop [sum 0, idx -1, p-idx 0]
      (if (>= idx end)
        (abs (rem sum 10))
        (let [p    (pattern p-idx)
              idx' (+ idx (inc n))]
          (if (zero? p)
            (recur sum idx' (mod (inc p-idx) 4))
            (recur (+ sum (* p (sum-range xs idx idx')))
                   idx'
                   (mod (inc p-idx) 4))))))))

(defn phase [xs]
  (mapv (partial nth-digit xs) (range 0 (count xs))))

(defn phase-offset [xs]
  (loop [sum 0, n (dec (count xs)), result '()]
    (if (neg? n)
      (vec result)
      (let [sum' (+ sum (xs n))]
        (recur sum' (dec n) (conj result (abs (rem sum' 10))))))))

(defn part1 [input]
  (s/parse-int (str/join (take 8 (c/iterate-n phase (s/digits input) 100)))))

; Got stuck on this one, but used these observations:
; https://www.reddit.com/r/adventofcode/comments/ebai4g/comment/fb45653/?utm_source=share&utm_medium=web3x&utm_name=web3xcss&utm_term=1&utm_content=share_button
;
; - the second half of the output only depends on the second half of the input
;
; - each digit i in the second half of the output is the sum of the digits of
;   the input from i to the end of the input
(defn part2 [input]
  (let [digits (vec (apply concat (repeat 10000 (s/digits input))))
        offset (s/parse-int (subs input 0 7))]
    (when (>= offset (/ (count digits) 2))
      (s/parse-int (str/join (take 8 (c/iterate-n phase-offset (vec (drop offset digits)) 100)))))))
