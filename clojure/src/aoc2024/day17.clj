;; https://adventofcode.com/2024/day/17
(ns aoc2024.day17
  (:require [aoc.day :as d]
            [aoc.util.string :as s]
            [clojure.string :as str]))

(defn input [] (d/day-input 2024 17))

(defn parse-machine [input]
  (let [[a b c & code] (s/parse-ints input)]
    {:a a :b b :c c :code (vec code) :pc 0 :output []}))

(defn combo [{:keys [a b c]} arg]
  (case arg
    0 0, 1 1, 2 2, 3 3
    4 a, 5 b, 6 c
    7 (throw (Exception. "Invalid combo arg"))))

(defn execute [machine]
  (loop [{:keys [a b c code pc output] :as m} machine]
    (if (>= pc (count code))
      m
      (let [pc' (+ pc 2)
            m'  (assoc m :pc pc')
            [op arg] (subvec code pc pc')]
        (case op
          ;; adv
          0 (recur (assoc m' :a (bit-shift-right a (combo m arg))))

          ;; bxl
          1 (recur (assoc m' :b (bit-xor b arg)))

          ;; bst
          2 (recur (assoc m' :b (mod (combo m arg) 8)))

          ;; jnz
          3 (if (zero? a)
              (recur m')
              (recur (assoc m' :pc arg)))

          ;; bxc
          4 (recur (assoc m' :b (bit-xor b c)))

          ;; out
          5 (recur (assoc m' :output (conj output (mod (combo m arg) 8))))

          ;; bdv
          6 (recur (assoc m' :b (bit-shift-right a (combo m arg))))

          ;; cdv
          7 (recur (assoc m' :c (bit-shift-right a (combo m arg)))))))))

(defn part1 [input]
  (str/join "," (:output (execute (parse-machine input)))))

(defn part2 [input]
  (let [machine    (parse-machine input)
        code       (:code machine)
        last-digit (dec (count code))]
    (loop [a 0 digit 0]
      (let [output (:output (execute (assoc machine :a a)))]
        (if (= output (subvec code (- last-digit digit)))
          (if (= digit last-digit)
            a
            (recur (* a 8) (inc digit)))
          (recur (inc a) digit))))))
