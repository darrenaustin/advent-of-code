;; https://adventofcode.com/2018/day/4
(ns aoc2018.day04
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2018 4))

(defn- parse-entry [line]
  (let [[_ time-str status] (re-find #":(\d+)] (.*)" line)
        minute (s/int time-str)]
    (if (str/starts-with? status "Guard")
      [:start-shift (s/int status)]
      (case status
        "wakes up"     [:wakes-up minute]
        "falls asleep" [:falls-asleep minute]))))

(defn- parse-log [input]
  (map parse-entry (sort (s/lines input))))

(defn- sleep-entries [start-min end-min]
  (map vector (range start-min end-min) (repeat 1)))

(defn- minutes-asleep [entries]
  (first
   (reduce
    (fn [[minutes sleep-start] [action time]]
      (case action
        :falls-asleep [minutes time]
        :wakes-up     [(into minutes (sleep-entries sleep-start time)) nil]))
    [{} nil]
    entries)))

(defn- process-night [entries]
  (let [guard (second (first entries))
        sleep-minutes (minutes-asleep (rest entries))]
    {guard sleep-minutes}))

(defn- total-sleep-time [minutes]
  (reduce + (vals minutes)))

(defn- process-log [log]
  (->> (c/partition-starting #(= :start-shift (first %)) log)
       (map process-night)
       (apply merge-with (partial merge-with +))
       (remove (comp empty? val))
       (into {})))

(defn- best-minute [minutes]
  (key (apply max-key val minutes)))

(defn part1 [input]
  (let [log (process-log (parse-log input))
        [guard minutes] (apply max-key (comp total-sleep-time val) log)]
    (* guard (best-minute minutes))))

(defn part2 [input]
  (let [log (process-log (parse-log input))
        [guard minutes] (apply max-key (comp #(apply max (vals %)) val) log)]
    (* guard (best-minute minutes))))
