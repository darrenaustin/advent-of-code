(ns aoc.day
  (:require [clojure.data.json :as json]
            [clojure.string :as str])
  (:import (java.io StringWriter)))

;; TODO: add a way to override this with an env var.
(defn input-repo-dir [] "../inputs")

(defn input-file-name [year day-num]
  (format "%s/%04d/%02d_input.txt" (input-repo-dir) year day-num))

(defn answer-file-name [year day-num]
  (format "%s/%04d/%02d_answer.json" (input-repo-dir) year day-num))

(defn day-input
  ([year day-num] (day-input year day-num true))
  ([year day-num trim?]
   (try
     (let [content (slurp (input-file-name year day-num))]
       (if trim? (str/trim content) content))
     (catch Exception _ ""))))

(defn day-answers [year day-num]
  (try
    (json/read-str (slurp (answer-file-name year day-num)) :key-fn keyword)
    (catch Exception _ {})))

(defn day-var [year day-num var-name]
  (find-var (symbol (format "aoc%d.day%02d/%s" year day-num var-name))))

(defn day-val [year day-num var-name]
  (when-let [var (day-var year day-num var-name)]
    (var-get var)))

;; From https://stackoverflow.com/questions/62724497/how-can-i-record-time-for-function-call-in-clojure
(defmacro time-execution
  [& body]
  `(let [s# (StringWriter.)]
     (binding [*out* s#]
       {:return (time ~@body)
        :time   (int (read-string (str/replace (str s#) #"[^0-9\\.]" "")))})))

(defn result [answer expected time]
  (let [correct   (cond
                    (nil? expected) ""
                    (= answer expected) "correct, "
                    :else "INCORRECT, ")
        [secs ms] [(quot time 1000) (rem time 1000)]
        time-desc (if (zero? secs)
                    (str ms "ms")
                    (str secs "s " ms "ms <TOO SLOW>"))]
    (println (format "%s, %s%s" answer correct time-desc))))

(defn execute [year day-num]
  (let [input   (or ((day-val year day-num "input")) "")
        answers (day-answers year day-num)
        name    (or (:name answers) "<UNKNOWN>")
        part1   (day-var year day-num "part1")
        part2   (day-var year day-num "part2")]
    (println (format "%d Day %d: %s" year, day-num name))
    (let [p1 (time-execution (part1 input))]
      (print "  part 1: ")
      (result (:return p1) (:answer1 answers) (:time p1)))
    (let [p2 (time-execution (part2 input))]
      (print "  part 2: ")
      (result (:return p2) (:answer2 answers) (:time p2)))
    (println)))
