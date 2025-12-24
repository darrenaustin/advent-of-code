(ns aoc.util.string
  (:refer-clojure :exclude [int ints])
  (:require
   [clojure.pprint :refer [cl-format]]
   [clojure.string :as str])
  (:import
   [java.util.regex Matcher]))

(def alphabet "abcdefghijklmnopqrstuvwxyz")

(defn blocks
  "Splits a string into blocks separated by double newlines (blank lines)."
  [s]
  (str/split s #"\n\s*\n"))

(defn lines
  "Splits a string into a sequence of lines."
  [s]
  (str/split-lines s))

(defn parse-blocks
  "Splits a string into blocks and applies a sequence of parsers to them.
   `parsers` is a sequence of `[key parser-fn]` pairs.
   Returns a map merging the results of applying each parser to its corresponding block.

   Example:
   (parse-blocks \"1\\n\\n2\" [[:a parse-int] [:b parse-int]])
   => {:a 1 :b 2}"
  [s parsers]
  (apply merge
         (map (fn [block [k f]] {k (f block)})
              (blocks s) parsers)))

(defn- read-int [s]
  ;; read-string treats a leading '0' as an octal number. Sigh.
  (read-string (cond
                 (String/.equals s "0") s
                 (str/starts-with? s "0") (subs s 1)
                 (str/starts-with? s "-0") (str "-" (subs s 2))
                 :else s)))

(defn ints
  "Returns a vector of all integers found in the string `s`.
   Handles negative numbers."
  [s]
  (mapv read-int (re-seq #"-?\d+" s)))

(defn int
  "Returns the first integer found in the string `s`.
   Handles negative numbers."
  [s]
  (first (ints s)))

(defn pos-ints
  "Returns a vector of all positive integers found in the string `s`, parsed as integers.
   Note that negative signs '-' are treated as seperators."
  [s]
  (mapv read-int (re-seq #"\d+" s)))

(defn digit
  "Converts a character digit to its integer value."
  [chr]
  (^[char] Character/getNumericValue chr))

(defn digits
  "Returns a vector of digits from a number or string."
  [n]
  (mapv digit (str n)))

(defn re-seq-overlapping
  "Returns a lazy sequence of all overlapping matches of the regex `re` in string `s`.
   Uses a lookahead assertion to find matches without consuming characters.

   Example:
   (re-seq-overlapping #\"one|eight\" \"oneight\")
   ;; => (\"one\" \"eight\")

   Note: Regex alternation is eager. If multiple alternatives match at the same
   position, only the first one in the regex will be returned.

   Example of limitation:
   (re-seq-overlapping #\"foo|bar|foob\" \"foobar\")
   ;; => (\"foo\" \"bar\")
   ;; \"foob\" is not returned because \"foo\" matches first at index 0."
  [re s]
  (map second (re-seq (re-pattern (str "(?=(" re "))")) s)))

(defn re-indices [pattern s]
  (let [m (re-matcher (re-pattern pattern) s)]
    ((fn step []
       (when (Matcher/.find m)
         (cons [(Matcher/.start m) (Matcher/.end m)]
               (lazy-seq (step))))))))

(defn substring-replace [s [start end] replacement]
  (str (subs s 0 start) replacement (subs s end)))

(defn to-hex
  "Converts a number to its hexadecimal string representation.
   Supports optional minimum padding width and padding character (default '0')."
  ([n] (cl-format nil "~X" n))
  ([n min-padding] (cl-format nil (str "~" min-padding ",'0X") n))
  ([n min-padding padding-char]
   (cl-format nil (str "~" min-padding ",'" padding-char "X") n)))

(defn to-binary
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
