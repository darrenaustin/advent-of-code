;; https://adventofcode.com/2019/day/15
(ns aoc2019.day15
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.math :as m]
   [aoc.util.pathfinding :as path]
   [aoc.util.pos :as p]
   [aoc2019.intcode :as i])
  (:import
   (clojure.lang PersistentQueue)))

(defn input [] (d/day-input 2019 15))

(def dirs {1 p/dir-n, 2 p/dir-s, 3 p/dir-w, 4 p/dir-e})

(defn generate-grid [input]
  (let [machine (i/init-machine (i/parse input))
        queue   (into PersistentQueue/EMPTY
                      (for [dir [1 2 3 4]] [machine dir [0 0]]))]
    (loop [queue queue, grid {[0 0] \.}]
      (if-let [[machine dir loc] (peek queue)]
        (let [loc' (p/pos+ loc (dirs dir))]
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
    (filter #(#{\O \.} (grid %))
            (p/orthogonal-to loc))))

(defn part1 [input]
  (let [grid (generate-grid input)
        goal (some (fn [[k v]] (when (= v \O) k)) grid)
        heuristic (fn [loc] (m/manhattan-distance loc goal))]
    (path/a-star-cost [0 0] (neighbors grid) #(= \O (grid %)) :heuristic heuristic)))

(defn part2 [input]
  (loop [time 0, grid (generate-grid input)]
    (let [vacuum (c/keys-when-val #{\.} grid)]
      (if (empty? vacuum)
        time
        (recur (inc time)
               (apply assoc grid
                      (mapcat #(if (some #{\O} (map grid (p/orthogonal-to %)))
                                 [% \O]
                                 [% \.]) vacuum)))))))
