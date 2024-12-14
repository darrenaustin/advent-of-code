(ns aoc.util.string
  (:require
   [clojure.edn :as edn]))

(defn parse-ints [str]
  (map edn/read-string (re-seq #"[-+]?\d+" str)))

(defn parse-pos-ints [str]
  (map edn/read-string (re-seq #"\d+" str)))

(defn digit [chr]
  (^[char] Character/getNumericValue chr))
