;; https://adventofcode.com/2018/day/19
(ns aoc2018.day19
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2018 19))

(defn parse [input]
  (mapv (fn [line]
          (let [op     (subs line 0 4)
                params (s/ints line)]
            (into [op] params)))
        (s/lines input)))

(def operations
  {"addr" (fn [a b c reg] (assoc reg c (+ (reg a) (reg b))))
   "addi" (fn [a b c reg] (assoc reg c (+ (reg a) b)))
   "mulr" (fn [a b c reg] (assoc reg c (* (reg a) (reg b))))
   "muli" (fn [a b c reg] (assoc reg c (* (reg a) b)))
   "banr" (fn [a b c reg] (assoc reg c (bit-and (reg a) (reg b))))
   "bani" (fn [a b c reg] (assoc reg c (bit-and (reg a) b)))
   "borr" (fn [a b c reg] (assoc reg c (bit-or (reg a) (reg b))))
   "bori" (fn [a b c reg] (assoc reg c (bit-or (reg a) b)))
   "setr" (fn [a _ c reg] (assoc reg c (reg a)))
   "seti" (fn [a _ c reg] (assoc reg c a))
   "gtir" (fn [a b c reg] (assoc reg c (if (> a (reg b)) 1 0)))
   "gtri" (fn [a b c reg] (assoc reg c (if (> (reg a) b) 1 0)))
   "gtrr" (fn [a b c reg] (assoc reg c (if (> (reg a) (reg b)) 1 0)))
   "eqir" (fn [a b c reg] (assoc reg c (if (= a (reg b)) 1 0)))
   "eqri" (fn [a b c reg] (assoc reg c (if (= (reg a) b) 1 0)))
   "eqrr" (fn [a b c reg] (assoc reg c (if (= (reg a) (reg b)) 1 0)))})

(defn execute [program regs]
  (let [ip (second (first program)), instrs (subvec program 1)]
    (loop [pc 0, regs regs]
      (if-let [instr (get instrs pc nil)]
        (let [regs'  (assoc regs ip pc)
              [op a b c] instr
              regs'' ((operations op) a b c regs')
              pc'    (inc (regs'' ip))]
          (recur pc' regs''))
        regs))))

(defn part1 [input]
  (first (execute (parse input) [0 0 0 0 0 0])))

(defn part2 [_]
  ;; Analyzing my specific input (see 19_input_annotated.txt), with
  ;; register 0 = 1, it appears to be very slowly computing the sum
  ;; of the divisors of 10551264.
  (m/sum (m/divisors 10551264)))
