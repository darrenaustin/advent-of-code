;; https://adventofcode.com/2024/day/15
(ns aoc2024.day15
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.grid-vec :as g]
   [aoc.util.math :as m]
   [aoc.util.pos :as p]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2024 15))

(def robot-dirs
  {\^ p/dir-up
   \v p/dir-down
   \< p/dir-left
   \> p/dir-right})

(defn parse [input grid-fn]
  (let [[grid-data instructions] (s/blocks input)
        grid (g/str->grid-vec (grid-fn grid-data))
        dirs (map robot-dirs (str/replace instructions #"\s" ""))]
    [grid dirs]))

(defn double-grid [data]
  (str/join
   (reduce (fn [s [p v]] (str/replace s p v))
           data
           {#"#"  "##"
            #"O"  "[]"
            #"\." ".."
            #"@"  "@."})))

(defn move-cells [grid locs dir]
  (let [cells (map grid locs)]
    (into grid (concat (map vector locs (repeat \.))
                       (map vector (map p/pos+ locs (repeat dir)) cells)))))

(defn moveable-in-dir [grid loc dir]
  (loop [blocks #{loc} check [loc]]
    (if (seq check)
      (let [loc    (p/pos+ (first check) dir)
            check' (subvec check 1)]
        (case (grid loc)
          \# (recur nil nil)
          \. (recur blocks check')
          \O (recur (conj blocks loc) (conj check' loc))
          \[ (let [pair-loc (p/pos+ loc p/dir-right)]
               (if (= dir p/dir-right)
                 (recur (conj blocks loc pair-loc) (conj check' pair-loc))
                 (recur (conj blocks loc pair-loc) (conj check' loc pair-loc))))
          \] (let [pair-loc (p/pos+ loc p/dir-left)]
               (if (= dir p/dir-left)
                 (recur (conj blocks loc pair-loc) (conj check' pair-loc))
                 (recur (conj blocks loc pair-loc) (conj check' loc pair-loc))))))
      blocks)))

(defn move-robot [grid dir]
  (let [robot (first (c/keys-when-val #{\@} grid))]
    (if-let [blocks (moveable-in-dir grid robot dir)]
      (move-cells grid blocks dir)
      grid)))

(defn solve [input box grid-fn]
  (let [[grid dirs] (parse input grid-fn)]
    (m/sum
     (map (fn [[x y]] (+ (* 100 y) x))
          (c/keys-when-val #{box}
                           (reduce (fn [g d] (move-robot g d)) grid dirs))))))

;; TODO: speed up
(defn part1 [input] (solve input \O identity))

;; TODO: speed up
(defn part2 [input] (solve input \[ double-grid))
