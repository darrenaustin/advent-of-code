(ns aoc2019.intcode
  (:require [aoc.util.collection :as c]
            [aoc.util.string :as s]))

(defn parse [input]
  (vec (s/parse-ints input)))

(defn set-pc [machine addr]
  (assoc machine :pc addr))

(defn inc-pc
  ([machine] (update machine :pc inc))
  ([machine n] (update machine :pc + n)))

(defn param-value [machine {:keys [mode value]}]
  (case mode
    0 (get-in machine [:mem value] 0)
    1 value))

(defn params [{:keys [pc mem]} num-params]
  (let [opcode (mem pc)
        modes  (reverse (c/pad-left (s/digits (quot opcode 100)) num-params 0))
        args   (map #(mem (+ pc %)) (range 1 (inc num-params)))]
    (map (fn [m a] {:value a :mode m}) modes args)))

(defn store [machine param value]
  (assoc-in machine [:mem (:value param)] value))

(defn op-+ [machine]
  (let [[a b c] (params machine 3)]
    (-> machine
        (store c (+ (param-value machine a) (param-value machine b)))
        (inc-pc 4))))

(defn op-* [machine]
  (let [[a b c] (params machine 3)]
    (-> machine
        (store c (* (param-value machine a) (param-value machine b)))
        (inc-pc 4))))

(defn op-input [machine]
  (let [[a] (params machine 1)
        value (peek (:input machine))]
    (-> machine
        (store a value)
        (update :input pop)
        (inc-pc 2))))

(defn op-output [machine]
  (let [[a] (params machine 1)]
    (-> machine
        (update :output conj (param-value machine a))
        (inc-pc 2))))

(defn op-jump-if-true [machine]
  (let [[a b] (params machine 2)]
    (if (zero? (param-value machine a))
      (inc-pc machine 3)
      (set-pc machine (param-value machine b)))))

(defn op-jump-if-false [machine]
  (let [[a b] (params machine 2)]
    (if (zero? (param-value machine a))
      (set-pc machine (param-value machine b))
      (inc-pc machine 3))))

(defn op-less-than [machine]
  (let [[a b c] (params machine 3)
        value (if (< (param-value machine a) (param-value machine b)) 1 0)]
    (-> machine
        (store c value)
        (inc-pc 4))))

(defn op-equals [machine]
  (let [[a b c] (params machine 3)
        value (if (= (param-value machine a) (param-value machine b)) 1 0)]
    (-> machine
        (store c value)
        (inc-pc 4))))

(defn op-halt [machine]
  (assoc machine :status :halted))

(def ops
  {1  op-+
   2  op-*
   3  op-input
   4  op-output
   5  op-jump-if-true
   6  op-jump-if-false
   7  op-less-than
   8  op-equals
   99 op-halt})

(defn init-machine [program input output]
  {:mem program, :pc 0, :status :running, :input input, :output (or output [])})

(defn execute
  ([program] (execute program nil nil))
  ([program input output]
   (loop [{:keys [mem pc] :as machine} (init-machine program input output)]
     (if (= (:status machine) :running)
       (recur ((ops (rem (mem pc) 100)) machine))
       machine))))
