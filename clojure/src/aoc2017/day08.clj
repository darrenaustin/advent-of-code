;; https://adventofcode.com/2017/day/8
(ns aoc2017.day08
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2017 8))

(def ^:private instruction-regex #"^(\w+) (inc|dec) (-?\d+) if (\w+) (.*?) (-?\d+)$")

(defn- parse-instruction [line]
  (let [[_ reg1 op val1 reg2 pred val2] (re-find instruction-regex line)]
    {:reg1 reg1
     :op   (if (= op "inc") + -)
     :val1 (s/int val1)
     :reg2 reg2
     :pred (if (= pred "!=") not= (resolve (symbol pred)))
     :val2 (s/int val2)}))

(defn- execute-instruction [regs instruction]
  (let [{:keys [reg1 op val1 reg2 pred val2]} instruction]
    (if (pred (get regs reg2 0) val2)
      (update regs reg1 (fnil op 0) val1)
      regs)))

(defn- execute-program [input]
  (reduce
   (fn [{:keys [regs max-val]} line]
     (let [regs' (execute-instruction regs line)
           current-max (if (empty? regs') 0 (apply max (vals regs')))]
       {:regs regs'
        :max-val (max max-val current-max)}))
   {:regs {} :max-val 0}
   (map parse-instruction (s/lines input))))

(defn part1 [input] (apply max (vals (:regs (execute-program input)))))

(defn part2 [input] (:max-val (execute-program input)))
