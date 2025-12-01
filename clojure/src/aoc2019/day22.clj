;; https://adventofcode.com/2019/day/22
(ns aoc2019.day22
  (:require [aoc.day :as d]
            [aoc.util.math :as m]
            [aoc.util.string :as s]
            [clojure.string :as str]))

(defn input [] (d/day-input 2019 22))

;; linear coefficients [a b deck-size] are used to describe the linear
;; transformation `f(x) = (a*x + b) % deck-size` that represents a shuffle
;; operation, where x is the original index of a card, and f(x) is the new
;; index after the shuffle. These can be composed to represent multiple shuffle
;; operations.
(defn coefficients-for-composed-shuffles [initial-coefficients shuffles deck-size]
  (reduce (fn [[a b] shuffle]
            (cond
              (= shuffle "deal into new stack")
              [(- a) (- deck-size b 1)]

              (str/starts-with? shuffle "cut ")
              (let [n (s/parse-int shuffle)]
                [a (mod (+ b n) deck-size)])

              (str/starts-with? shuffle "deal with increment ")
              (let [n (s/parse-int shuffle)
                    n-inv (m/mod-inverse n deck-size)]
                [(mod (* a n-inv) deck-size)
                 (mod (* b n-inv) deck-size)])

              :else (throw (ex-info "Unknown instruction" {:instruction shuffle}))))
          initial-coefficients
          (reverse shuffles)))

(defn inverse-coefficients
  [[a b] deck-size]
  (let [a-inv (m/mod-inverse a deck-size)]
    [a-inv (mod (* (- a-inv) b) deck-size)]))

(defn polynomial-mod [[a b] n m]
  ;; Process (ax+b)^n % m
  (if (zero? n)
    [1 0]
    (if (even? n)
      (polynomial-mod [(mod (* a a) m)
                       (mod (+ (* a b) b) m)]
                      (quot n 2)
                      m)
      (let [[c d] (polynomial-mod [a b] (dec n) m)]
        [(mod (* a c) m)
         (mod (+ (* a d) b) m)]))))

(defn card-at-index [index [a b] deck-size]
  (long (mod (+ (* a index) b) deck-size)))

(defn index-for-card
  [card [a b] deck-size]
  (let [[a-inv b-inv] (inverse-coefficients [a b] deck-size)]
    (mod (+ (* a-inv card) b-inv) deck-size)))

(defn part1 [input]
  (let [deck-size    10007
        shuffles     (str/split-lines input)
        coefficients (coefficients-for-composed-shuffles [1 0] shuffles deck-size)]
    (index-for-card 2019 coefficients deck-size)))

(defn part2 [input]
  (let [deck-size    119315717514047N
        iterations   101741582076661N
        shuffles     (str/split-lines input)
        coefficients (-> [1 0]
                         ;; Find the linear transformation (a*x + b) mod deck-size
                         ;; that represents one full shuffle
                         (coefficients-for-composed-shuffles shuffles deck-size)
                         ;; Apply the transformation 'iterations' times
                         (polynomial-mod iterations deck-size))]
    (card-at-index 2020 coefficients deck-size)))
