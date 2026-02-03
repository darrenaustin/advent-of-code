;; https://adventofcode.com/2019/day/20
 (ns aoc2019.day20
   (:require
    [aoc.day :as d]
    [aoc.util.grid :as g]
    [aoc.util.math :as m]
    [aoc.util.pathfinding :as path]
    [aoc.util.pos :as p]
    [aoc.util.string :as s]))

(defn input [] (d/day-input 2019 20 :trim? false))

(def ^:private letter? (set s/alphabet-upper))

(defn- named-in-dir [grid pos dir]
  (when-let [prefix (letter? (grid pos))]
    (let [pos' (p/pos+ pos dir)]
      (when-let [suffix (letter? (grid pos'))]
        (let [name (str prefix suffix)
              before (p/pos- pos dir)
              after (p/pos+ pos' dir)]
          [name (if (= \. (grid before)) before after)])))))

(defn- find-named [grid]
  (update-vals
   (group-by first
             (for [y (range (g/height grid)), x (range (g/width grid))
                   :let [named-right (named-in-dir grid [x y] p/dir-right)
                         named-down (named-in-dir grid [x y] p/dir-down)
                         named (or named-right named-down)]
                   :when named]
               named))
   #(concat (map second %))))

(defn build-portals [named]
  (reduce (fn [ps [e1 e2]] (assoc ps e1 e2, e2 e1)) {} (vals named)))

(defn- outer-portal? [grid [x y]]
  (or (= x 2) (= x (- (g/width grid) 3))
      (= y 2) (= y (- (g/height grid) 3))))

(defn- parse-maze [input]
  (let [grid     (g/->sparse-grid input (conj letter? \.))
        named    (find-named grid)
        entrance (first (named "AA"))
        exit     (first (named "ZZ"))
        portals  (build-portals (dissoc named "AA" "ZZ"))]
    {:grid grid
     :entrance entrance
     :exit exit
     :portals portals}))

(defn- neighbors-for [grid portals]
  (fn [pos]
    (concat (filter grid (p/orthogonal-to pos))
            (when-let [portal (portals pos)] [portal]))))

(defn- level-neighbors-for [grid inner-portals outer-portals]
  (fn [[pos level]]
    (concat (map vector (filter grid (p/orthogonal-to pos)) (repeat level))
            (when (pos? level) (when-let [up (outer-portals pos)] [[up (dec level)]]))
            (when-let [down (inner-portals pos)] [[down (inc level)]]))))

(defn- level-heuristic-for [exit]
  (fn [[pos level]]
    (+ (m/manhattan-distance pos exit)
       (* level 100))))

(defn part1 [input]
  (let [{:keys [grid entrance exit portals]} (parse-maze input)]
    (path/a-star-cost entrance (neighbors-for grid portals) #{exit})))

(defn part2 [input]
  (let [{:keys [grid entrance exit portals]} (parse-maze input)
        {inner false, outer true} (group-by #(outer-portal? grid (key %)) portals)]
    (path/a-star-cost [entrance 0]
                      (level-neighbors-for grid (into {} inner) (into {} outer))
                      #{[exit 0]}
                      :heuristic (level-heuristic-for exit))))
