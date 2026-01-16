;; https://adventofcode.com/2019/day/24
 (ns aoc2019.day24
   (:require
    [aoc.day :as d]
    [aoc.util.collection :as c]
    [aoc.util.grid :as g]
    [aoc.util.pos :as p]))

(defn input [] (d/day-input 2019 24))

(defn- infested-grid-neighbors [grid pos]
  (count (filter (comp #{\#} grid) (p/orthogonal-to pos))))

(defn- step-grid [grid]
  (reduce
   (fn [g pos]
     (let [bug? (= \# (grid pos))
           neighbors (infested-grid-neighbors grid pos)]
       (assoc g pos (cond
                      (and bug? (= neighbors 1)) \#
                      (and (not bug?) (#{1 2} neighbors)) \#
                      :else \.))))
   grid
   (keys grid)))

(defn- biodiversity [grid]
  (->> (c/filter-vals #{\#} grid)
       keys
       (map #(bit-shift-left 1 (g/key-index grid %)))
       (reduce +)))

(def ^:private center [2 2])

(defn- level-neighbors [[level pos]]
  (mapcat (fn [new-pos]
            (if (= new-pos center)
              ;; Inner neighbors
              (let [dir (p/pos- new-pos pos)]
                (case dir
                  [+0 -1] (for [x (range 5)] [(inc level) [x 4]])   ; Down into top
                  [+0 +1] (for [x (range 5)] [(inc level) [x 0]])   ; Up into bottom
                  [-1 +0] (for [y (range 5)] [(inc level) [4 y]])   ; Right into left
                  [+1 +0] (for [y (range 5)] [(inc level) [0 y]]))) ; Left into right
              ;; Outer neighbors or same level
              (let [[x y] new-pos]
                (cond
                  (neg? x) [[(dec level) (p/pos+ center p/dir-left)]]
                  (neg? y) [[(dec level) (p/pos+ center p/dir-up)]]
                  (>= x 5) [[(dec level) (p/pos+ center p/dir-right)]]
                  (>= y 5) [[(dec level) (p/pos+ center p/dir-down)]]
                  :else    [[level new-pos]]))))
          (p/orthogonal-to pos)))

(def ^:private computed-transitions
  "Map of [pos] -> sequence of [level-delta neighbor-pos]"
  (into {} (for [x (range 5) y (range 5)
                 :let [pos [x y]]
                 :when (not= pos center)]
             [pos (level-neighbors [0 pos])])))

(defn- neighborhood [bugs]
  (mapcat (fn [[lvl pos]]
            (keep (fn [[dl dpos]]
                    [(+ lvl dl) dpos])
                  (computed-transitions pos)))
          bugs))

(defn- step-bugs [bugs]
  (let [counts (frequencies (neighborhood bugs))]
    (set (for [[loc n] counts
               :when (if (bugs loc) (= n 1) (#{1 2} n))]
           loc))))

(defn bugs-after [input mins]
  (let [initial-bugs (set (for [[pos ch] (g/str->grid input)
                                :when (= ch \#)]
                            [0 pos]))]
    (count (c/nth-iteration step-bugs initial-bugs mins))))

(defn part1 [input]
  (->> (g/str->grid input)
       (iterate step-grid)
       c/first-duplicate
       biodiversity))

(defn part2 [input] (bugs-after input 200))
