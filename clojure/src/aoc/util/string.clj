(ns aoc.util.string)

(defn read-int [str] (Integer/parseInt str 10))

(defn parse-ints [str]
  (map read-int (re-seq #"[-+]?\d+" str)))

(defn parse-pos-ints [str]
  (map read-int (re-seq #"\d+" str)))

(defn digit [chr]
  (^[char] Character/getNumericValue chr))
