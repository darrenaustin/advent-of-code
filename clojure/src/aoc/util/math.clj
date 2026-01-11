(ns aoc.util.math
  (:require
   [aoc.util.collection :as c]
   [clojure.math :as math]))

(def max-int Integer/MAX_VALUE)

(def min-int Integer/MIN_VALUE)

(def min-long Long/MIN_VALUE)

#_{:clojure-lsp/ignore [:clojure-lsp/unused-public-var]}
(def max-long Long/MAX_VALUE)

(defn num-digits [n]
  (if (zero? n)
    1
    (long (inc (math/log10 (abs n))))))

(defn digits [n]
  (loop [ds '(), n (abs n)]
    (if (< n 10)
      (conj ds (int n))
      (recur (conj ds (int (rem n 10))) (quot n 10)))))

(defn div? [n d]
  (zero? (rem n d)))

(defn gcd [a b]
  (if (zero? b)
    a
    (recur b (rem a b))))

(defn lcm
  ([a b]
   (if (or (zero? a) (zero? b))
     0
     (abs (/ (* a b) (gcd a b)))))
  ([a b & more]
   (reduce lcm (lcm a b) more)))

(defn sum [l] (reduce + l))
(defn product [l] (reduce * l))

(defn ceil-div [n d]
  (if (zero? (rem n d))
    (quot n d)
    (inc (quot n d))))

(defn distance [x y]
  (abs (- x y)))

(defn manhattan-distance
  ([pos-a] (manhattan-distance pos-a (repeat (count pos-a) 0)))
  ([pos-a pos-b]
   (sum (map distance pos-a pos-b))))

(defn range-inc
  ([] (range))
  ([end] (range (inc end)))
  ([start end] (range start (inc end)))
  ([start end step] (range start (inc end) step)))

;; Blatantly stolen from `tschady`'s excellent aoc repo:
;;
;; https://github.com/tschady/advent-of-code/blob/main/src/aoc/coll_util.clj
;;
;; This clever and elegant implementation is based on their observation:
;;
;; The trick here is realizing that `(B + C + D) - (A + B + C) = D - A`.
(defn intervals
  "Returns the seq of intervals between each element of `xs`, step `n` (default 1)"
  ([xs] (intervals 1 xs))
  ([n xs] (map - (drop n xs) xs)))

#_{:clojure-lsp/ignore [:clojure-lsp/unused-public-var]}
(defn mins-by [n-fn coll]
  (reduce (fn [[mn mins] e]
            (let [n (n-fn e)]
              (case (compare mn n)
                -1 [mn mins]
                0 [mn (conj mins e)]
                1 [n [e]])))
          [max-int []]
          coll))

(defn maxes-by [n-fn coll]
  (reduce (fn [[mx maxes] e]
            (let [n (n-fn e)]
              (case (compare mx n)
                -1 [n [e]]
                0 [mx (conj maxes e)]
                1 [mx maxes])))
          [min-int []]
          coll))

#_{:clojure-lsp/ignore [:clojure-lsp/unused-public-var]}
(defn min-of [f coll]
  (transduce (map f) min max-long coll))

(defn max-of [f coll]
  (transduce (map f) max min-long coll))

#_{:clojure-lsp/ignore [:clojure-lsp/unused-public-var]}
(defn min-by [f coll]
  (apply min-key f coll))

(defn max-by [f coll]
  (apply max-key f coll))

(defn indexed-min [coll]
  (reduce (fn [[mi mv] [i v]] (if (< v mv) [i v] [mi mv]))
          (c/indexed coll)))

(defn indexed-max [coll]
  (reduce (fn [[mi mv] [i v]] (if (> v mv) [i v] [mi mv]))
          (c/indexed coll)))

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
  (->> (range 1 (inc (math/sqrt n)))
       (filter (fn [d] (div? n d)))
       (mapcat (fn [d] [d (/ n d)]))
       sort))

(defn- extended-gcd [a b]
  (loop [old-r a, r b
         old-s 1, s 0
         old-t 0, t 1]
    (if (zero? r)
      [old-r old-s old-t]
      (let [quotient (quot old-r r)]
        (recur r (- old-r (* quotient r))
               s (- old-s (* quotient s))
               t (- old-t (* quotient t)))))))

(defn mod-inverse [a m]
  (let [m (abs m)
        a (if (neg? a) (mod a m) a)
        [g x _] (extended-gcd a m)]
    (when (= g 1)
      (mod x m))))
