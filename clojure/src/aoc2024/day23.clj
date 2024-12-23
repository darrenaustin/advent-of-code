;; https://adventofcode.com/2024/day/23
(ns aoc2024.day23
  (:require [aoc.day :as d]
            [clojure.math.combinatorics :as combo]
            [clojure.set :as set]
            [clojure.string :as str]))

(defn input [] (d/day-input 2024 23))

(defn parse-graph [input]
  (reduce (fn [graph [v1 v2]]
            (assoc graph
              v1 (conj (get graph v1 #{}) v2)
              v2 (conj (get graph v2 #{}) v1)))
          {}
          (map #(str/split % #"-") (str/split-lines input))))

(defn mutual? [graph n node-set]
  (or (node-set n) (empty? (set/difference node-set (graph n)))))

(defn clique-for [graph v]
  (loop [clique #{v} check (graph v)]
    (if (empty? check)
      clique
      (let [c (first check)]
        (if (mutual? graph c clique)
          (recur (conj clique c) (rest check))
          (recur clique (rest check)))))))

(defn cliques [graph]
  (set (mapv #(clique-for graph %) (keys graph))))

(defn connected? [graph [a b c]]
  (and
    (contains? (graph a) b)
    (contains? (graph a) c)
    (contains? (graph b) c)))

(defn connected-triples [graph including]
  (let [possible (filter (fn [i] (<= 2 (count (graph i)))) (keys graph))]
    (filter #(and
               (some including %)
               (connected? graph %))
            (combo/combinations possible 3))))

(defn part1 [input]
  (let [graph       (parse-graph input)
        t-nodes     (filter #(str/starts-with? % "t") (keys graph))]
    (count (connected-triples graph (set t-nodes)))))

(defn part2 [input]
  (str/join "," (sort (apply max-key count (cliques (parse-graph input))))))

(def t "kh-tc
qp-kh
de-cg
ka-co
yn-aq
qp-ub
cg-tb
vc-aq
tb-ka
wh-tc
yn-cg
kh-ub
ta-co
de-co
tc-td
tb-wq
wh-td
ta-ka
td-qp
aq-cg
wq-ub
ub-vc
de-ta
wq-aq
wq-vc
wh-yn
ka-de
kh-ta
co-tc
wh-qp
tb-vc
td-yn")

(def trips "
aq,cg,yn
aq,vc,wq
co,de,ka
co,de,ta
co,ka,ta
de,ka,ta
kh,qp,ub
qp,td,wh
tb,vc,wq
tc,td,wh
td,wh,yn
ub,vc,wq")

(def g
  {'A' ['B', 'C', 'D' 'E'],
   'B' ['A', 'C', 'D'],
   'C' ['A', 'B', 'D'],
   'D' ['A', 'B', 'C'],
   'E' ['F' 'A'],
   'F' ['E']})

(def foo
  #{#{"co" "de" "ta"}
    #{"co" "ka" "ta"}
    #{"de" "ka" "ta"}
    #{"qp" "td" "wh"}
    #{"tb" "vc" "wq"}
    #{"tc" "td" "wh"}
    #{"td" "wh" "yn"}
    })
