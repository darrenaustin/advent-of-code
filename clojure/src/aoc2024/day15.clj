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

(defn parse
  ([input] (parse input identity))
  ([input grid-fn]
   (let [[grid-data instructions] (str/split input #"\n\n")
         grid (parse-grid (grid-fn grid-data))
         dirs (map robot-dirs (str/replace instructions #"\s" ""))]
     [grid dirs])))

(defn double-grid [data]
  (apply str
         (reduce (fn [s [p v]] (str/replace s p v))
                 data
                 {#"#"  "##"
                  #"O"  "[]"
                  #"\." ".."
                  #"@"  "@."})))

(def moveable #{\O \@ \[ \]})

(defn space-in-dir [grid loc dir]
  (let [end (first (drop-while #(moveable (grid %))
                               (iterate #(vec+ % dir) loc)))]
    (when (= (grid end) \.)
      end)))

(defn move [grid from to]
  (assoc grid to (grid from) from \.))

(defn move-robot [grid dir]
  (let [robot (loc-where grid #{\@})]
    (if-let [space (space-in-dir grid robot dir)]
      (loop [grid grid loc space]
        (if (= loc robot)
          grid
          (let [prev-loc (vec- loc dir)]
            (recur (move grid prev-loc loc) prev-loc))))
      grid)))

(defn move-block [grid from to]
  (move (move grid from to) (vec+ from dir-right) (vec+ to dir-right)))

(defn add-block
  ([blocks block]
   (if (some #{block} blocks)
     blocks
     (conj blocks block))))

(defn moveable-in-dir [grid loc dir]
  (loop [blocks [] locs [loc]]
    (if-let [loc (first locs)]
      (let [next-locs (subvec locs 1)]
        (case (grid loc)
          \. (recur blocks next-locs)
          \@ (recur blocks (conj next-locs (vec+ loc dir)))
          \# (recur nil nil)
          \[ (let [[bx by] loc
                   next-loc1 (vec+ loc dir)
                   next-loc2 (vec+ [(inc bx) by] dir)]
               (recur (add-block blocks loc)
                      (conj next-locs next-loc1 next-loc2)))
          \] (let [[bx by] loc
                   next-loc1 (vec+ loc dir)
                   next-loc2 (vec+ [(dec bx) by] dir)]
               (recur (add-block blocks [(dec bx) by])
                      (conj next-locs next-loc1 next-loc2)))))
      blocks)))

(defn move-robot2 [grid dir]
  (if (zero? (second dir))
    (move-robot grid dir)
    (let [robot (loc-where grid #{\@})]
      (if-let [blocks (moveable-in-dir grid robot dir)]
        (move (reduce (fn [g b] (move-block g b (vec+ b dir)))
                      grid
                      (reverse blocks))
              robot (vec+ robot dir))
        grid))))

(defn follow-directions [grid dirs move-fn]
  (reduce (fn [g d] (move-fn g d)) grid dirs))

(defn gps-coord [[x y]]
  (+ (* 100 y) x))

;; TODO: clean up and speed up
(defn part1 [input]
  (let [[grid dirs] (parse input)]
    (m/sum (map gps-coord
                (locs-where (follow-directions grid dirs move-robot)
                            #{\O})))))

;; TODO: clean up and speed up
(defn part2 [input]
  (let [[grid dirs] (parse input double-grid)]
    (m/sum (map gps-coord
                (locs-where (follow-directions grid dirs move-robot2)
                            #{\[})))))
