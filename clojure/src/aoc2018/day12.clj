;; https://adventofcode.com/2018/day/12
(ns aoc2018.day12
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [clojure.string :as str]))

(defn input [] (d/day-input 2018 12))

(defn parse-pots [input]
  (into (sorted-set)
        (comp (map-indexed vector)
              (filter #(not= \. (second %)))
              (map first))
        input))

(defn pattern->mask [pattern]
  (loop [mask 0 cs pattern]
    (if (empty? cs)
      mask
      (recur (bit-or (bit-shift-left mask 1) (if (= (first cs) \#) 1 0))
             (rest cs)))))

(defn parse-rule [line]
  (let [[pattern plant] (str/split line #" => ")]
    (when (= plant "#")
      (pattern->mask pattern))))

(defn parse [input]
  (let [[state rules] (str/split input #"\n\n")]
    [(parse-pots (second (str/split state #": ")))
     (set (keep parse-rule (str/split-lines rules)))]))

(defn mask-for [pots p]
  (loop [mask 0 cs (map pots (range (- p 2) (+ p 3)))]
    (if (empty? cs)
      mask
      (recur (bit-or (bit-shift-left mask 1) (if (first cs) 1 0))
             (rest cs)))))

(defn next-generation [rules]
  (fn [pots]
    (let [start-p    (- (first pots) 2) end-p (+ (last pots) 2)
          start-mask (mask-for pots (dec start-p))]
      (loop [p start-p mask start-mask pots' (sorted-set)]
        (if (< end-p p)
          pots'
          (let [mask' (bit-or (bit-shift-left (bit-and mask 0xf) 1)
                              (if (pots (+ p 2)) 1 0))
                pots' (if (rules mask') (conj pots' p) pots')]
            (recur (inc p) mask' pots')))))))

(defn nth-iteration [n f x]
  (loop [n n x x]
    (if (zero? n) x (recur (dec n) (f x)))))

(defn normalize [pots]
  (let [start (first pots)]
    (into (sorted-set) (map #(- % start) pots))))

(defn detect-cycle [f x]
  (loop [n 0 x x seen {}]
    (or (seen x)
        (recur (inc n) (normalize (f x)) (assoc seen x n)))))

(defn solve [[pots rules] iterations]
  (m/sum (nth-iteration iterations (next-generation rules) pots)))

(defn part1 [input] (solve (parse input) 20))

(defn part2 [input]
  (let [[pots rules] (parse input)
        start-cycle (detect-cycle (next-generation rules) pots)
        start-sum   (solve [pots rules] start-cycle)
        cycle-diff  (- (solve [pots rules] (inc start-cycle)) start-sum)]
    (+ (* cycle-diff (- 50000000000 start-cycle)) start-sum)))
