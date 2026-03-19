;; https://adventofcode.com/2023/day/20
(ns aoc2023.day20
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2023 20))

(defn- inputs-for [network node]
  (c/keys-when-val #((set (:outputs %)) node) network))

(defn- init-conjunctions [network]
  (reduce-kv
   (fn [m k v]
     (assoc m k
            (if (= (:type v) :conjunction)
              (assoc v :inputs (zipmap (inputs-for network k) (repeat false)))
              v)))
   {}
   network))

(defn- parse-network [input]
  (init-conjunctions
   (into {}
         (map (fn [line]
                (let [[_ prefix name outs] (re-find #"(%|&)?(\w+) -> (.*)" line)]
                  [name {:type (case prefix nil :broadcast, "%" :flip-flop, "&" :conjunction)
                         :value false
                         :outputs (str/split outs #", ")}])))
         (s/lines input))))

(defn- deliver-pulse [{:keys [type value outputs] :as module} [to pulse from]]
  (case type
    :broadcast   [module (map (fn [o] [o pulse to]) outputs)]
    :flip-flop   (if pulse
                   [module]
                   [(assoc module :value (not value)) (map (fn [o] [o (not value) to]) outputs)])
    :conjunction (let [module' (assoc-in module [:inputs from] pulse)
                       pulse' (not (every? identity (vals (:inputs module'))))]
                   [module' (map (fn [o] [o pulse' to]) outputs)])
    [module]))

(defn- press-button [{:keys [network]}]
  (loop [network network
         pulses (c/queue ["broadcaster" false :button])
         log []]
    (if-let [[to _ _ :as pulse] (peek pulses)]
      (let [[module new-pulses] (deliver-pulse (network to) pulse)]
        (recur (assoc network to module)
               (into (pop pulses) new-pulses)
               (conj log pulse)))
      {:network network :log log})))

(defn part1 [input]
  (let [initial-state {:network (parse-network input) :log []}
        states (take 1000 (drop 1 (iterate press-button initial-state)))
        pulses (mapcat :log states)
        {highs true, lows false} (frequencies (map second pulses))]
    (* highs lows)))

(defn part2 [input]
  (let [network (parse-network input)]
    ;; The number of presses for rx to trigger is prohibitive to
    ;; simulate. However, if rx is only connected to a conjunction
    ;; node as input, we just need to know when it will send a high
    ;; pulse. If we track presses to the first pulse to each of the
    ;; conjunction's inputs, the lcm of the input periods will give
    ;; us when all the inputs will line up to trigger rx.
    (assert (let [rx-inputs (inputs-for network "rx")]
              (and (= (count rx-inputs) 1)
                   (= (:type (network (first rx-inputs))) :conjunction))))

    (let [inputs (set (keys (:inputs (network (first (inputs-for network "rx"))))))]
      (loop [state {:network network :log []}, presses 1, periods {}]
        (if (= (count inputs) (count periods))
          (apply m/lcm (vals periods))
          (let [next-state (press-button state)
                new-periods (reduce (fn [ps [_ pulse from]]
                                      (if (and (inputs from) pulse (not (ps from)))
                                        (assoc ps from presses)
                                        ps))
                                    periods
                                    (:log next-state))]
            (recur next-state (inc presses) new-periods)))))))
