(ns aoc2022.day21
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2022 21))

(def ^:private ops {"+" + "-" - "*" * "/" /})

(defn- parse-riddle [input]
  (into {}
        (map (fn [line]
               (let [[name value] (str/split line #": ")
                     [a op b]     (str/split value #" ")]
                 [(keyword name)
                  (if op
                    [(keyword a) (ops op) (keyword b)]
                    (s/int value))])))
        (s/lines input)))

(defn- riddle-value
  ([riddle name]
   (when-let [val (riddle name)]
     (if (number? val)
       val
       (let [[a op b] val
             v1 (riddle-value riddle a)
             v2 (riddle-value riddle b)]
         (when (and v1 v2)
           (op v1 v2)))))))

(defn- invert-op [left op right target]
  (if (nil? left)
    ; target = (expr op right)
    (condp = op
      + (- target right)
      - (+ target right)
      * (/ target right)
      / (* target right))

    ; target = (left op expr)
    (condp = op
      + (- target left)
      - (- left target)
      * (/ target left)
      / (/ left target))))

(defn- match-root [riddle target-name]
  (loop [name :root, target nil]
    (if (= name target-name)
      target
      (let [[op1 op op2] (riddle name)
            left (riddle-value riddle op1)
            right (riddle-value riddle op2)]
        (if (nil? left)
          (recur op1 (if target (invert-op left op right target) right))
          (recur op2 (if target (invert-op left op right target) left)))))))

(defn part1 [input]
  (riddle-value (parse-riddle input) :root))

(defn part2 [input]
  (-> (parse-riddle input)
      (dissoc :humn)
      (match-root :humn)))
