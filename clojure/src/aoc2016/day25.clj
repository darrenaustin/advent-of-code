;; https://adventofcode.com/2016/day/25
 (ns aoc2016.day25
   (:require
    [aoc.day :as d]
    [aoc2016.assembunny :as asm]))

(defn input [] (d/day-input 2016 25))

(defn part1 [input]
  (let [base (asm/init-program input)]
    (loop [a 0, program (assoc-in base [:regs :a] a)]
      (let [output (:output program)]
        (cond
          (not (every? #{0 1} output)) (recur (inc a) (assoc-in base [:regs :a] (inc a)))
          (not= (count output) 8) (recur a (asm/step program))
          (= output [0 1 0 1 0 1 0 1]) a
          :else (recur (inc a) (assoc-in base [:regs :a] (inc a))))))))

(defn part2 [_] "🎄 Got em all! 🎉")
