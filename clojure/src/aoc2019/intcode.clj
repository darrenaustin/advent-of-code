(ns aoc2019.intcode
  (:require [aoc.util.collection :as c]
            [aoc.util.string :as s]))

(defn parse [input]
  (vec (s/parse-ints input)))

(def tracing false)

(defn trace [machine op params]
  (when tracing
    (println (format "tracing [%02d]: %s" (:pc machine) op) params)))

(defn vec->map [v]
  (into {} (map-indexed (fn [i x] [i x]) v)))

(defn set-pc [machine addr]
  (assoc machine :pc addr))

(defn inc-pc
  ([machine] (update machine :pc inc))
  ([machine n] (update machine :pc + n)))

(defn param-value [machine {:keys [mode value]}]
  (case mode
    0 (get-in machine [:mem value] 0)
    1 value
    2 (get-in machine [:mem (+ value (:relative-base machine))] 0)))

(defn param-address [machine {:keys [mode value]}]
  (case mode
    0 value
    1 (throw (Exception. "Can't store to an immediate param address"))
    2 (+ value (:relative-base machine))))

(defn params [{:keys [pc mem]} num-params]
  (let [opcode (mem pc)
        modes  (reverse (c/pad-left (s/digits (quot opcode 100)) num-params 0))
        args   (map #(mem (+ pc %)) (range 1 (inc num-params)))]
    (map (fn [m a] {:value a :mode m}) modes args)))

(defn store [machine param value]
  (assoc-in machine [:mem (param-address machine param)] value))

(defn op-+ [machine]
  (let [[a b c] (params machine 3)]
    (trace machine "+" [a b c])
    (-> machine
        (store c (+ (param-value machine a) (param-value machine b)))
        (inc-pc 4))))

(defn op-* [machine]
  (let [[a b c] (params machine 3)]
    (trace machine "*" [a b c])
    (-> machine
        (store c (* (param-value machine a) (param-value machine b)))
        (inc-pc 4))))

(defn op-input [machine]
  (let [[a] (params machine 1)
        value (peek (:input machine))]
    (trace machine "input" [a value])
    (if value
      (-> machine
          (store a value)
          (update :input pop)
          (inc-pc 2))
      (assoc machine :status :waiting))))

(defn op-output [machine]
  (let [[a] (params machine 1)]
    (trace machine "output" [a])
    (-> machine
        (update :output conj (param-value machine a))
        (inc-pc 2))))

(defn op-jump-if-true [machine]
  (let [[a b] (params machine 2)]
    (trace machine "jump-if-true" [a b])
    (if (zero? (param-value machine a))
      (inc-pc machine 3)
      (set-pc machine (param-value machine b)))))

(defn op-jump-if-false [machine]
  (let [[a b] (params machine 2)]
    (trace machine "jump-if-false" [a b])
    (if (zero? (param-value machine a))
      (set-pc machine (param-value machine b))
      (inc-pc machine 3))))

(defn op-less-than [machine]
  (let [[a b c] (params machine 3)
        value (if (< (param-value machine a) (param-value machine b)) 1 0)]
    (trace machine "less-than" [a b c value])
    (-> machine
        (store c value)
        (inc-pc 4))))

(defn op-equals [machine]
  (let [[a b c] (params machine 3)
        value (if (= (param-value machine a) (param-value machine b)) 1 0)]
    (trace machine "equals" [a b c value])
    (-> machine
        (store c value)
        (inc-pc 4))))

(defn op-inc-relative-base [machine]
  (let [[a] (params machine 1)]
    (trace machine "inc-rel-base" [a])
    (-> machine
        (update :relative-base + (param-value machine a))
        (inc-pc 2))))

(defn op-halt [machine]
  (trace machine "halt" [])
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
   9  op-inc-relative-base
   99 op-halt})

(defn init-machine
  ([program] (init-machine program nil nil))
  ([program input output]
   {:mem           (vec->map program)
    :pc            0
    :relative-base 0
    :status        :running
    :input         input
    :output        (or output [])}))

(defn mem->vec [{:keys [mem]}]
  (let [start (apply min (keys mem))
        end   (apply max (keys mem))]
    (reduce (fn [v i] (conj v (get mem i 0))) [] (range start (inc end)))))

(defn update-io [machine input output]
  (assoc machine :input input :output output :status :running))

(defn execute [machine]
  (loop [{:keys [mem pc] :as machine} machine]
    (if (= (:status machine) :running)
      (recur ((ops (rem (mem pc) 100)) machine))
      machine)))

(defn run
  ([program] (run program nil nil))
  ([program input output] (execute (init-machine program input output))))
