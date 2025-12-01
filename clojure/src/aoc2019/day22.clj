;; https://adventofcode.com/2019/day/22
(ns aoc2019.day22
  (:require [aoc.day :as d]
            [aoc.util.math :as m]
            [aoc.util.string :as s]
            [clojure.string :as str]))

(defn input [] (d/day-input 2019 22))

(defn coefficients-for-composed-shuffles
  "Computes linear coefficients [a b] representing the composition of multiple
   shuffle operations. Each shuffle is represented as `f(x) = (a*x + b) % deck-size`.
   Where `x` is the original card index and `f(x)` is the new index of the card
   after the shuffles."
  [initial-coefficients shuffles deck-size]
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
  "Given coefficients [a b] for f(x) = (a*x + b) % deck-size,
   returns coefficients [a' b'] for the inverse function g(y) = (a'*y + b') % deck-size
   such that g(f(x)) = x."
  [[a b] deck-size]
  (let [a-inv (m/mod-inverse a deck-size)]
    [a-inv (mod (* (- a-inv) b) deck-size)]))

(defn polynomial-mod
  "Efficiently computes the nth power of linear transformation (ax+b) mod m.
   Uses binary exponentiation to avoid computing large intermediate values.
   Returns coefficients [a' b'] representing (ax+b)^n mod m."
  [[a b] n m]
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

(defn card-at-index
  "Given an index position, returns which card ends up at that position
   after applying the shuffle transformation represented by coefficients [a b]."
  [index [a b] deck-size]
  (long (mod (+ (* a index) b) deck-size)))

(defn index-for-card
  "Given a card value, returns where that card ends up in the shuffled deck."
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
