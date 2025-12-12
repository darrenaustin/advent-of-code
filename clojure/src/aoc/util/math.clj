(ns aoc.util.math
  (:require
   [aoc.util.collection :as c]
   [clojure.math :as math]))

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
  ;; TODO: this is slow, probably should have a prime factorization instead.
  (filter (partial div? n) (range 1 (inc n))))

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
    (if (= g 1)
      (mod x m)
      (throw (ex-info "Modular inverse does not exist" {:a a :m m})))))  ; Return nil if no inverse exists
