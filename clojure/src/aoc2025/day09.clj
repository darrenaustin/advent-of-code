;; https://adventofcode.com/2025/day/9
  (ns aoc2025.day09
    (:require
     [aoc.day :as d]
     [aoc.util.collection :as c]
     [aoc.util.grid2 :as g]
     [aoc.util.string :as s]
     [aoc.util.pos :as p]
     [clojure.string :as str]))

(defn input [] (d/day-input 2025 9))

(defn parse-tiles [input]
  (mapv s/parse-ints (str/split-lines input)))

(defn area [[x1 y1] [x2 y2]]
  (* (inc (abs (- x1 x2))) (inc (abs (- y1 y2)))))

(defn part1 [input]
  (let [tiles (parse-tiles input)]
    (apply max (for [[t1 t2] (c/pairs tiles)]
                 (area t1 t2)))))

(defn- make-coord-map [coords]
  (let [sorted (apply sorted-set coords)
        padded (concat [(dec (first sorted))] sorted [(inc (last sorted))])]
    (into {} (map-indexed (fn [i n] [n i]) padded))))

(defn compression-maps [coords]
  [(make-coord-map (map first coords))
   (make-coord-map (map second coords))])

(defn map-coords [[x-map y-map] [x y]] [(x-map x) (y-map y)])

(defn draw-line [grid [[lx1 ly1 :as l1] [lx2 ly2 :as l2] :as line] value]
  (reduce (fn [g p] (g/set-cell g p value))
          grid
          (for [x (range (min lx1 lx2) (inc (max lx1 lx2)))
                y (range (min ly1 ly2) (inc (max ly1 ly2)))]
            [x y])))

(defn draw-lines [grid lines value]
  (reduce (fn [g l] (draw-line g l value)) grid lines))

(defn flood-fill [grid start value]
  (loop [grid grid, [seed & more] #{start}]
    (cond
      (nil? seed) grid
      (or (not (g/in-grid? grid seed)) (g/cell grid seed)) (recur grid more)
      :else (recur (g/set-cell grid seed value)
                   (apply conj more (p/orthogonal-to seed))))))

(defn build-sum-grid [grid]
  (let [w (g/width grid), h (g/height grid)]
    (loop [y 0, sums (transient (vec (repeat (inc h) (vec (repeat (inc w) 0)))))]
      (if (= y h)
        (persistent! sums)
        (let [prev-row (nth sums y)
              row (loop [x 0, sum 0, row (transient [0])]
                    (if (= x w)
                      (persistent! row)
                      (let [row-sum (+ sum (g/cell grid [x y] 0))
                            total-sum (+ (nth prev-row (inc x)) row-sum)]
                        (recur (inc x) row-sum (conj! row total-sum)))))]
          (recur (inc y) (assoc! sums (inc y) row)))))))

(defn rect-sum [sum-grid [x1 y1] [x2 y2]]
  (let [[x-low x-high] (if (< x1 x2) [x1 (inc x2)] [x2 (inc x1)])
        [y-low y-high] (if (< y1 y2) [y1 (inc y2)] [y2 (inc y1)])]
    (+ (g/cell sum-grid [x-high y-high])
       (g/cell sum-grid [x-low y-low])
       (- (g/cell sum-grid [x-low y-high]))
       (- (g/cell sum-grid [x-high y-low])))))

(defn part2 [input]
  (let [tiles (parse-tiles input)
        [x-cmp y-cmp :as raw->cmp] (compression-maps tiles)
        cmp->raw (map c/vals->keys raw->cmp)
        verts (mapv #(map-coords raw->cmp %) tiles)
        lines (conj (map vector (drop-last verts) (drop 1 verts))
                    [(last verts) (first verts)])
        grid (-> (g/make-grid (count x-cmp) (count y-cmp))
                 (draw-lines lines 0)
                 (flood-fill [0 0] 1))
        sum-grid (build-sum-grid grid)]
    (apply max (for [[vert1 vert2] (c/pairs verts)
                     :when (zero? (rect-sum sum-grid vert1 vert2))]
                 (area (map-coords cmp->raw vert1)
                       (map-coords cmp->raw vert2))))))
