;; https://adventofcode.com/2015/day/23
 (ns aoc2015.day23
   (:require
    [aoc.day :as d]
    [aoc.util.string :as s]
    [clojure.string :as str]))

(defn input [] (d/day-input 2015 23))

(defn- run-program [input registers]
  (let [program (mapv #(str/split % #" |, ") (s/lines input))]
    (loop [pc 0, regs registers]
      (if (contains? program pc)
        (let [[op p1 p2] (program pc)]
          (case op
            "hlf" (recur (inc pc) (update regs p1 / 2))
            "tpl" (recur (inc pc) (update regs p1 * 3))
            "inc" (recur (inc pc) (update regs p1 inc))
            "jmp" (recur (+ pc (s/int p1)) regs)
            "jie" (recur (if (even? (regs p1)) (+ pc (s/int p2)) (inc pc)) regs)
            "jio" (recur (if (= 1 (regs p1)) (+ pc (s/int p2)) (inc pc)) regs)))
        regs))))

(defn part1 [input]
  (get (run-program input {"a" 0, "b" 0}) "b"))

(defn part2 [input]
  (get (run-program input {"a" 1, "b" 0}) "b"))
