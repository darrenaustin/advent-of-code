;; https://adventofcode.com/2023/day/15
(ns aoc2023.day15
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2023 15))

(defn- label-hash [s]
  (reduce (fn [h c] (-> (+ h (int c)) (* 17) (rem 256))) 0 s))

(defn- parse-op [s]
  (let [[_ label op length] (re-find #"(\w+)(-|=)([\d]*)" s)]
    {:label label
     :box (label-hash label)
     :op-fn (case op "-" dissoc, "=" assoc)
     :focal-length (s/int length)}))

(defn- perform-op [boxes op]
  (let [{:keys [label box op-fn focal-length]} op]
    (update boxes box (fnil op-fn (array-map)) label focal-length)))

(defn- focusing-power [boxes]
  (reduce-kv (fn [p box-num lenses]
               (reduce + p (map-indexed (fn [i [_ l]]
                                          (* (inc box-num) (inc i) l))
                                        lenses)))
             0
             boxes))

(defn part1 [input]
  (transduce (map label-hash) + 0 (str/split input #",")))

(defn part2 [input]
  (focusing-power
   (transduce (map parse-op) (completing perform-op) {} (str/split input #","))))
