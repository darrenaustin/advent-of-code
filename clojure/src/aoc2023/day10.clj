;; https://adventofcode.com/2023/day/10
(ns aoc2023.day10
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.grid :as g]
   [aoc.util.pos :as p]))

(defn input [] (d/day-input 2023 10))

(def pipe-dirs
  {\| {p/dir-n p/dir-n, p/dir-s p/dir-s},
   \- {p/dir-w p/dir-w, p/dir-e p/dir-e},
   \L {p/dir-s p/dir-e, p/dir-w p/dir-n},
   \J {p/dir-e p/dir-n, p/dir-s p/dir-w},
   \7 {p/dir-e p/dir-s, p/dir-n p/dir-w},
   \F {p/dir-n p/dir-e, p/dir-w p/dir-s}})

(defn- path-from [grid start dir]
  (loop [path [start], current (p/pos+ start dir), dir dir]
    (if (= current start)
      path
      (when-let [dir' (get-in pipe-dirs [(grid current) dir])]
        (recur (conj path current) (p/pos+ current dir') dir')))))

(defn- parse-field [input]
  (let [grid (g/->grid input)
        start (first (c/keys-when-val #{\S} grid))
        path (first (keep #(path-from grid start %) p/adjacent-dirs))
        start-dirs (set [(p/pos- start (second path)) (p/pos- start (last path))])
        start-pipe (first (c/keys-when-val #(= start-dirs (set (keys %))) pipe-dirs))
        path? (set path)]
    {:grid (g/map->sparse-grid (c/filter-map #(path? (key %)) (assoc grid start start-pipe)))
     :path path?}))

(defn- enclosed-in-row [grid path row]
  (loop [[col & cols] (range (g/width grid)), enclosed 0, crossings 0]
    (if (nil? col)
      enclosed
      (let [pos [col row]]
        (if (path pos)
          (recur cols enclosed (if (#{\| \L \J} (grid pos)) (inc crossings) crossings))
          (recur cols (if (odd? crossings) (inc enclosed) enclosed) crossings))))))

(defn- path-enclosed [input]
  (let [{:keys [grid path]} (parse-field input)]
    (transduce (map (partial enclosed-in-row grid path))
               +
               (range (g/height grid)))))

(defn part1 [input]
  (/ (count (:path (parse-field input))) 2))

(defn part2 [input] (path-enclosed input))
