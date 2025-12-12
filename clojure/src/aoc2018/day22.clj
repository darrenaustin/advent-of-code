;; https://adventofcode.com/2018/day/22
 (ns aoc2018.day22
   (:require
    [aoc.day :as d]
    [aoc.util.grid :as g]
    [aoc.util.math :as m]
    [aoc.util.memoize :refer [let-memoized]]
    [aoc.util.pathfinding :as pf]
    [aoc.util.string :as s]
    [aoc.util.vec :as v]))

(defn input [] (d/day-input 2018 22))

(defn parse [input]
  (let [[depth tx ty] (s/parse-ints input)]
    [depth [tx ty]]))

(defn erosion-for [depth target]
  (let-memoized
   [erosion (fn [[x y :as loc]]
              (mod (+ depth
                      (cond
                        (= loc [0 0]) 0
                        (= loc target) 0
                        (zero? y) (* x 16807)
                        (zero? x) (* y 48271)
                        :else (* (erosion [(dec x) y])
                                 (erosion [x (dec y)]))))
                   20183))]
   erosion))

(defn region-for [depth target]
  (let [erosion (erosion-for depth target)]
    (memoize (fn [loc] (mod (erosion loc) 3)))))

(def rocky 0)
(def wet 1)
(def narrow 2)
(def tools [rocky wet narrow])

(def allowed-tools
  {rocky  #{:climbing :torch}
   wet    #{:climbing :neither}
   narrow #{:torch :neither}})

(def distance-to
  (into {} (for [from tools, tool (allowed-tools from), to tools
                 :let [other-tool (first (disj (allowed-tools from) tool))
                       dist       (if ((allowed-tools to) tool)
                                    [1 tool]
                                    [8 other-tool])]]
             [[from tool to] dist])))

(defn neighbors-for [region target]
  (fn [[loc tool :as state]]
    (let [ns (into {} (for [n-loc (remove (fn [[x y]] (or (neg? x) (neg? y)))
                                          (v/orthogonal-to loc))
                            :let [[dist tool] (distance-to [(region loc)
                                                            tool
                                                            (region n-loc)])]]
                        [[n-loc tool] dist]))]
      (if (= state [target :climbing])
        ; special case where we reached the target, but don't have the torch.
        (assoc ns [loc :torch] 7)
        ns))))

(defn part1 [input]
  (let [[depth target] (parse input)
        region (region-for depth target)]
    (m/sum (map region (g/area-locs [0 0] target)))))

(defn part2 [input]
  (let [[depth target] (parse input)
        neighbors (neighbors-for (region-for depth target) target)]
    (pf/dijkstra-distance [[0 0] :torch] neighbors #{[target :torch]})))
