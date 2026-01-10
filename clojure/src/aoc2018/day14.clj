;; https://adventofcode.com/2018/day/14
(ns aoc2018.day14
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2018 14))

(defn part1 [input]
  (let [target-num    (s/int input)
        target-length (+ target-num 10)]
    (loop [elf1 0 elf2 1 recipes [3 7]]
      (if (> (count recipes) target-length)
        (str/join (take 10 (drop target-num recipes)))
        (let [recipe1     (recipes elf1)
              recipe2     (recipes elf2)
              new-recipes (m/digits (+ recipe1 recipe2))
              recipes'    (reduce conj recipes new-recipes)
              num-recipes (count recipes')]
          (recur (mod (+ elf1 recipe1 1) num-recipes)
                 (mod (+ elf2 recipe2 1) num-recipes)
                 recipes'))))))

(defn part2 [input]
  (let [target        (s/digits input)
        target-length (count target)]
    (loop [elf1 0 elf2 1 recipes [3 7] search-idx 0]
      (if (< (+ search-idx target-length) (count recipes))
        (if (= target (subvec recipes search-idx (+ search-idx target-length)))
          search-idx
          (recur elf1 elf2 recipes (inc search-idx)))
        (let [recipe1     (recipes elf1) recipe2 (recipes elf2)
              new-recipes (m/digits (+ recipe1 recipe2))
              recipes'    (reduce conj recipes new-recipes)
              num-recipes (count recipes')]
          (recur (mod (+ elf1 recipe1 1) num-recipes)
                 (mod (+ elf2 recipe2 1) num-recipes)
                 recipes'
                 search-idx))))))
