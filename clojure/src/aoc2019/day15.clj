;; https://adventofcode.com/2019/day/15
(ns aoc2019.day15
  (:require
   [aoc.day :as d]
   [aoc.util.grid :as g]
   [aoc.util.pathfinding :as pf]
   [aoc.util.vec :as v]
   [aoc2019.intcode :as i])
  (:import
   (clojure.lang PersistentQueue)))

(defn input [] (d/day-input 2019 15))

(def dirs {1 v/dir-n, 2 v/dir-s, 3 v/dir-w, 4 v/dir-e})

(defn generate-grid [input]
  (let [machine (i/init-machine (i/parse input))
        queue   (into PersistentQueue/EMPTY
                      (for [dir [1 2 3 4]] [machine dir [0 0]]))]
    (loop [queue queue, grid {[0 0] \.}]
      (if-let [[machine dir loc] (peek queue)]
        (let [loc' (v/vec+ loc (dirs dir))]
          (if (grid loc')
            (recur (pop queue) grid)
            (let [machine' (-> machine (i/update-io [dir] []) (i/execute))
                  response (first (:output machine'))
                  cell     ({0 \#, 1 \., 2 \O} response)
                  grid'    (assoc grid loc' cell)]
              (if (zero? response)
                (recur (pop queue) grid')
                (recur (into (pop queue)
                             (for [dir [1 2 3 4]] [machine' dir loc']))
                       grid')))))
        grid))))

(defn neighbors [grid]
  (fn [loc]
    (into {} (map (fn [l] [l 1])
                  (filter #(#{\O \.} (grid %))
                          (v/orthogonal-to loc))))))

(defn part1 [input]
  (let [grid (generate-grid input)]
    (pf/dijkstra-distance [0 0] (neighbors grid) #(= \O (grid %)))))

(defn part2 [input]
  (loop [time 0, grid (generate-grid input)]
    (let [vacuum (g/locs-where grid #{\.})]
      (if (empty? vacuum)
        time
        (recur (inc time)
               (apply assoc grid
                      (mapcat #(if (some #{\O} (map grid (v/orthogonal-to %)))
                                 [% \O]
                                 [% \.]) vacuum)))))))
