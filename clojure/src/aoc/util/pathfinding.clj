(ns aoc.util.pathfinding
  "A collection of pathfinding algorithms including A* and Dijkstra.
   Provides functions for finding shortest path costs, single paths, and all shortest paths."
  (:require
   [clojure.data.priority-map :refer [priority-map priority-map-keyfn]]))

(defn a-star-cost
  "Finds the shortest path cost from start to a goal state using A* search.
   `neighbors`: function that takes a state and returns a sequence of neighbor states.
   `goal?`: function that takes a state and returns true if it is a goal state.
   `:cost`: optional function that takes current-state and neighbor-state and returns the cost of the transition. Defaults to (constantly 1).
   `:heuristic`: optional function that takes a state and returns an estimated cost to the goal. Defaults to (constantly 0)."
  [start neighbors goal? & {:keys [cost heuristic]
                            :or {cost (constantly 1) heuristic (constantly 0)}}]
  (loop [q (priority-map start (heuristic start))
         costs {start 0}]
    (when-let [[curr _] (peek q)]
      (if (goal? curr)
        (costs curr)
        (let [curr-cost (costs curr)
              q (pop q)
              neighbors (neighbors curr)
              {next-q :q next-costs :c}
              (reduce (fn [{:keys [q c] :as acc} next-state]
                        (let [step-cost (cost curr next-state)
                              new-cost (+ curr-cost step-cost)]
                          (if (< new-cost (get c next-state Double/POSITIVE_INFINITY))
                            {:q (assoc q next-state (+ new-cost (heuristic next-state)))
                             :c (assoc c next-state new-cost)}
                            acc)))
                      {:q q :c costs}
                      neighbors)]
          (recur next-q next-costs))))))

(defn- reconstruct-path [came-from current]
  (loop [curr current
         path (list current)]
    (if-let [parent (get came-from curr)]
      (recur parent (conj path parent))
      path)))

(defn a-star-path
  "Finds a shortest path from start to a goal state using A* search.
   Returns a sequence of states from start to goal, or nil if no path is found.
   `neighbors`: function that takes a state and returns a sequence of neighbor states.
   `goal?`: function that takes a state and returns true if it is a goal state.
   `:cost`: optional function that takes current-state and neighbor-state and returns the cost of the transition. Defaults to (constantly 1).
   `:heuristic`: optional function that takes a state and returns an estimated cost to the goal. Defaults to (constantly 0)."
  [start neighbors goal? & {:keys [cost heuristic]
                            :or {cost (constantly 1) heuristic (constantly 0)}}]
  (loop [q (priority-map start (heuristic start))
         costs {start 0}
         came-from {}]
    (when-let [[curr _] (peek q)]
      (if (goal? curr)
        (reconstruct-path came-from curr)
        (let [curr-cost (costs curr)
              q (pop q)
              neighbors (neighbors curr)
              {next-q :q next-costs :c next-came-from :cf}
              (reduce (fn [{:keys [q c cf] :as acc} next-state]
                        (let [step-cost (cost curr next-state)
                              new-cost (+ curr-cost step-cost)]
                          (if (< new-cost (get c next-state Double/POSITIVE_INFINITY))
                            {:q (assoc q next-state (+ new-cost (heuristic next-state)))
                             :c (assoc c next-state new-cost)
                             :cf (assoc cf next-state curr)}
                            acc)))
                      {:q q :c costs :cf came-from}
                      neighbors)]
          (recur next-q next-costs next-came-from))))))

(defn- min-paths [a b]
  (let [[a-d a-paths] a [b-d b-paths] b]
    (case (compare a-d b-d)
      -1 [a-d a-paths]
      0 [a-d (concat a-paths b-paths)]
      1 [b-d b-paths])))

(defn dijkstra-traverse
  "Traverses the graph from start using Dijkstra's algorithm.
   Returns a map of {state [cost [parent-states]]} for all reachable states.
   `neighbors`: function that takes a state and returns a sequence of neighbor states.
   `:cost`: optional function that takes current-state and neighbor-state and returns the cost of the transition. Defaults to (constantly 1)."
  [start neighbors & {:keys [cost] :or {cost (constantly 1)}}]
  (loop [q (priority-map-keyfn first start [0 []])
         visited {}]
    (if-let [[curr [d _]] (peek q)]
      (let [visited (assoc visited curr (get q curr))
            q (pop q)
            nbrs (neighbors curr)
            dist (->> nbrs
                      (remove #(contains? visited %))
                      (map (fn [n] [n [(+ d (cost curr n)) [curr]]]))
                      (into {}))]
        (recur (merge-with min-paths q dist) visited))
      visited)))

(defn- paths-from [paths-map loc start]
  (if (= loc start)
    [[start]]
    (let [min-locs (second (paths-map loc))]
      (map #(conj % loc) (mapcat #(paths-from paths-map % start) min-locs)))))

(defn dijkstra-all-paths
  "Finds all shortest paths from start to a goal state using Dijkstra's algorithm.
   Returns a sequence of paths (each path is a sequence of states).
   `neighbors`: function that takes a state and returns a sequence of neighbor states.
   `:cost`: optional function that takes current-state and neighbor-state and returns the cost of the transition. Defaults to (constantly 1)."
  [start neighbors goal? & {:keys [cost] :or {cost (constantly 1)}}]
  (loop [q (priority-map-keyfn first start [0 []])
         visited {}
         goals []
         min-goal-cost Double/POSITIVE_INFINITY]
    (if-let [[curr [d _]] (peek q)]
      (if (> d min-goal-cost)
        (mapcat #(paths-from visited % start) goals)
        (let [visited (assoc visited curr (get q curr))
              q (pop q)]
          (if (goal? curr)
            (recur q visited (conj goals curr) d)
            (let [nbrs (neighbors curr)
                  dist (->> nbrs
                            (remove #(contains? visited %))
                            (map (fn [n] [n [(+ d (cost curr n)) [curr]]]))
                            (into {}))]
              (recur (merge-with min-paths q dist) visited goals min-goal-cost)))))
      (mapcat #(paths-from visited % start) goals))))
