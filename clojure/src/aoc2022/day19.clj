;; https://adventofcode.com/2022/day/19
(ns aoc2022.day19
  (:require
   [aoc.day :as d]
   [aoc.util.math :refer [ceil-div max-int product sum]]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2022 19))

(defn- parse-blueprints [input]
  (map #(let [[id ore clay obsidian obsidian-clay geode geode-obsidian] (s/ints %)]
          {:id id
           :robot-costs [[:geode {:ore geode, :obsidian geode-obsidian}]
                         [:obsidian {:ore obsidian, :clay obsidian-clay}]
                         [:clay {:ore clay}]
                         [:ore {:ore ore}]]
           :max-robots {:ore (max ore clay obsidian geode)
                        :clay obsidian-clay
                        :obsidian geode-obsidian
                        :geode max-int}})
       (s/lines input)))

(defn- max-geodes [time blueprint]
  (let [{:keys [robot-costs max-robots]} blueprint
        best-geodes (volatile! 0)]
    (letfn [(dfs [time robots resources]
              (let [potential (+ (:geode resources)
                                 (* (:geode robots) time)
                                 (quot (* (dec time) time) 2))]
                (when (> potential @best-geodes)
                  (vswap! best-geodes max (+ (:geode resources) (* (:geode robots) time)))
                  (doseq [[material cost] robot-costs]
                    (let [need-more? (< (material robots) (material max-robots))
                          can-build? (every? (fn [[res _]] (pos? (res robots))) cost)]
                      (when (and need-more? can-build?)
                        (let [wait (reduce
                                    (fn [w [res amt]]
                                      (max w (let [needed (- amt (res resources))]
                                               (if (<= needed 0) 0 (ceil-div needed (res robots))))))
                                    0
                                    cost)
                              dt (inc wait)]
                          (when (< dt time)
                            (let [prod (fn [m r amount]
                                         (update m r + (* amount dt) (- (get cost r 0))))
                                  new-resources (reduce-kv prod resources robots)]
                              (dfs (- time dt)
                                   (update robots material inc)
                                   new-resources))))))))))]
      (dfs time
           {:ore 1 :clay 0 :obsidian 0 :geode 0}
           {:ore 0 :clay 0 :obsidian 0 :geode 0})
      @best-geodes)))

(defn- quality [time blueprint]
  (* (:id blueprint) (max-geodes time blueprint)))

(defn part1 [input]
  (->> (parse-blueprints input)
       (pmap (partial quality 24))
       sum))

(defn part2 [input]
  (->> (parse-blueprints input)
       (take 3)
       (pmap (partial max-geodes 32))
       product))
