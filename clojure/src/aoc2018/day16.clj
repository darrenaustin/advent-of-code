;; https://adventofcode.com/2018/day/16
(ns aoc2018.day16
  (:require [aoc.day :as d]
            [aoc.util.string :as s]
            [clojure.set :as set]
            [clojure.string :as str]))

(defn input [] (d/day-input 2018 16))

(defn parse-samples [input]
  (let [sample-data (first (str/split input #"\n\n\n"))]
    (for [sample (str/split sample-data #"\n\n")]
      (let [nums (partition 4 (s/parse-ints sample))]
        {:before      (vec (first nums))
         :instruction (second nums)
         :after       (vec (last nums))}))))

(defn parse-program [input]
  (partition 4 (s/parse-ints (second (str/split input #"\n\n\n")))))

(defn sample-match? [{:keys [before instruction after]} op]
  (let [[_ a b c] instruction]
    (= (op a b c before) after)))

(def operations
  [(fn addr [a b c reg] (assoc reg c (+ (reg a) (reg b))))
   (fn addi [a b c reg] (assoc reg c (+ (reg a) b)))
   (fn mulr [a b c reg] (assoc reg c (* (reg a) (reg b))))
   (fn muli [a b c reg] (assoc reg c (* (reg a) b)))
   (fn banr [a b c reg] (assoc reg c (bit-and (reg a) (reg b))))
   (fn bani [a b c reg] (assoc reg c (bit-and (reg a) b)))
   (fn borr [a b c reg] (assoc reg c (bit-or (reg a) (reg b))))
   (fn bori [a b c reg] (assoc reg c (bit-or (reg a) b)))
   (fn setr [a _ c reg] (assoc reg c (reg a)))
   (fn seti [a _ c reg] (assoc reg c a))
   (fn gtir [a b c reg] (assoc reg c (if (> a (reg b)) 1 0)))
   (fn gtri [a b c reg] (assoc reg c (if (> (reg a) b) 1 0)))
   (fn gtrr [a b c reg] (assoc reg c (if (> (reg a) (reg b)) 1 0)))
   (fn eqir [a b c reg] (assoc reg c (if (= a (reg b)) 1 0)))
   (fn eqri [a b c reg] (assoc reg c (if (= (reg a) b) 1 0)))
   (fn eqrr [a b c reg] (assoc reg c (if (= (reg a) (reg b)) 1 0)))])

(defn sample-matches [sample]
  (filter (partial sample-match? sample) operations))

(defn common-ops [samples]
  (reduce set/intersection (map (comp set sample-matches) samples)))

(defn determine-ops [samples]
  (let [by-opcode (group-by #(first (:instruction %)) samples)
        possible  (into {} (for [[code ss] by-opcode] [code (common-ops ss)]))]
    (loop [ops {} possible possible]
      (if (empty? possible)
        ops
        (let [[code os] (first (filter (fn [[_ ps]] (= (count ps) 1)) possible))
              possible' (dissoc possible code)]
          (recur (assoc ops code (first os))
                 (into {} (for [[op s] possible'] [op (disj s (first os))])))
          )))))

(defn execute [ops program]
  (reduce (fn [rs [op a b c]] ((ops op) a b c rs)) [0 0 0 0] program))

(defn part1 [input]
  (count (filter #(<= 3 (count (sample-matches %)))
                 (parse-samples input))))

(defn part2 [input]
  (first (execute (determine-ops (parse-samples input))
                  (parse-program input))))
