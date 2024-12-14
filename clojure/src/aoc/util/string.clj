(ns aoc.util.string
  (:require
   [clojure.edn :as edn]))

(defn parse-nums [str]
  (map edn/read-string (re-seq #"\d+" str)))

(defn parse-full-nums [str]
  (map edn/read-string (re-seq #"[-+]?\d+" str)))


(defn digit [chr]
  (^[char] Character/getNumericValue chr))
