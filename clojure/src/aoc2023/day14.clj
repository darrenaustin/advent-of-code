;; https://adventofcode.com/2023/day/14
(ns aoc2023.day14
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.grid :as g]))

(defn input [] (d/day-input 2023 14))

(defn- parse-dish [input]
  (let [grid  (g/->grid input)
        [w h] (g/size grid)
        {rocks :rocks, wall-cols :cols, wall-rows :rows}
        (reduce (fn [acc [x y]]
                  (case (grid [x y])
                    \O (update acc :rocks conj {:x x :y y})
                    \# (-> acc
                           (update-in [:cols x] (fnil conj []) y)
                           (update-in [:rows y] (fnil conj []) x))
                    acc))
                {:rocks #{} :cols {} :rows {}}
                (for [x (range w) y (range h)] [x y]))
        walls-by-col (into {} (for [x (range w)] [x (concat [-1] (wall-cols x []) [h])]))
        walls-by-row (into {} (for [y (range h)] [y (concat [-1] (wall-rows y []) [w])]))]
    {:rocks rocks
     :walls-in-dir
     {:north {:lines walls-by-col :group-axis :x :step 1}
      :west  {:lines walls-by-row :group-axis :y :step 1}
      :south {:lines (update-vals walls-by-col reverse) :group-axis :x :step -1}
      :east  {:lines (update-vals walls-by-row reverse) :group-axis :y :step -1}}
     :height h}))

(defn- tilt-line [rocks walls move-axis step]
  (let [order (case step -1 >, 1 <)]
    (loop [rocks          (sort order (map move-axis rocks))
           start          (first walls)
           [wall & walls] (rest walls)
           line           (transient [])]
      (if (nil? wall)
        (persistent! line)
        (let [[in-seg remaining] (split-with #(order % wall) rocks)]
          (recur remaining
                 wall
                 walls
                 (reduce conj! line
                         (take (count in-seg)
                               (iterate #(+ % step) (+ start step))))))))))

(defn- tilt [rocks {:keys [lines group-axis step]}]
  (let [move-axis (case group-axis :x :y, :y :x)]
    (into #{}
          (mapcat (fn [[n r]]
                    (map (fn [o] {group-axis n, move-axis o})
                         (tilt-line r (lines n) move-axis step))))
          (group-by group-axis rocks))))

(defn- cycle-dish [walls-in-dir rocks]
  (reduce tilt rocks (map walls-in-dir [:north :west :south :east])))

(defn- total-load [rocks height]
  (transduce (map #(- height (:y %))) + 0 rocks))

(defn part1 [input]
  (let [{:keys [rocks walls-in-dir height]} (parse-dish input)]
    (total-load (tilt rocks (:north walls-in-dir)) height)))

(defn part2 [input]
  (let [{:keys [rocks walls-in-dir height]} (parse-dish input)]
    (total-load (c/iteration-with-cycle 1000000000
                                        (partial cycle-dish walls-in-dir) rocks) height)))
