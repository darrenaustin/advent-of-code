;; https://adventofcode.com/2019/day/23
 (ns aoc2019.day23
   (:require
    [aoc.day :as d]
    [aoc2019.intcode :as ic]))

(defn input [] (d/day-input 2019 23))

(def ^:private nat-addr 255)
(def ^:private no-packets -1)

(defn- build-network [input]
  (let [program (ic/parse-program input)]
    (mapv #(assoc (ic/run program [% no-packets] nil) :addr %)
          (range 50))))

(defn- collect-packets [nodes]
  (->> nodes
       (mapcat :output)
       (partition 3)
       (reduce (fn [m [addr x y]]
                 (update m addr (fnil conj []) x y))
               {})))

(defn- process-packets [nodes packets]
  (mapv (fn [n]
          (assert (empty? (:input n)))
          (-> n
              (ic/update-io (get packets (:addr n) [no-packets]) nil)
              ic/execute))
        nodes))

(defn- last-nat-packet [packets]
  (take-last 2 (get packets nat-addr)))

(defn part1 [input]
  (loop [nodes (build-network input)]
    (let [packets (collect-packets nodes)]
      (if-let [[_ exit-y] (get packets nat-addr)]
        exit-y
        (recur (process-packets nodes packets))))))

(defn part2 [input]
  (loop [nodes (build-network input), nat-packet nil, nat-ys #{}]
    (let [packets (collect-packets nodes)
          nat-packet' (or (last-nat-packet packets) nat-packet)]
      (if (empty? packets)
        (let [[_ nat-y] nat-packet']
          (if (nat-ys nat-y)
            nat-y
            (recur (process-packets nodes {0 nat-packet'})
                   nil
                   (conj nat-ys nat-y))))
        (recur (process-packets nodes packets) nat-packet' nat-ys)))))
