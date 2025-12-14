;; https://adventofcode.com/2015/day/8
(ns aoc2015.day08
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2015 8))

;; We don't need fully decoded strings. We just need
;; to know how long the decoded strings are. read-string
;; will handle any quotes, so just replace any unicode
;; or escaped \ characters with a -.
(defn decoded-size [s]
  (-> s
      (str/replace #"(\\x[a-f0-9]{1,2})|(\\\\)" "-")
      read-string
      count))

;; Same idea here, but the only thing that effects
;; the size is the " or \ character, so just replace
;; them with two characters. Then account for the
;; extra outer quotes at the end.
(defn encoded-size [s]
  (-> s
      (str/replace #"[\"\\]" "--")
      count
      (+ 2)))

(defn part1 [input]
  (m/sum (map #(- (count %) (decoded-size %))
              (s/lines input))))

(defn part2 [input]
  (m/sum (map #(- (encoded-size %) (count %))
              (s/lines input))))
