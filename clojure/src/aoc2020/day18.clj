;; https://adventofcode.com/2020/day/18
 (ns aoc2020.day18
   (:require
    [aoc.day :as d]
    [aoc.util.math :refer [sum]]
    [aoc.util.string :as s]
    [clojure.edn :as edn]
    [clojure.walk :as walk]))

(defn input [] (d/day-input 2020 18))

(defn- parse-line [s]
  ;; We are only evaluating lists during the tree walk,
  ;; so wrap the top level in a list.
  (edn/read-string (str "(" s ")")))

(def ops {'+ +, '* *})

(defn- equal-precedence [_ _] true)

(defn- addition-precedence [op1 _] (= op1 '+))

(defn- eval-list
  [has-precedence? tokens]
  (letfn [(eval-tokens [result [op arg & rst]]
            (if op
              (if (has-precedence? op (first rst))
                ;; op has greater or equal precedence, so perform op first
                (recur ((ops op) result arg) rst)
                ;; op is lower precedence, so do it after the rest
                ((ops op) result (eval-tokens arg rst)))
              result))]
    (eval-tokens (first tokens) (rest tokens))))

(defn- eval-expr [op-compare expr]
  (walk/postwalk #(if (list? %) (eval-list op-compare %) %) expr))

(defn- compute [input op-compare]
  (->> (s/lines input)
       (map parse-line)
       (map (partial eval-expr op-compare))
       sum))

(defn part1 [input] (compute input equal-precedence))

(defn part2 [input] (compute input addition-precedence))
