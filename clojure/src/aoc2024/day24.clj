;; https://adventofcode.com/2024/day/24
(ns aoc2024.day24
  (:require [aoc.day :as d]
            [clojure.set :as set]
            [clojure.string :as str]))

(defn input [] (d/day-input 2024 24))

(defn parse-gate [input]
  (let [[w1 op w2 out] (rest (re-find #"(.*) (AND|OR|XOR) (.*) -> (.*)" input))]
    {:w1 w1 :w2 w2 :op op :out out}))

(defn parse [input]
  (let [[init-data conn-data] (str/split input #"\n\n")]
    [(update-vals (into {} (map #(str/split % #": ")
                                (str/split-lines init-data)))
                  read-string)
     (map parse-gate (str/split-lines conn-data))]))

(defn wires-of [network]
  (apply set/union (map (fn [g] #{(:w1 g) (:w2 g) (:out g)}) network)))

(defn wires->integer [wires]
  (read-string (str "2r" (str/join wires))))

(defn open-gates [values network]
  (filter #(nil? (values (:out %))) network))

(defn eval-gate [values gate]
  (let [{:keys [w1 w2 op out]} gate]
    {out (case op
           "AND" (bit-and (values w1) (values w2))
           "OR" (bit-or (values w1) (values w2))
           "XOR" (bit-xor (values w1) (values w2)))}))

(defn simulate [[initial network]]
  (let [wires   (wires-of network)
        z-wires (reverse (sort (filter #(str/starts-with? % "z") wires)))]
    (loop [values initial]
      (let [z-values (map values z-wires)]
        (if (not-any? nil? z-values)
          (wires->integer z-values)
          (let [gates  (filter #(and (values (:w1 %)) (values (:w2 %)))
                               (open-gates values network))
                output (apply merge (map #(eval-gate values %) gates))]
            (recur (merge values output))))))))

(defn part1 [input]
  (simulate (parse input)))

;; Shamelessly cribbed from a solution found via reddit:
;;
;; https://github.com/piman51277/AdventOfCode/blob/master/solutions/2024/24/index2prog.js
;;
;; I know very little about ripple-adders, so I am using this as
;; guide to implement this solution in clojure. Challenging to
;; convert a complex imperative program to a functional language.
;;
;; TODO: Need to come back and see how much cleaner I can make this.
;;
(defn is-direct? [gate]
  (or (str/starts-with? (:w1 gate) "x")
      (str/starts-with? (:w2 gate) "x")))

(defn is-output? [gate]
  (str/starts-with? (:out gate) "z"))

(defn is-type [type]
  (fn [gate] (= (:op gate) type)))

(defn has-output [output]
  (fn [gate] (= (:out gate) output)))

(defn has-input [input]
  (fn [gate] (or (= (:w1 gate) input)
                 (= (:w2 gate) input))))

(defn faulty-FAGate0? [{:keys [w1 w2 out] :as gate}]
  (let [first? (or (= w1 "x00") (= w2 "x00"))]
    (or (and first? (not= out "z00"))
        (and (not first?) (is-output? gate)))))

(defn faulty-output? [gate]
  ;; TODO: shouldn't hard code this for z45
  (let [last? (= (:out gate) "z45")]
    (or (and last? (not= (:op gate) "OR"))
        (and (not last?) (not= (:op gate) "XOR")))))

(defn faulty-0-3-connections [g0s g3s faulty]
  (loop [g0s g0s faulty' faulty check-gates []]
    (if (empty? g0s)
      [check-gates faulty']
      (let [{:keys [out] :as g0} (first g0s)]
        (if (or (faulty g0) (= out "z00"))
          (recur (rest g0s) faulty' check-gates)
          (let [matches (filter #((has-input (:out g0)) %) g3s)]
            (if (empty? matches)
              (recur (rest g0s) (conj faulty' (:out g0)) (conj check-gates g0))
              (recur (rest g0s) faulty' check-gates))))))))

(defn determine-flagged-gates [gates check-gates g3s faulty]
  (loop [check-gates check-gates faulty' faulty]
    (if (empty? check-gates)
      faulty'
      (let [{:keys [w1]} (first check-gates)
            intended       (str "z" (subs w1 1))
            matches        (filter (has-output intended) g3s)
            inputs         #{(:w1 (first matches)) (:w2 (first matches))}
            or-matches     (filter #(and ((is-type "OR") %)
                                         (inputs (:out %))) gates)
            or-output      (:out (first or-matches))
            correct-output (first (set/difference inputs #{or-output}))]
        (recur (rest check-gates) (conj faulty' correct-output))))))

(defn part2 [input]
  (let [[_ gates] (parse input)
        g0s     (filter #(and (is-direct? %) ((is-type "XOR") %)) gates)
        g3s     (filter #(and (not (is-direct? %)) ((is-type "XOR") %)) gates)
        outputs (filter is-output? gates)
        faulty  (set (map :out (concat (filter faulty-FAGate0? g0s)
                                       (filter #(not (is-output? %)) g3s)
                                       (filter faulty-output? outputs))))
        [check-gates faulty] (faulty-0-3-connections g0s g3s faulty)
        faulty  (determine-flagged-gates gates check-gates g3s faulty)]
    (str/join "," (sort faulty))))
