;; https://adventofcode.com/2019/day/18
 (ns aoc2019.day18
   (:require
    [aoc.day :as d]
    [aoc.util.char :as char]
    [aoc.util.collection :as c]
    [aoc.util.grid :as g]
    [aoc.util.math :as m]
    [aoc.util.pathfinding :as path]
    [aoc.util.pos :as p]
    [aoc.util.string :as s]
    [clojure.data.priority-map :refer [priority-map]]
    [clojure.set :as set]))

(defn input [] (d/day-input 2019 18))

(def ^:private key? (set s/alphabet-lower))
(def ^:private door? (set s/alphabet-upper))
(def ^:private entrance? #{\@ \0 \1 \2 \3})
(def ^:private point-of-interest? (set/union entrance? key?))

(defn- parse-grid [input] (g/str->grid input))

(defn- parse-robot-grid [input]
  (let [grid (parse-grid input)
        entrance (key (first (c/filter-vals entrance? grid)))]
    (into grid
          cat
          [;; replace the entrance with a wall
           [[entrance \#]]
           ;; replace the cross around entrance with walls
           (map (fn [w] [w \#]) (p/orthogonal-to entrance))
           ;; replace the corners around entrance with robot entrances.
           (map-indexed (fn [i e] [e (char/digit->char i)])
                        (p/diagonal-to entrance))])))

(defn- key-index [k] (- (int k) (int \a)))
(defn- key->bit [k] (bit-shift-left 1 (key-index k)))
(defn- add-key [keys k] (bit-or keys (key->bit k)))
(defn- has-key? [keys k] (bit-test keys (key-index k)))
(defn- unlocks? [keys doors] (= (bit-and keys doors) doors))

(defn- vault-for [grid]
  (let [pois (into {}
                   (comp (c/filter-vals point-of-interest?)
                         (map (juxt val key)))
                   grid)
        entrances (c/filter-keys entrance? pois)]
    {:grid (into grid (map (fn [[_ e]] [e \.]) entrances))
     :pois pois
     :entrances entrances
     :all-keys (reduce add-key 0 (filter key? (keys pois)))}))

(defn- reachable-keys [graph start keys]
  (loop [q (priority-map start 0), visited {}, found []]
    (if-let [[node dist] (peek q)]
      (let [q (pop q)]
        (if (contains? visited node)
          (recur q visited found)
          (let [visited (assoc visited node dist)
                new-key? (and (key? node) (not (has-key? keys node)))]
            (if new-key?
              (recur q visited (conj found {:to node :dist dist}))
              (let [neighbors (get graph node)
                    passable (filter #(unlocks? keys (:doors %)) neighbors)
                    next-q (reduce (fn [pq {to :to, to-dist :dist}]
                                     (let [new-dist (+ dist to-dist)]
                                       (if (and (< new-dist (get visited to m/pos-infinity))
                                                (< new-dist (get pq to m/pos-infinity)))
                                         (assoc pq to new-dist)
                                         pq)))
                                   q
                                   passable)]
                (recur next-q visited found))))))
      found)))

(defn- better-path?
  [dist1 doors1 dist2 doors2]
  (and (<= dist1 dist2)
       (= (bit-and doors1 doors2) doors1)))

(defn- bfs-from-poi [grid start-pos]
  (loop [q (c/queue {:pos start-pos :dist 0 :doors 0})
         visited {} ;; pos -> vector of {:dist :doors}
         results {}] ;; char -> vector of {:dist :doors}
    (if-let [{:keys [pos dist doors]} (peek q)]
      (let [q (pop q)
            existing (get visited pos [])]
        (if (some (fn [{d :dist m :doors}] (better-path? d m dist doors)) existing)
          (recur q visited results)
          (let [new-visited (assoc visited pos (conj existing {:dist dist :doors doors}))
                val (grid pos)
                poi? (and (not= pos start-pos) (point-of-interest? val))
                new-results (if poi?
                              (update results val (fnil conj []) {:dist dist :doors doors})
                              results)]
            (if poi?
              (recur q new-visited new-results)
              (let [new-doors (if (door? val) (add-key doors (char/lower-case val)) doors)
                    neighbors (remove #(= \# (grid %)) (p/orthogonal-to pos))]
                (recur (into q (map (fn [p] {:pos p :dist (inc dist) :doors new-doors}) neighbors))
                       new-visited
                       new-results))))))
      results)))

(defn- build-graph [grid pois]
  (reduce-kv
   (fn [graph char pos]
     (let [targets (bfs-from-poi grid pos)]
       (assoc graph char
              (mapcat (fn [[target edges]]
                        (map #(assoc % :to target) edges))
                      targets))))
   {}
   pois))

(defn- min-steps [grid]
  (let [{:keys [grid pois entrances all-keys]} (vault-for grid)
        graph (build-graph grid pois)
        neighbors (fn [[agents keys]]
                    (mapcat (fn [i]
                              (map (fn [{:keys [to dist]}]
                                     [[(assoc agents i to) (add-key keys to)] dist])
                                   (reachable-keys graph (nth agents i) keys)))
                            (range (count agents))))
        goal? (fn [[_ keys]] (= keys all-keys))]
    (path/a-star-weighted-cost [(vec (keys entrances)) 0] neighbors goal?)))

(defn part1 [input] (min-steps (parse-grid input)))

(defn part2 [input] (min-steps (parse-robot-grid input)))
