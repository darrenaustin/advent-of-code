;; https://adventofcode.com/2018/day/24
(ns aoc2018.day24
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c :refer [by desc]]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2018 24))

(defn parse-attr [attr]
  (let [[_ kind values] (re-find #"(\w+) to (.*)" attr)]
    {(keyword kind) (set (str/split values #", "))}))

(defn parse-attrs [attrs]
  (when attrs
    (reduce (fn [m a] (merge m (parse-attr a))) {} (str/split attrs #"; "))))

(defn parse-group [army idx line]
  (let [[units hp attack initiative] (s/ints line)
        [_ attack-type] (re-find #"does \d+ (\w+) damage.*" line)
        [_ attrs] (re-find #".*points \((.*)\)" line)]
    (merge {:id          (str army "-" (inc idx))
            :army        army
            :units       units
            :hp          hp
            :attack      attack
            :attack-type attack-type
            :initiative  initiative}
           (parse-attrs attrs))))

(defn parse-army [input]
  (let [[title & groups] (s/lines input)
        name (if (str/starts-with? title "Immune") :immune :infection)]
    (into {} (for [g (map-indexed (partial parse-group name) groups)] [(:id g) g]))))

(defn parse [input]
  (reduce merge (mapv parse-army (str/split input #"\n\n"))))

(defn effective-power [group]
  (* (:units group) (:attack group)))

(defn attack-damage [attacker target]
  (condp contains? (:attack-type attacker)
    (:immune target) 0
    (:weak target) (* 2 (effective-power attacker))
    (effective-power attacker)))

(defn enemies? [a b]
  (not= (:army a) (:army b)))

(defn select-target [group targets]
  (letfn [(damage [target] (attack-damage group target))
          (can-damage? [target] (pos? (damage target)))
          (enemy? [target] (enemies? group target))]
    (first (sort (by damage desc
                     effective-power desc
                     :initiative desc)
                 (filter (every-pred enemy? can-damage?) targets)))))

(defn select-targets [groups]
  (let [target-groups (sort (by effective-power desc :initiative desc) groups)]
    (loop [[group & groups] target-groups
           available (set target-groups)
           attacks   []]
      (if (nil? group)
        attacks
        (if-let [target (select-target group available)]
          (recur groups
                 (disj available target)
                 (conj attacks [(:id group) (:id target)]))
          (recur groups available attacks))))))

(defn perform-attack [groups [attacker-id target-id]]
  (if-let [attacker (groups attacker-id)]
    (let [damage     (attack-damage attacker (groups target-id))
          {:keys [units hp]} (groups target-id)
          units-left (- units (quot damage hp))]
      (if (pos? units-left)
        (assoc-in groups [target-id :units] units-left)
        (dissoc groups target-id)))
    groups))

(defn fight [groups]
  (let [attacks (sort (by (comp :initiative groups first) desc)
                      (select-targets (vals groups)))]
    (reduce perform-attack groups attacks)))

(defn battle-over? [groups]
  (or (nil? groups)
      (= 1 (count (group-by :army (vals groups))))))

(defn battle [groups]
  (loop [[fight & fights] (iterate fight groups) last-fight nil]
    ; If there was no damage done, it is a stalemate so return nil
    (when (not= fight last-fight)
      (if (battle-over? fight)
        fight
        (recur fights fight)))))

(defn boost-immune [groups amount]
  (into {} (for [[id g] groups]
             (if (= (:army g) :immune)
               [id (update g :attack + amount)]
               [id g]))))

(defn immune-win? [groups]
  (and groups
       (every? #(= :immune (:army %)) (vals groups))))

(defn units-left [groups]
  (m/sum (map :units (vals groups))))

(defn lowest-boost-win [groups]
  (let [[upper _] (c/first-where #(immune-win? (second %))
                                 (for [b (iterate #(* 2 %) 1)]
                                   [b (battle (boost-immune groups b))]))]
    (loop [low (quot upper 2), high upper]
      (if (< low high)
        (let [mid (quot (+ low high) 2)]
          (if (immune-win? (battle (boost-immune groups mid)))
            (recur low mid)
            (recur (inc mid) high)))
        (battle (boost-immune groups low))))))

(defn part1 [input]
  (units-left (battle (parse input))))

(defn part2 [input]
  (let [groups (parse input)]
    (units-left (lowest-boost-win groups))))
