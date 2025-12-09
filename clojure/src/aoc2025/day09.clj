;; https://adventofcode.com/2025/day/9
 (ns aoc2025.day09
   (:require
    [aoc.day :as d]
    [aoc.util.string :as s]
    [clojure.string :as str]))

(defn input [] (d/day-input 2025 9))

(defn parse-tiles [input]
  (mapv s/parse-ints (str/split-lines input)))

(defn area [[x1 y1] [x2 y2]]
  (* (inc (abs (- x1 x2))) (inc (abs (- y1 y2)))))

(defn part1 [input]
  (let [tiles (parse-tiles input)]
    (apply max (for [t1 (range (dec (count tiles)))
                     t2 (range (inc t1) (count tiles))
                     :let [tile1 (nth tiles t1) tile2 (nth tiles t2)]]
                 (area tile1 tile2)))))

(defn vertical? [[[px1 _] [px2 _]]] (= px1 px2))

(defn on? [[px py :as p] [[lx1 ly1 :as l1] [lx2 ly2 :as l2] :as line]]
  (if (vertical? line)
    (and (= px lx1)
         (<= (min ly1 ly2) py (max ly1 ly2)))
    (and (= py ly1)
         (<= (min lx1 lx2) px (max lx1 lx2)))))

(defn point-in-poly? [lines [px py :as p]]
  (loop [crossings 0, [[[lx1 ly1 :as l1] [_ ly2 :as l2] :as line] & lines] lines]
    (cond
      (nil? line) (odd? crossings)
      (on? p line) true
      (vertical? line) (if (and (< px lx1)
                                (<= (min ly1 ly2) py (dec (max ly1 ly2))))
                         (recur (inc crossings) lines)
                         (recur crossings lines))
      :else (recur crossings lines))))

;; TODO: this is very slow and messy
(defn rect-valid? [lines tiles [x1 y1] [x2 y2]]
  (let [min-x (min x1 x2) max-x (max x1 x2)
        min-y (min y1 y2) max-y (max y1 y2)
        c1 [x2 y1] c2 [x1 y2]
        center [(/ (+ x1 x2) 2.0) (/ (+ y1 y2) 2.0)]]
    (and (point-in-poly? lines c1)
         (point-in-poly? lines c2)
         (point-in-poly? lines center)
         ;; No vertex strictly inside
         (not-any? (fn [[vx vy]]
                     (and (< min-x vx max-x)
                          (< min-y vy max-y)))
                   tiles)
         ;; No edge strictly crosses
         (not-any? (fn [[[lx1 ly1] [lx2 ly2] :as line]]
                     (if (vertical? line)
                       (and (< min-x lx1 max-x)
                            (<= (min ly1 ly2) min-y)
                            (>= (max ly1 ly2) max-y))
                       ;; horizontal
                       (and (< min-y ly1 max-y)
                            (<= (min lx1 lx2) min-x)
                            (>= (max lx1 lx2) max-x))))
                   lines))))

(defn part2 [input]
  (let [tiles (parse-tiles input)
        lines (conj (map vector (drop-last tiles) (drop 1 tiles))
                    [(last tiles) (first tiles)])]
    (apply max (for [t1 (range (dec (count tiles)))
                     t2 (range (inc t1)  (count tiles))
                     :let [tile1 (nth tiles t1) tile2 (nth tiles t2)]]
                 (if (rect-valid? lines tiles tile1 tile2)
                   (area tile1 tile2)
                   0)))))
