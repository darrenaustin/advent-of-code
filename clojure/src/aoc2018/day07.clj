;; https://adventofcode.com/2018/day/7
(ns aoc2018.day07
  (:require
    [aoc.day :as d]
    [clojure.set :as set]
    [clojure.string :as str]))

(defn input [] (d/day-input 2018 7))

(defn parse-rules [input]
  (let [rules (apply merge-with set/union
                     (for [[_ pre post] (re-seq #"Step (\S+) .* step (\S+) .*\." input)]
                       {post #{pre}}))]
    {:prereqs rules
     :steps   (sort (apply set/union
                           (conj (vals rules) (set (keys rules)))))}))

(defn part1 [input]
  (let [{:keys [prereqs steps]} (parse-rules input)]
    (loop [order [] prereqs prereqs steps steps]
      (if (seq steps)
        (let [step (first (filter #(empty? (prereqs %)) steps))]
          (recur (conj order step)
                 (into {} (map (fn [[k v]] [k (disj v step)]) prereqs))
                 (remove #{step} steps)))
        (str/join order)))))

(defn char->int [ch]
  (inc (- (int ch) (int \A))))

(defn work [workers]
  (let [{finished true workers' false}
        (group-by (fn [[_ v]] (zero? v))
                  (for [[k v] workers] [k (dec v)]))]
    [(mapv first finished)
     (into {} workers')]))

;; TODO: streamline this.
(defn part2
  ([input] (part2 input 5 60))
  ([input num-workers step-time]
   (let [{:keys [prereqs steps]} (parse-rules input)]
     (loop [time -1 prereqs prereqs steps steps workers {}]
       (if (or (seq steps) (seq workers))
         (let [[finished workers'] (work workers)
               prereqs'  (into {} (map (fn [[k v]] [k (apply disj v finished)]) prereqs))
               available (take (- num-workers (count workers'))
                               (filter #(empty? (prereqs' %)) steps))]
           (recur (inc time) (apply dissoc prereqs' available) (remove (set (concat finished available)) steps)
                  (apply merge workers' (for [s available] {s (+ step-time (char->int (first s)))}))))
         time)))))
