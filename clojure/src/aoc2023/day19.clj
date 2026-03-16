;; https://adventofcode.com/2023/day/19
(ns aoc2023.day19
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2023 19))

(defn- parse-rule [rule]
  (let [[_ r _ op v d] (re-find #"(\w+)((<|>)(\d+):(\w+))*" rule)]
    (if (nil? op)
      {:dest (keyword r)}
      {:attr (keyword r)
       :rel (case op "<" < ">" >)
       :value (s/int v)
       :dest (keyword d)})))

(defn- parse-workflows [block]
  (into {}
        (map (fn [workflow]
               (let [[_ name rules] (re-find #"(\w+)\{(.*)\}" workflow)]
                 [(keyword name) (map parse-rule (str/split rules #","))])))
        (s/lines block)))

(defn- parse-parts [block]
  (map (fn [part]
         (into {} (for [[_ r v] (re-seq #"(\w)=(\d+)" part)]
                    [(keyword r) (s/int v)])))
       (s/lines block)))

(defn- accepted? [workflows part]
  (loop [current :in]
    (case current
      :A true
      :R false
      (recur (some (fn [{:keys [attr rel value dest]}]
                     (when (or (nil? attr) (rel (part attr) value)) dest))
                   (workflows current))))))

(defn- constrain [rules constraints]
  (loop [[{:keys [attr rel value dest]} & rules] rules
         constrained []
         constraints constraints]
    (cond
      (nil? dest) constrained
      (nil? rel)  (conj constrained [dest constraints])
      (= rel >)   (let [[mn mx] (constraints attr)]
                    (recur rules
                           (conj constrained [dest (assoc constraints attr [(inc value) mx])])
                           (assoc constraints attr [mn value])))
      (= rel <)   (let [[mn mx] (constraints attr)]
                    (recur rules
                           (conj constrained [dest (assoc constraints attr [mn (dec value)])])
                           (assoc constraints attr [value mx]))))))

(defn- num-accepted [workflows]
  (loop [open (c/queue [:in (zipmap [:x :m :a :s] (repeat [1 4000]))]), accepted 0]
    (if-let [[name constraints] (peek open)]
      (case name
        :R (recur (pop open) accepted)
        :A (recur (pop open)
                  (+ accepted (reduce * (map (fn [[mn mx]] (- (inc mx) mn)) (vals constraints)))))
        (recur (reduce conj (pop open) (constrain (workflows name) constraints)) accepted))
      accepted)))

(defn part1 [input]
  (let [[workflows parts] (s/parse-blocks input [parse-workflows parse-parts])]
    (transduce (comp (filter #(accepted? workflows %))
                     (mapcat vals))
               + 0 parts)))

(defn part2 [input]
  (num-accepted (first (s/parse-blocks input [parse-workflows]))))
