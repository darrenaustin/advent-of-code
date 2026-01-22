(ns aoc.util.string
  (:refer-clojure :exclude [int ints])
  (:require
   [aoc.util.char :as char]
   [clojure.pprint :refer [cl-format]]
   [clojure.string :as str])
  (:import
   [java.util.regex Matcher]))

(def alphabet-lower "abcdefghijklmnopqrstuvwxyz")
(def alphabet-upper "ABCDEFGHIJKLMNOPQRSTUVWXYZ")

(defn blocks
  "Splits a string into blocks separated by double newlines (blank lines)."
  {:inline (fn [s] `(str/split ~s #"\n\s*\n"))}
  [s]
  (str/split s #"\n\s*\n"))

(defn lines
  "Splits a string into a sequence of lines."
  {:inline (fn [s] `(str/split-lines ~s))}
  [s]
  (str/split-lines s))

(defn parse-blocks
  "Splits a string into blocks and applies a sequence of parsers to them.
   `parsers` is a sequence of `[key parser-fn]` pairs.
   Returns a sequence of the results in the same order as the parser-fns.

   Example:
   (parse-blocks \"1\\n\\n2\" [int #(inc int)])
   => (1 3)"
  [s parsers]
  (map (fn [block parser] (parser block))
       (blocks s) parsers))

(defn parse-blocks-map
  "Splits a string into blocks and applies a sequence of parsers to them.
   `parsers` is a sequence of `[key parser-fn]` pairs.
   Returns a map merging the results of applying each parser to its corresponding block.

   Example:
   (parse-blocks-map \"1\\n\\n2\" [[:a int] [:b int]])
   => {:a 1 :b 2}"
  [s parsers]
  (apply merge
         (map (fn [block [k f]] {k (f block)})
              (blocks s) parsers)))

(defn- read-int
  ([s] (read-int s 10))
  ([s radix]
   ;; Strip leading zeros to prevent octal interpretation in base 10,
   ;; but preserve the sign and ensure we don't strip a lone "0".
   (let [clean-s (str/replace s #"^(-?)0+(?=\d)" "$1")]
     (read-string (if (= 10 radix)
                    clean-s
                    (str radix "r" clean-s))))))

(defn- extract-ints [s regex radix]
  (when (seq s)
    (mapv #(read-int % radix) (re-seq regex s))))

(defn ints
  "Returns a vector of all integers found in the string `s`.
   Handles negative numbers.
   Optionally accepts a `radix` (base) for parsing (default 10)."
  ([s] (extract-ints s #"-?\d+" 10))
  ([s radix] (extract-ints s #"-?\d+" radix)))

(defn pos-ints
  "Returns a vector of all positive integers found in the string `s`, parsed as integers.
   Note that negative signs '-' are not treated as part of the number, so \"-10\" becomes [10].
   Optionally accepts a `radix` (base) for parsing (default 10)."
  ([s] (extract-ints s #"\d+" 10))
  ([s radix] (extract-ints s #"\d+" radix)))

(defn int
  "Returns the first integer found in the string `s`.
   Handles negative numbers.
   Optionally accepts a `radix` (base) for parsing (default 10)."
  ([s] (first (ints s)))
  ([s radix] (first (ints s radix))))

(defn digits
  "Returns a sequence of digits from a string."
  [s]
  (map char/digit (str s)))

(defn ascii?
  "Returns true if n is a valid ASCII code (0-127)."
  [n]
  (<= 0 n 127))

(defn str->ascii
  "Converts a string to a sequence of ASCII codes."
  [s]
  (map clojure.core/int s))

(defn ascii->str
  "Converts a sequence of ASCII codes to a string."
  [as]
  (str/join (map char as)))

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
