;; https://adventofcode.com/2020/day/7
(ns aoc2020.day07
  (:require
   [aoc.day :as d]
   [aoc.util.math :refer [sum]]
   [aoc.util.memoize :refer [letfn-mem]]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2020 7))

(defn parse-rule [s]
  (when-not (re-find #"no other bags" s)
    ;; Matches "[optional-num name] bag"" which is used for both parent and children
    (let [[[_ parent] & contents] (map rest (re-seq #"(\d+)? ?(\w+ \w+) bag" s))
          children (->> contents
                        (map (fn [[n name]] {name (s/int n)}))
                        (apply merge))]
      {parent children})))

(defn- parse-rules [input]
  (into {} (map parse-rule) (s/lines input)))

(defn- parents-graph [rules]
  (reduce-kv
   (fn [graph parent contents]
     (reduce-kv (fn [g child _]
                  (update g child (fnil conj #{}) parent))
                graph
                contents))
   {}
   rules))

(defn num-bags-in [rules bag]
  (letfn-mem
   [(num-bags [bag]
              (->> (rules bag)
                   (map (fn [[b n]] (* n (inc (num-bags b)))))
                   sum))]
   (num-bags bag)))

(defn part1 [input]
  (let [parents (parents-graph (parse-rules input))]
    (->> (tree-seq (constantly true) parents "shiny gold")
         set
         count
         dec)))

(defn part2 [input]
  (num-bags-in (parse-rules input) "shiny gold"))
