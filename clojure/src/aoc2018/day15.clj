;; https://adventofcode.com/2018/day/15
(ns aoc2018.day15
  (:require
   [aoc.day :as d]
   [aoc.util.grid :as g]
   [aoc.util.math :as m]
   [aoc.util.pathfinding :as pf]
   [aoc.util.pos :as p]))

(defn input [] (d/day-input 2018 15))

(defn read-order [[ax ay] [bx by]]
  (if (= ay by)
    (compare ax bx)
    (compare ay by)))

(defn min-hp-read-order [{hp1 :hp loc1 :loc} {hp2 :hp loc2 :loc}]
  (if (= hp1 hp2)
    (read-order loc1 loc2)
    (compare hp1 hp2)))

(defn shortest-path-order [p1 p2]
  (if (= (last p1) (last p2))
    (read-order (first p1) (first p2))
    (read-order (last p1) (last p2))))

(defn only-one-unit-type-left? [{:keys [units]}]
  (apply = (map :type (vals units))))

(defn any-elf-deaths? [field]
  (or (pos? (:elf-deaths field))
      (only-one-unit-type-left? field)))

(defn parse [input]
  (let [grid  (g/parse-grid input)
        units (g/locs-where grid #{\E \G})]
    {:grid           grid
     :rounds         0
     :elf-deaths     0
     :end-condition? only-one-unit-type-left?
     :battle-over?   false
     :units          (into (sorted-map-by read-order)
                           (for [u units]
                             [u {:type (grid u) :loc u :hp 200 :attack 3}]))}))

(defn update-elf-attack [field elf-attack]
  (update field :units merge (into {} (for [[loc unit] (:units field) :when (= (:type unit) \E)]
                                        [loc (assoc unit :attack elf-attack)]))))

(defn sort-units [units] (sort min-hp-read-order units))

(defn sort-paths [ps] (sort shortest-path-order ps))

(defn unit-at [{:keys [units]} loc] (units loc))

(def enemy-of {\E \G, \G \E})

(defn enemies [field unit]
  (remove #(= (:type unit) (:type %)) (vals (:units field))))

(defn open-adjacent [{:keys [grid]} loc]
  (filter #(= (grid %) \.) (p/orthogonal-to loc)))

(defn neighbors [field loc]
  (into {} (for [n (open-adjacent field loc)] [n 1])))

(defn paths-to [field start goals]
  (pf/dijkstra-paths start (partial neighbors field) (set goals)))

(defn attack-targets [{:keys [grid units]} unit]
  (sort-units
   (map units
        (filter #(= (grid %) (enemy-of (:type unit)))
                (p/orthogonal-to (:loc unit))))))

(defn move-unit [field unit loc]
  (-> field
      (update-in [:units] dissoc (:loc unit))
      (assoc-in [:units loc] (assoc unit :loc loc))
      (assoc-in [:grid (:loc unit)] \.)
      (assoc-in [:grid loc] (:type unit))))

(defn damage-unit [field unit damage]
  (let [hp' (- (:hp unit) damage)]
    (if (pos? hp')
      (assoc-in field [:units (:loc unit) :hp] hp')
      (-> field
          (update :elf-deaths (if (= (:type unit) \E) inc identity))
          (update-in [:units] dissoc (:loc unit))
          (assoc-in [:grid (:loc unit)] \.)))))

(defn move-loc [field unit]
  (let [open-targets    (mapcat #(open-adjacent field (:loc %)) (enemies field unit))
        min-path-groups (group-by count (paths-to field (:loc unit) open-targets))]
    (when (seq min-path-groups)
      (let [min-paths (second (apply min-key key min-path-groups))]
        (ffirst (sort-paths (map rest min-paths)))))))

(defn unit-turn [field unit]
  (let [targets (attack-targets field unit)]
    (if (seq targets)
      (damage-unit field (first targets) (:attack unit))
      (if-let [new-loc (move-loc field unit)]
        (let [field'  (move-unit field unit new-loc)
              targets (attack-targets field' (unit-at field' new-loc))]
          (if (seq targets)
            (damage-unit field' (first targets) (:attack unit))
            field'))
        field))))

(defn round [field unit-locs]
  (if (empty? unit-locs)
    (update field :rounds inc)
    (if ((:end-condition? field) field)
      (assoc field :battle-over? true)
      (if-let [unit (unit-at field (first unit-locs))]
        (recur (unit-turn field unit) (rest unit-locs))
        (recur field (rest unit-locs))))))

(defn battle [field]
  (loop [field field]
    (if (:battle-over? field)
      field
      (recur (round field (keys (:units field)))))))

(defn elf-victory [field elf-attack]
  (let [field (battle (-> field
                          (update-elf-attack elf-attack)
                          (assoc :end-condition? any-elf-deaths?)))]
    (when (zero? (:elf-deaths field))
      field)))

(defn outcome [field]
  (* (:rounds field) (m/sum (map :hp (vals (:units field))))))

(defn part1 [input]
  (outcome (battle (parse input))))

(defn part2 [input]
  (outcome (first (keep #(elf-victory (parse input) %) (iterate inc 4)))))
