;; https://adventofcode.com/2024/day/15
(ns aoc2024.day15
  (:require
    [aoc.day :as d]
    [aoc.util.grid :refer :all]
    [aoc.util.math :as m]
    [clojure.string :as str]))

(def input (d/day-input 2024 15))

(def robot-dirs
  {\^ dir-up
   \v dir-down
   \< dir-left
   \> dir-right})

(defn parse [input grid-fn]
  (let [[grid-data instructions] (str/split input #"\n\n")
        grid (parse-grid (grid-fn grid-data))
        dirs (map robot-dirs (str/replace instructions #"\s" ""))]
    [grid dirs]))

(defn double-grid [data]
  (apply str
         (reduce (fn [s [p v]] (str/replace s p v))
                 data
                 {#"#"  "##"
                  #"O"  "[]"
                  #"\." ".."
                  #"@"  "@."})))

(defn move-cells [grid locs dir]
  (let [cells (map grid locs)]
    (apply assoc grid
           (concat (mapcat vector locs (repeat \.))
                   (mapcat vector (map vec+ locs (repeat dir)) cells)))))

(defn moveable-in-dir [grid loc dir]
  (loop [blocks #{loc} check [loc]]
    (if (seq check)
      (let [loc    (vec+ (first check) dir)
            check' (subvec check 1)]
        (case (grid loc)
          \# (recur nil nil)
          \. (recur blocks check')
          \O (recur (conj blocks loc) (conj check' loc))
          \[ (let [pair-loc (vec+ loc dir-right)]
               (if (= dir dir-right)
                 (recur (conj blocks loc pair-loc) (conj check' pair-loc))
                 (recur (conj blocks loc pair-loc) (conj check' loc pair-loc))))
          \] (let [pair-loc (vec+ loc dir-left)]
               (if (= dir dir-left)
                 (recur (conj blocks loc pair-loc) (conj check' pair-loc))
                 (recur (conj blocks loc pair-loc) (conj check' loc pair-loc))))))
      blocks)))

(defn move-robot2 [grid dir]
  (let [robot (loc-where grid #{\@})]
    (if-let [blocks (moveable-in-dir grid robot dir)]
      (move-cells grid blocks dir)
      grid)))

(defn follow-directions [grid dirs move-fn]
  (reduce (fn [g d] (move-fn g d)) grid dirs))

(defn gps-coord [[x y]]
  (+ (* 100 y) x))

(defn solve [input box grid-fn]
  (let [[grid dirs] (parse input grid-fn)]
    (m/sum
      (map gps-coord
           (locs-where (follow-directions grid dirs move-robot2)
                       #{box})))))

;; TODO: speed up
(defn part1 [input] (solve input \O identity))

;; TODO: speed up
(defn part2 [input] (solve input \[ double-grid))
