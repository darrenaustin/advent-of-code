;; https://adventofcode.com/2015/day/22
 (ns aoc2015.day22
   (:require
    [aoc.day :as d]
    [aoc.util.string :as s]))

(defn input [] (d/day-input 2015 22))

(defn- parse-boss [input] (zipmap [:hp :damage] (s/ints input)))

(defn- initial-battle [input]
  {:player {:hp 50, :mana 500, :armor 0, :mana-spent 0}
   :boss (parse-boss input)
   :turn :player
   :effects {}})

(def ^:private spells
  [{:name :magic-missile :cost 53 :damage 4}
   {:name :drain :cost 73 :damage 2 :heal 2}
   {:name :shield :cost 113 :effect {:turns 6 :armor 7}}
   {:name :poison :cost 173 :effect {:turns 6 :damage 3}}
   {:name :recharge :cost 229 :effect {:turns 5 :mana 101}}])

(defn- castable-spells [mana active]
  (filter #(and (<= (:cost %) mana)
                (not (contains? active (:name %))))
          spells))

(defn- apply-mode [battle]
  (if (and (:hard-mode battle) (= (:turn battle) :player))
    (update-in battle [:player :hp] dec)
    battle))

(defn- age-effects [effects]
  (->> effects
       (map (fn [[n e]] [n (update e :turns dec)]))
       (remove (fn [[_ e]] (<= (:turns e) 0)))
       (into {})))

(defn- apply-effects [battle]
  (let [effects (apply merge-with + (vals (:effects battle)))]
    (-> battle
        (assoc-in [:player :armor] (get effects :armor 0))
        (update-in [:boss :hp] - (get effects :damage 0))
        (update-in [:player :mana] + (get effects :mana 0))
        (update :effects age-effects))))

(defn cast-spell [battle spell]
  (-> battle
      (update-in [:player :mana] - (:cost spell))
      (update-in [:player :mana-spent] + (:cost spell))
      (update-in [:boss :hp] - (get spell :damage 0))
      (update-in [:player :hp] + (get spell :heal 0))
      (update :effects #(if (contains? spell :effect)
                          (assoc % (:name spell) (:effect spell))
                          %))))

(defn- boss-attack [battle]
  (let [damage (max 1 (- (get-in battle [:boss :damage])
                         (get-in battle [:player :armor])))]
    (update-in battle [:player :hp] - damage)))

(defn- next-turns [battle]
  (let [battle' (-> battle
                    apply-mode
                    apply-effects
                    (assoc :turn (if (= (:turn battle) :boss) :player :boss)))]
    (if (= (:turn battle) :boss)
      [(boss-attack battle')]
      (for [spell (castable-spells (get-in battle' [:player :mana]) (:effects battle'))]
        (cast-spell battle' spell)))))

(defn- cheapest-win [battle]
  (let [cheapest
        (fn [{:keys [battles best]}]
          (if (seq battles)
            (recur
             (reduce (fn [result battle]
                       (reduce (fn [result battle]
                                 (cond
                                   (<= (get-in battle [:player :hp]) 0)
                                   result

                                   (<= (:best result) (get-in battle [:player :mana-spent]))
                                   result

                                   (<= (get-in battle [:boss :hp]) 0)
                                   (assoc result :best (get-in battle [:player :mana-spent]))

                                   :else
                                   (update result :battles conj battle)))
                               result
                               (next-turns battle)))
                     {:best best, :battles []}
                     battles))
            best))]
    (cheapest {:battles [battle] :best Integer/MAX_VALUE})))

(defn part1 [input]
  (cheapest-win (initial-battle input)))

(defn part2 [input]
  (cheapest-win (assoc (initial-battle input) :hard-mode true)))
