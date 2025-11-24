(ns aoc.util.math
  (:require [clojure.math :as math]))

(defn num-digits [n] (count (str n)))

(defn div? [n d]
  (zero? (rem n d)))

(defn sum [l] (reduce + l))
(defn product [l] (reduce * l))

(defn ceil-div [n d]
  (if (zero? (rem n d))
    (quot n d)
    (inc (quot n d))))

(defn distance [x y]
  (abs (- x y)))

(defn manhattan-distance [pos-a pos-b]
  (sum (map distance pos-a pos-b)))

(defn mins-by [n-fn coll]
  (reduce (fn [[mn mins] e]
            (let [n (n-fn e)]
              (case (compare mn n)
                -1 [mn mins]
                0 [mn (conj mins e)]
                1 [n [e]])))
          [Integer/MAX_VALUE []]
          coll))

(defn maxes-by [n-fn coll]
  (reduce (fn [[mx maxes] e]
            (let [n (n-fn e)]
              (case (compare mx n)
                -1 [n [e]]
                0 [mx (conj maxes e)]
                1 [mx maxes])))
          [Integer/MIN_VALUE []]
          coll))

(defn quadratic-roots [a b c]
  (cond
    (= 0 a b c) :any
    (= 0 a b) nil
    (zero? a) [(/ (- c) b)]
    :else (let [discriminant (- (* b b) (* 4 a c))
                two-a        (* 2 a)
                neg-b        (- b)]
            (cond
              (pos? discriminant) [(/ (+ neg-b (math/sqrt discriminant)) two-a)
                                   (/ (- neg-b (math/sqrt discriminant)) two-a)]
              (zero? discriminant) [(/ neg-b two-a)]))))

(defn eq-pos-int? [x] (and (== (int x) x) (pos? x)))

(defn divisors [n]
  ;; TODO: this is slow, probably should have a prime factorization instead.
  (filter (partial div? n) (range 1 (inc n))))
