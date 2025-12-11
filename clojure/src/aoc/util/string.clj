(ns aoc.util.string
  (:require
   [clojure.pprint :refer [cl-format]]
   [clojure.string :as str]))

(defn split-blocks
  "Splits a string into blocks separated by double newlines (blank lines)."
  [s]
  (str/split s #"\n\s*\n"))

(defn- read-int [s]
  ;; read-string treats a leading '0' as an octal number. Sigh.
  (read-string (cond
                 (= s "0") s
                 (str/starts-with? s "0") (subs s 1)
                 (str/starts-with? s "-0") (str "-" (subs s 2))
                 :else s)))

(defn parse-ints
  "Parses all integers from a string, including negative numbers.
   Returns a vector of integers."
  [str]
  (mapv read-int (re-seq #"-?\d+" str)))

(defn parse-int
  "Parses the first integer found in a string."
  [str]
  (first (parse-ints str)))

(defn parse-pos-ints
  "Parses all positive integers from a string.
   Returns a vector of integers."
  [str]
  (mapv read-int (re-seq #"\d+" str)))

(defn digit
  "Converts a character digit to its integer value."
  [chr]
  (^[char] Character/getNumericValue chr))

(defn digits
  "Returns a vector of digits from a number or string."
  [n]
  (mapv digit (str n)))

(defn ->hex
  "Converts a number to its hexadecimal string representation.
   Supports optional minimum padding width and padding character (default '0')."
  ([n] (cl-format nil "~X" n))
  ([n min-padding] (cl-format nil (str "~" min-padding ",'0X") n))
  ([n min-padding padding-char]
   (cl-format nil (str "~" min-padding ",'" padding-char "X") n)))

(defn ->bin
  "Converts a number to its binary string representation.
   Supports optional minimum padding width and padding character (default '0')."
  ([n] (cl-format nil "~B" n))
  ([n min-padding] (cl-format nil (str "~" min-padding ",'0B") n))
  ([n min-padding padding-char]
   (cl-format nil (str "~" min-padding ",'" padding-char "B") n)))

(defn string<
  "Returns true if strings are in monotonically increasing order, otherwise false."
  ([_] true)
  ([x y] (neg? (compare x y)))
  ([x y & more]
   (if (string< x y)
     (if (next more)
       (recur y (first more) (next more))
       (string< y (first more)))
     false)))

(defn string>
  "Returns true if strings are in monotonically decreasing order, otherwise false."
  ([_] true)
  ([x y] (pos? (compare x y)))
  ([x y & more]
   (if (string> x y)
     (if (next more)
       (recur y (first more) (next more))
       (string> y (first more)))
     false)))
