;; https://adventofcode.com/2024/day/24
(ns aoc2024.day24
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]
   [clojure.set :as set]
   [clojure.string :as str]))

(defn input [] (d/day-input 2024 24))

(defn parse-gate [input]
  (let [[a op b out] (rest (re-find #"(.*) (AND|OR|XOR) (.*) -> (.*)" input))]
    {:a a :op op :b b :out out}))

(defn parse [input]
  (let [[init-data conn-data] (str/split input #"\n\n")]
    [(update-vals (into {} (map #(str/split % #": ")
                                (s/lines init-data)))
                  read-string)
     (map parse-gate (s/lines conn-data))]))

(defn wires-of [gates]
  (apply set/union (map (fn [g] #{(:a g) (:b g) (:out g)}) gates)))

(defn wires->integer [wires]
  (read-string (str "2r" (str/join wires))))

(defn open-gates [values gates]
  (filter #(nil? (values (:out %))) gates))

(defn eval-gate [values gate]
  (let [{:keys [a b op out]} gate]
    {out (case op
           "AND" (bit-and (values a) (values b))
           "OR" (bit-or (values a) (values b))
           "XOR" (bit-xor (values a) (values b)))}))

(defn simulate [[initial-states gates]]
  (let [wires   (wires-of gates)
        z-wires (reverse (sort (filter #(str/starts-with? % "z") wires)))]
    (loop [values initial-states]
      (let [z-values (map values z-wires)]
        (if (not-any? nil? z-values)
          (wires->integer z-values)
          (let [has-inputs (filter #(and (values (:a %)) (values (:b %)))
                                   (open-gates values gates))
                output     (apply merge (map #(eval-gate values %) has-inputs))]
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
(defn direct-gate? [gate]
  (or (str/starts-with? (:a gate) "x")
      (str/starts-with? (:b gate) "x")))

(defn output-gate? [gate]
  (str/starts-with? (:out gate) "z"))

(defn has-input? [gate input]
  (or (= (:a gate) input)
      (= (:b gate) input)))

(defn faulty-gate0? [{:keys [a b out] :as gate}]
  (let [first? (or (= a "x00") (= b "x00"))]
    (or (and first? (not= out "z00"))
        (and (not first?) (output-gate? gate)))))

(defn faulty-gate3? [gate]
  (not (output-gate? gate)))

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
          (let [matches (filter #(has-input? % (:out g0)) g3s)]
            (if (empty? matches)
              (recur (rest g0s) (conj faulty' (:out g0)) (conj check-gates g0))
              (recur (rest g0s) faulty' check-gates))))))))

(defn determine-flagged-expected [gates check-gates g3s faulty]
  (loop [check-gates check-gates faulty' faulty]
    (if (empty? check-gates)
      faulty'
      (let [{:keys [a]} (first check-gates)
            intended       (str "z" (subs a 1))
            matches        (filter #(= (:out %) intended) g3s)
            inputs         #{(:a (first matches)) (:b (first matches))}
            or-matches     (filter #(and (= "OR" (:op %))
                                         (inputs (:out %))) gates)
            or-output      (:out (first or-matches))
            correct-output (first (set/difference inputs #{or-output}))]
        (recur (rest check-gates) (conj faulty' correct-output))))))

(defn part2 [input]
  (let [[_ gates] (parse input)
        g0s     (filter #(and (direct-gate? %) (= "XOR" (:op %))) gates)
        g3s     (filter #(and (not (direct-gate? %)) (= "XOR" (:op %))) gates)
        outputs (filter output-gate? gates)
        faulty  (set (map :out (concat (filter faulty-gate0? g0s)
                                       (filter faulty-gate3? g3s)
                                       (filter faulty-output? outputs))))
        [check-gates faulty] (faulty-0-3-connections g0s g3s faulty)
        faulty  (determine-flagged-expected gates check-gates g3s faulty)]
    (str/join "," (sort faulty))))

;; (def faulty ["drg" "gvw" "jbp" "jgc" "qjb" "z15" "z22" "z35"])

;; (defn graph-vis [gates faulty]
;;   (let [inputs    (sort (set (mapcat (fn [g] [(:a g) (:b g)]) gates)))
;;         outputs   (sort (set (map :out gates)))
;;         xs        (filter #(str/starts-with? % "x") inputs)
;;         ys        (filter #(str/starts-with? % "y") inputs)
;;         zs        (filter #(str/starts-with? % "z") outputs)
;;         or-gates  (map-indexed (fn [i g] (assoc g :op (format "OR%02d" i)))
;;                                (filter #(= "OR" (:op %)) gates))
;;         xor-gates (map-indexed (fn [i g] (assoc g :op (format "XOR%02d" i)))
;;                                (filter #(= "XOR" (:op %)) gates))
;;         and-gates (map-indexed (fn [i g] (assoc g :op (format "AND%02d" i)))
;;                                (filter #(= "AND" (:op %)) gates))
;;         gates     (concat or-gates xor-gates and-gates)
;;         ors       (map :op or-gates)
;;         xors      (map :op xor-gates)
;;         ands      (map :op and-gates)]
;;     (println "strict digraph {")
;;     (println "  graph [")
;;     (println "    label = \"\\n\\nAdvent of Code 2024 day 24\"")
;;     (println "    fontsize=20")
;;     (println "  ]")
;;     (println "  subgraph {")
;;     (println "    rank=min")
;;     (println "    node[style=filled;color=\"#aaf\"];")
;;     (println "   " (str/join "; " (concat xs ys)))
;;     (println "  }")
;;     (println "  subgraph {")
;;     (println "    rank=max")
;;     (println "    node[style=filled;color=\"#afa\"];")
;;     (println "   " (str/join "; " zs))
;;     (println "  }")
;;     (println "  {")
;;     (println "    node[shape=rect;style=filled;color=lightgray];")
;;     (println "   " (str/join "; " (concat ands ors xors)))
;;     (println "  }")
;;     (println "  {")
;;     (println "    node[style=filled;color=red;pencolor=white];")
;;     (println "   " (str/join "; " faulty))
;;     (println "  }")
;;     (doseq [gate gates]
;;       (println " " (format "%s -> %s" (:a gate) (:op gate)))
;;       (println " " (format "%s -> %s" (:b gate) (:op gate)))
;;       (println " " (format "%s -> %s" (:op gate) (:out gate)))
;;       (println))
;;     (println "}")))
