;; https://adventofcode.com/2022/day/16
(ns aoc2022.day16
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c :refer [first-where]]
   [aoc.util.math :refer [max-int range-inc]]
   [aoc.util.string :as s]
   [clojure.data.priority-map :refer [priority-map-by]]))

(defn input [] (d/day-input 2022 16))

(def ^:private start-valve :AA)

(defn parse-valves [input]
  (into {}
        (for [line (s/lines input)]
          (let [[valve & tunnels] (map keyword (re-seq #"[A-Z]{2}" line))
                flow (s/int (re-find #"\d+" line))]
            [valve {:flow flow :tunnels tunnels}]))))

(defn- bfs-distances [valves start]
  (loop [q   (c/queue [start 0])
         seen #{start}
         dists {}]
    (if-let [[curr d] (peek q)]
      (let [dist'     (inc d)
            neighbors (remove seen (get-in valves [curr :tunnels]))
            dists'    (reduce (fn [acc n]
                                (if (pos? (get-in valves [n :flow]))
                                  (assoc acc n (inc dist'))
                                  acc))
                              dists neighbors)]
        (recur (into (pop q) (map vector neighbors (repeat dist')))
               (into seen neighbors)
               dists'))
      dists)))

(defn- relevant-valves [valves]
  (filter #(or (= start-valve (key %))
               (pos? (:flow (val %))))
          valves))

(defn- distance-graph [valves]
  (into {}
        (map (fn [v] [v (bfs-distances valves v)]))
        (keys (relevant-valves valves))))

(defn- best-valves-for-time [valves dists time]
  (let [min-dists (update-vals dists #(apply min max-int (vals %)))]
    (vec
     (for [t (range-inc time)]
       (sort-by (fn [[_ dist flow]] (* flow (- t dist))) >
                (for [[valve d] min-dists
                      :let [flow (:flow (valves valve))]
                      :when (and (> t d) (pos? flow))]
                  [valve d flow]))))))

(defn- estimate-potential [state best-valves]
  (loop [agents (map second (:agents state))
         opened (:opened state)
         bound  (:pressure state)]
    (let [time (first agents)
          best-options (best-valves time)]
      (if-let [[valve dist flow] (first-where #(not (opened (first %))) best-options)]
        (let [time' (- time dist)]
          (recur (sort > (conj (rest agents) time'))
                 (conj opened valve)
                 (+ bound (* flow time'))))
        bound))))

(defn- next-states [valves dists current]
  (let [{:keys [pressure opened agents]} current
        [pos time] (first agents)]
    (keep (fn [[next-valve dist]]
            (let [remaining (- time dist)]
              (when (and (pos? remaining) (not (opened next-valve)))
                {:pressure (+ pressure (* remaining (:flow (valves next-valve))))
                 :opened   (conj opened next-valve)
                 :agents   (sort-by second > (conj (rest agents) [next-valve remaining]))})))
          (dists pos))))

(defn- max-pressure [input time num-agents]
  (let [valves      (parse-valves input)
        dists       (distance-graph valves)
        best-valves (best-valves-for-time valves dists time)
        init-state  {:pressure 0
                     :opened   #{start-valve}
                     :agents   (repeat num-agents [start-valve time])}]
    (loop [q    (priority-map-by > init-state (estimate-potential init-state best-valves))
           seen #{}
           best 0]
      (if-let [[current upper] (peek q)]
        (cond
          (<= upper best) best
          (seen (dissoc current :pressure)) (recur (pop q) seen best)

          :else
          (let [children (next-states valves dists current)
                best'    (reduce max best (map :pressure children))
                q'       (into (pop q)
                               (keep (fn [state]
                                       (let [est (estimate-potential state best-valves)]
                                         (when (> est best')
                                           [state est]))))
                               children)]
            (recur q' (conj seen (dissoc current :pressure)) best')))
        best))))

(defn part1 [input] (max-pressure input 30 1))

(defn part2 [input] (max-pressure input 26 2))
