;; https://adventofcode.com/2017/day/7
 (ns aoc2017.day07
   (:require
    [aoc.day :as d]
    [aoc.util.collection :as c]
    [aoc.util.string :as s]
    [clojure.set :as set]
    [clojure.string :as str]))

(defn input [] (d/day-input 2017 7))

(defn- parse-program [line]
  (let [[_ name weight children] (re-find #"(\w+) \((\d+)\)(?: -> (.*))?" line)]
    {:name name
     :weight (s/int weight)
     :children (when children (str/split children #", "))}))

(defn- parse-tower [input]
  (let [tower    (c/map-by :name (map parse-program (s/lines input)))
        parents  (set (keys tower))
        children (set (mapcat :children (vals tower)))
        root     (first (set/difference parents children))]
    (assoc tower :root root)))

(defn- find-imbalance [tower]
  (letfn [(imbalance
            ;; Recursively query the nodes for either their {:weight w}
            ;; or a {:fix w} for the imbalance found at their level.
            [program]
            (let [node (tower program)
                  results (map imbalance (:children node))]
              (if-let [fix (some #(when (:fix %) %) results)]
                fix
                (let [weights (zipmap (:children node) (map :weight results))
                      groups  (sort-by (comp count val) (group-by val weights))]
                  (if (or (empty? weights) (= 1 (count groups)))
                    {:weight (+ (:weight node) (reduce + (map :weight results)))}
                    (let [[[bad-total [[bad-child & _]]] [target-total]] groups
                          bad-node-weight (:weight (tower bad-child))]
                      {:fix (+ bad-node-weight (- target-total bad-total))}))))))]
    (:fix (imbalance (:root tower)))))

(defn part1 [input] (:root (parse-tower input)))

(defn part2 [input] (find-imbalance (parse-tower input)))
