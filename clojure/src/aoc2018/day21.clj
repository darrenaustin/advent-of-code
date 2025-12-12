;; https://adventofcode.com/2018/day/21
(ns aoc2018.day21
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2018 21))

(defn parse [input]
  (letfn [(parse-instr [instr]
            (let [[op & args] (str/split instr #" ")]
              (into [op] (mapv s/parse-int args))))]
    (mapv parse-instr (str/split-lines input))))

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

(defn apply-op [[op a b c] regs]
  ((operations op) a b c regs))

(defn execute [program regs line-handlers]
  (let [[_ ip] (first program) instrs (subvec program 1)]
    (loop [pc 0, regs regs]
      (if-let [value ((or (line-handlers pc) (constantly nil)) regs)]
        value
        (if-let [instr (get instrs pc nil)]
          (let [regs' (apply-op instr (assoc regs ip pc))]
            (recur (inc (regs' ip)) regs'))
          regs)))))

(defn part1 [input]
  ;; Analyzing the program (21_input_annotated.txt), it looks like
  ;; it will exit on instruction 28/29 when register 3 is equal to
  ;; register 0. Using the above code, we can run the program and
  ;; stop when it first hits 28 and see what register 3 is.
  (execute (parse input) [0 0 0 0 0 0] {28 (fn [regs] (regs 3))}))

(defn part2 [input]
  ;; In this case we need the last value of register 3 that will exit
  ;; the program. This means there is probably a cycle, so just run until
  ;; we see an already generated reg 3 value and take the last one.
  ;; This is *way* too slow. Perhaps I need to figure out what the calculation
  ;; is, or convert the program to raw clojure to find this faster.
  (let [ds (atom #{}) last-d (atom nil)]
    (execute (parse input) [0 0 0 0 0 0]
             {28 (fn [[_ _ _ d _]]
                   (if (@ds d)
                     @last-d
                     (do (reset! last-d d)
                         (swap! ds conj d)
                         nil)))})))
