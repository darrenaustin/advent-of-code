;; https://adventofcode.com/2016/day/5
  (ns aoc2016.day05
    (:require
     [aoc.day :as d]
     [aoc.util.string :as s]
     [clojure.string :as str]
     [pandect.algo.md5 :refer [md5]]))

(defn input [] (d/day-input 2016 5))

(defn hashes [seed]
  (->> (range)
       (map (comp md5 (partial str seed)))
       (filter #(str/starts-with? % "00000"))))

(defn position-pair [hash]
  (let [[i c] (subs hash 5 7)]
    [(s/int (str i)) c]))

(defn part1 [input]
  (->> (hashes input)
       (take 8)
       (map #(nth % 5))
       str/join))

(defn part2 [input]
  (let [pairs (map position-pair (hashes input))]
    (loop [pwd (sorted-map), [[pos chr] & ps] pairs]
      (if (= (count pwd) 8)
        (str/join (vals pwd))
        (if (and pos (not (pwd pos)) (<= 0 pos 7))
          (recur (assoc pwd pos chr) ps)
          (recur pwd ps))))))
