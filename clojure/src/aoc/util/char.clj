(ns aoc.util.char
  "Utility functions for working with Java Characters.")

(defn lower-case?
  "Returns true if the character is lowercase."
  {:inline (fn [c] `(Character/isLowerCase ~c))}
  [^Character c]
  (Character/isLowerCase c))

(defn lower-case
  "Converts the character to lowercase."
  {:inline (fn [c] `(Character/toLowerCase ~c))}
  [^Character c]
  (Character/toLowerCase c))

(defn upper-case?
  "Returns true if the character is uppercase."
  {:inline (fn [c] `(Character/isUpperCase ~c))}
  [^Character c]
  (Character/isUpperCase c))

(defn upper-case
  "Converts the character to uppercase."
  {:inline (fn [c] `(Character/toUpperCase ~c))}
  [^Character c]
  (Character/toUpperCase c))

(defn digit?
  "Returns true if the character is a digit."
  {:inline (fn [c] `(Character/isDigit ~c))}
  [^Character c]
  (Character/isDigit c))

(defn digit
  "Converts a digit character to its integer value (0-9)."
  {:inline (fn [c] `(Character/getNumericValue ~c))}
  [^Character c]
  (Character/getNumericValue c))
