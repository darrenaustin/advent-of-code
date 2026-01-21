;; https://adventofcode.com/2020/day/19
(ns aoc2020.day19
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]
   [clojure.string :as str]
   [instaparse.core :as insta]))

(defn input [] (d/day-input 2020 19))

;; Let's cheat and use a parsing library
(defn- valid-messages [input]
  (let [[rules messages] (s/blocks input)
        parser (insta/parser rules :start :0)]
    (count (remove #(insta/failure? (parser %)) (s/lines messages)))))

(defn part1 [input] (valid-messages input))

(defn part2 [input]
  (valid-messages
   (-> input
       (str/replace #"(?m)^8:.*$"  "8: 42 | 42 8")
       (str/replace #"(?m)^11:.*$" "11: 42 31 | 42 11 31"))))
