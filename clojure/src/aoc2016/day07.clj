;; https://adventofcode.com/2016/day/7
(ns aoc2016.day07
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2016 7))

(defn- parse-ip [s]
  (let [{supernet false, hypernet true}
        (group-by #(str/starts-with? % "[") (re-seq #"\w+|\[\w+\]" s))]
    {:supernet supernet
     :hypernet (map #(subs % 1 (dec (count %))) hypernet)}))

(defn- abba [s] (re-seq #"(\w)(?!\1)(\w)(\2)(\1)" s))

(defn- aba [s]
  (map (fn [[_ _ a b]] [a b])
       (re-seq #"(?=((\w)(?!\2)(\w)(\2)))" s)))

(defn- tls? [{:keys [supernet hypernet]}]
  (and (some abba supernet)
       (not-any? abba hypernet)))

(defn- ssl? [{:keys [supernet hypernet]}]
  (let [super-abas (set (mapcat aba supernet))
        hyper-abas (set (mapcat aba hypernet))]
    (some (fn [[a b]] (hyper-abas [b a])) super-abas)))

(defn- num-ips [input pred]
  (->> (s/lines input)
       (map parse-ip)
       (filter pred)
       count))

(defn part1 [input] (num-ips input tls?))

(defn part2 [input] (num-ips input ssl?))
