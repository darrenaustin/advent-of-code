;; https://adventofcode.com/2024/day/21
(ns aoc2024.day21
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.grid :as g]
   [aoc.util.math :as m]
   [aoc.util.pathfinding :as pf]
   [aoc.util.pos :as p]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2024 21))

(defn parse-codes [input] (s/lines input))

(def dir->arrow
  {p/dir-up    "^"
   p/dir-down  "v"
   p/dir-left  "<"
   p/dir-right ">"
   p/origin    ""})

(defn vec-diffs [vs]
  (map p/pos- (rest vs) vs))

(defn neighbors [key-map]
  (fn [loc] (g/init-grid (filter key-map (p/orthogonal-to loc)) 1)))

(defn paths-map [coord-map key-map]
  (into {} (for [a (keys coord-map) b (keys coord-map)]
             [[a b]
              (mapv (fn [p] (str/join (map dir->arrow (vec-diffs p))))
                    (pf/dijkstra-paths (coord-map a)
                                       (neighbors key-map)
                                       #{(coord-map b)}))])))

(def num-pad
  {\A [2 3]
   \0 [1 3]
   \1 [0 2]
   \2 [1 2]
   \3 [2 2]
   \4 [0 1]
   \5 [1 1]
   \6 [2 1]
   \7 [0 0]
   \8 [1 0]
   \9 [2 0]})

(def number-pad-paths (paths-map num-pad (c/vals->keys num-pad)))

(def dir-pad-coords
  {\A [2 0]
   \^ [1 0]
   \< [0 1]
   \v [1 1]
   \> [2 1]})

(def dir-pad-paths (paths-map dir-pad-coords (c/vals->keys dir-pad-coords)))

(defn paths-for [pad-paths start code]
  (loop [pos start code code paths nil]
    (if-let [c (first code)]
      (recur c (rest code)
             (for [p (or paths [""]) np (or (pad-paths [pos c]) [""])]
               (str p np \A)))
      paths)))

(def min-length-dir-pad
  (memoize
   (fn [code num-robots]
     (if (zero? num-robots)
       (count code)
       (loop [min-count 0 curr \A code code]
         (if-let [c (first code)]
           (let [paths (map str (or (dir-pad-paths [curr c]) [""]) (repeat \A))]
             (recur (+ min-count (apply min (map #(min-length-dir-pad % (dec num-robots)) paths)))
                    c (rest code)))
           min-count))))))

(defn min-presses [code num-robots]
  (let [num-pad-paths (paths-for number-pad-paths \A code)]
    (apply min (map #(min-length-dir-pad % num-robots) num-pad-paths))))

(defn complexity [code num-robots]
  (* (first (s/ints code))
     (min-presses code num-robots)))

(defn solve [input num-robots]
  (m/sum (map #(complexity % num-robots) (parse-codes input))))

(defn part1 [input] (solve input 2))

(defn part2 [input] (solve input 25))
