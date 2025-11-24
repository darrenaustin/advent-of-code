;; https://adventofcode.com/2020/day/20
 (ns aoc2020.day20
   (:require
    [aoc.day :as d]
    [aoc.util.grid2 :as g]
    [aoc.util.string :as s]
    [clojure.math :as math]
    [clojure.string :as str]))

(defn input [] (d/day-input 2020 20))

(defn parse-grid-tiles [input]
  (let [tile-blocks (str/split input #"\n\s*\n")]
    (mapv (fn [block]
            (let [[header & grid-rows] (str/split-lines block)
                  id (s/parse-int header)
                  grid (g/parse-grid grid-rows)]
              {:id   id
               :grid grid}))
          tile-blocks)))

(defn grid-permutations [grid]
  (let [r0   (delay grid)
        r90  (delay (g/rotate-clockwise @r0))
        r180 (delay (g/rotate-clockwise @r90))
        r270 (delay (g/rotate-clockwise @r180))
        fh   (delay (g/flip-horizontal @r0))
        fv   (delay (g/flip-vertical @r0))
        fh90 (delay (g/rotate-clockwise @fh))
        fv90 (delay (g/rotate-clockwise @fv))]
    [r0 r90 r180 r270 fh fv fh90 fv90]))

(defn tile-permutations [tiles]
  (into {} (map (fn [tile] [(:id tile) (grid-permutations (:grid tile))]) tiles)))

(defn tile-fits? [arrangement grid pos]
  (let [[row col]     pos
        top-neighbor  (get-in arrangement [[(dec row) col] :grid])
        left-neighbor (get-in arrangement [[row (dec col)] :grid])]
    (and
     (or (nil? top-neighbor) (= (g/top-row grid) (g/bottom-row top-neighbor)))
     (or (nil? left-neighbor) (= (g/left-column grid) (g/right-column left-neighbor))))))

(defn arrange-tiles
  ([tile-permutations-map]
   (let [square-size (int (math/sqrt (count tile-permutations-map)))
         positions (for [row (range square-size) col (range square-size)] [row col])]
     (arrange-tiles tile-permutations-map {} (set (keys tile-permutations-map)) positions)))

  ([tile-permutations-map arrangement available-tile-ids positions-left]
   (if (empty? positions-left)
     arrangement
     (let [pos (first positions-left)]
       (loop [tile-ids available-tile-ids]
         (when-let [tile-id (first tile-ids)]
           (if-let [valid-permutations (seq (filter #(tile-fits? arrangement @% pos) (tile-permutations-map tile-id)))]
             (if-let [result (first (keep #(arrange-tiles tile-permutations-map
                                                          (assoc arrangement pos {:id tile-id :grid @%})
                                                          (disj available-tile-ids tile-id)
                                                          (rest positions-left))
                                          valid-permutations))]
               result
               (recur (rest tile-ids)))
             (recur (rest tile-ids)))))))))

(defn part1 [input]
  (let [arrangement (-> input parse-grid-tiles tile-permutations arrange-tiles)
        square-edge (dec (int (math/sqrt (count arrangement))))]
    (reduce * (map #(get-in arrangement [% :id])
                   [[0 0]
                    [0 square-edge]
                    [square-edge 0]
                    [square-edge square-edge]]))))

(defn remove-borders [grid]
  (g/sub-grid grid [1 1] [(- (g/width grid) 2) (- (g/height grid) 2)]))

(defn assemble-image [tile-arrangement]
  (let [width-in-tiles (int (math/sqrt (count tile-arrangement)))
        tile-size (- (g/width (get-in tile-arrangement [[0 0] :grid])) 2)
        image-size (* width-in-tiles tile-size)]
    (loop [grid (g/make-grid image-size image-size)
           positions (keys tile-arrangement)]
      (if (empty? positions)
        grid
        (let [[tile-row tile-col] (first positions)
              tile-grid (remove-borders (get-in tile-arrangement [[tile-row tile-col] :grid]))]
          (recur (g/set-sub-grid grid [(* tile-col tile-size) (* tile-row tile-size)] tile-grid)
                 (rest positions)))))))

(defn sub-grid-matches-at [grid sub-grid pos]
  (let [[start-x start-y] pos
        [sub-width sub-height] [(g/width sub-grid) (g/height sub-grid)]]
    (loop [x 0 y 0]
      (cond
        (= y sub-height) true
        (= x sub-width) (recur 0 (inc y))
        :else (let [grid-cell     (g/cell grid [(+ start-x x) (+ start-y y)])
                    sub-grid-cell (g/cell sub-grid [x y])]
                ;; (println [x y] grid-cell sub-grid-cell [(+ start-x x) (+ start-y y)])
                (if (and sub-grid-cell (not= sub-grid-cell grid-cell))
                  false
                  (recur (inc x) y)))))))

(defn sub-grid-matches [grid sub-grid]
  (for [y (range (- (g/height grid) (g/height sub-grid) 1))
        x (range (- (g/width grid) (g/width sub-grid) 1))
        :when (sub-grid-matches-at grid sub-grid [x y])]
    [x y]))

(def sea-monster
  (g/parse-grid
   "..................#.
#....##....##....###
.#..#..#..#..#..#..."
   #(when (not= % \.) %)))

(defn filled? [cell] (= cell \#))

(defn part2 [input]
  (let [image (-> input parse-grid-tiles tile-permutations arrange-tiles assemble-image)
        permutations (grid-permutations image)
        sea-monsters-found (count (first (keep #(seq (sub-grid-matches @% sea-monster)) permutations)))]
    (- (count (g/locations-where image filled?))
       (* sea-monsters-found (count (g/locations-where sea-monster filled?))))))
