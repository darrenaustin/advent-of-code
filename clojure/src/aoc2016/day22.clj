;; https://adventofcode.com/2016/day/22
 (ns aoc2016.day22
   (:require
    [aoc.day :as d]
    [aoc.util.math :as m]
    [aoc.util.pathfinding :as path]
    [aoc.util.pos :as p]
    [aoc.util.string :as s]
    [clojure.math.combinatorics :as combo]))

(defn input [] (d/day-input 2016 22))

(defn- parse-node [line]
  (let [[x y size used avail] (s/pos-ints line)]
    {:pos [x y], :size size, :used used, :avail avail}))

(defn- parse-nodes [input]
  (map parse-node (drop 2 (s/lines input))))

(defn- viable-pair? [[n1 n2]]
  (or (and (not (zero? (:used n1))) (<= (:used n1) (:avail n2)))
      (and (not (zero? (:used n2))) (<= (:used n2) (:avail n1)))))

(defn- moveable-nodes [input]
  (->> (combo/combinations (parse-nodes input) 2)
       (filter viable-pair?)
       flatten
       set))

(defn- node-grid [input]
  ;; For simplicity, the following is assumed:
  ;; - There is only one empty node.
  ;; - Each other node is either too big to move, or can only
  ;;   be moved into the empty node.
  ;; These are true for the example and my input.
  (reduce (fn [g n]
            (assoc g (:pos n) n
                   :empty (if (zero? (:used n)) (:pos n) (:empty g))
                   :width (max (first (:pos n)) (:width g))))
          {:width 0}
          (moveable-nodes input)))

(defn part1 [input]
  (->> (combo/combinations (parse-nodes input) 2)
       (filter viable-pair?)
       count))

(defn part2 [input]
  (let [{:keys [empty width] :as grid} (node-grid input)
        start {:goal [width 0], :empty empty}
        neighbors #(->> p/orthogonal-dirs
                        (map (partial p/pos+ (:empty %)))
                        (filter (partial contains? grid))
                        (map (fn [p] (if (= p (:goal %))
                                       {:goal (:empty %) :empty p}
                                       {:goal (:goal %) :empty p}))))
        goal? #(= (:goal %) [0 0])
        distance #(+ (m/manhattan-distance (:goal %) [0 0])
                     (m/manhattan-distance (:goal %) (:empty %)))]
    (path/a-star-cost start neighbors goal? :heuristic distance)))
