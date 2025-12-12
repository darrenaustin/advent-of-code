;; https://adventofcode.com/2024/day/15
(ns aoc2024.day15
  (:require
   [aoc.day :as d]
   [aoc.util.grid :as g]
   [aoc.util.math :as m]
   [aoc.util.vec :as v]
   [clojure.string :as str]))

(defn input [] (d/day-input 2024 15))

(def robot-dirs
  {\^ v/dir-up
   \v v/dir-down
   \< v/dir-left
   \> v/dir-right})

(defn parse [input grid-fn]
  (let [[grid-data instructions] (str/split input #"\n\n")
        grid (g/parse-grid (grid-fn grid-data))
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
                       (map vector (map v/vec+ locs (repeat dir)) cells)))))

(defn moveable-in-dir [grid loc dir]
  (loop [blocks #{loc} check [loc]]
    (if (seq check)
      (let [loc    (v/vec+ (first check) dir)
            check' (subvec check 1)]
        (case (grid loc)
          \# (recur nil nil)
          \. (recur blocks check')
          \O (recur (conj blocks loc) (conj check' loc))
          \[ (let [pair-loc (v/vec+ loc v/dir-right)]
               (if (= dir v/dir-right)
                 (recur (conj blocks loc pair-loc) (conj check' pair-loc))
                 (recur (conj blocks loc pair-loc) (conj check' loc pair-loc))))
          \] (let [pair-loc (v/vec+ loc v/dir-left)]
               (if (= dir v/dir-left)
                 (recur (conj blocks loc pair-loc) (conj check' pair-loc))
                 (recur (conj blocks loc pair-loc) (conj check' loc pair-loc))))))
      blocks)))

(defn move-robot [grid dir]
  (let [robot (g/loc-where grid #{\@})]
    (if-let [blocks (moveable-in-dir grid robot dir)]
      (move-cells grid blocks dir)
      grid)))

(defn solve [input box grid-fn]
  (let [[grid dirs] (parse input grid-fn)]
    (m/sum
     (map (fn [[x y]] (+ (* 100 y) x))
          (g/locs-where (reduce (fn [g d] (move-robot g d)) grid dirs)
                        #{box})))))

;; TODO: speed up
(defn part1 [input] (solve input \O identity))

;; TODO: speed up
(defn part2 [input] (solve input \[ double-grid))
