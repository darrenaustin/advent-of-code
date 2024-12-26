(ns aoc.util.string
  (:require [clojure.string :as str]))

(defn- read-int [s]
  ;; read-string treats a leading '0' as an octal number. Sigh.
  (read-string (cond
                 (= s "0") s
                 (str/starts-with? s "0") (subs s 1)
                 (str/starts-with? s "-0") (str "-" (subs s 2))
                 :else s)))

(defn parse-ints [str]
  (mapv read-int (re-seq #"-?\d+" str)))

(defn parse-pos-ints [str]
  (mapv read-int (re-seq #"\d+" str)))

(defn digit [chr]
  (^[char] Character/getNumericValue chr))

(defn string<
  ([_] true)
  ([x y] (neg? (compare x y)))
  ([x y & more]
   (if (string< x y)
     (if (next more)
       (recur y (first more) (next more))
       (string< y (first more)))
     false)))

(defn string>
  ([_] true)
  ([x y] (pos? (compare x y)))
  ([x y & more]
   (if (string> x y)
     (if (next more)
       (recur y (first more) (next more))
       (string> y (first more)))
     false)))
