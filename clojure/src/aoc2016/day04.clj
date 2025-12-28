;; https://adventofcode.com/2016/day/4
(ns aoc2016.day04
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2016 4))

(def room-regex #"^([a-z]+(?:-[a-z]+)*)-(\d+)\[([a-z]+)\]$")

(defn parse-rooms [input]
  (mapv #(let [[_ code id checksum] (re-find room-regex %)]
           {:code code
            :id (s/int id)
            :checksum checksum})
        (s/lines input)))

(defn compute-checksum [s]
  (->> (remove #{\-} s)
       frequencies
       ;; Descending by frequency, ascending by character
       (sort #(compare [(val %2) (key %1)]
                       [(val %1) (key %2)]))
       (map first)
       (take 5)
       str/join))

(defn cypher-shift [n chr]
  (if (= \- chr)
    \space
    (char (+ (int \a)
             (mod (+ (- (int chr) (int \a)) n)
                  26)))))

(defn decrypt-name [{:keys [code id] :as room}]
  (let [name (str/join (map #(cypher-shift id %) code))]
    (assoc room :name name)))

(defn real-room? [{:keys [code checksum]}]
  (= checksum (compute-checksum code)))

(defn part1 [input]
  (->> (parse-rooms input)
       (filter real-room?)
       (map :id)
       m/sum))

(defn part2 [input]
  (->> (parse-rooms input)
       (filter real-room?)
       (map decrypt-name)
       ;; Target name found by manually inspecting the results
       (c/first-where #(= (:name %) "northpole object storage"))
       :id))
