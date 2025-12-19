;; https://adventofcode.com/2015/day/16
(ns aoc2015.day16
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2015 16))

(defn parse-aunts [input]
  (map #(let [[[_ _ id] & props] (re-seq #"(\w+):? (\d+)" %)]
          {:aunt (s/int id)
           :compounds (into {}
                            (map (fn [[_ k v]] [(keyword k) (s/int v)]))
                            props)})
       (s/lines input)))

(def scanned
  {:children 3
   :cats 7
   :samoyeds 2
   :pomeranians 3
   :akitas 0
   :vizslas 0
   :goldfish 5
   :trees 3
   :cars 2
   :perfumes 1})

(defn matching-aunt? [compounds matchers]
  (every? (fn [[k v]] ((get matchers k =) v (scanned k)))
          compounds))

(defn find-aunt [input matching-fns]
  (->> (parse-aunts input)
       (filter #(matching-aunt? (:compounds %) matching-fns))
       first
       :aunt))

(defn part1 [input] (find-aunt input (constantly =)))

(defn part2 [input] (find-aunt input {:cats >
                                      :trees >
                                      :pomeranians <
                                      :goldfish <}))
