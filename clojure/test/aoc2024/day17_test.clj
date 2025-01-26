;; Test for aoc2024.day17
(ns aoc2024.day17-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day17 :as d]
            [clojure.test :refer :all]))

(defn machine [params]
  (merge {:a 0 :b 0 :c 0 :code [] :pc 0 :output []} params))

(deftest execute-examples
  ;If register C contains 9, the program 2,6 would set register B to 1.
  (is (= 1 (:b (d/execute (machine {:c 9 :code [2 6]})))))

  ;If register A contains 10, the program 5,0,5,1,5,4 would output 0,1,2.
  (is (= [0 1 2] (:output (d/execute (machine {:a 10 :code [5 0 5 1 5 4]})))))

  ;If register A contains 2024, the program 0,1,5,4,3,0 would output 4,2,5,6,7,7,7,7,3,1,0 and leave 0 in register A.
  (let [result (d/execute (machine {:a 2024 :code [0 1 5 4 3 0]}))]
    (is (and (= [4 2 5 6 7 7 7 7 3 1 0] (:output result))
             (zero? (:a result)))))

  ;If register B contains 29, the program 1,7 would set register B to 26.
  (is (= 26 (:b (d/execute (machine {:b 29 :code [1 7]})))))

  ;If register B contains 2024 and register C contains 43690, the program 4,0 would set register B to 44354.
  (is (= 44354 (:b (d/execute (machine {:b 2024 :c 43690 :code [4 0]}))))))

(def example1
  "Register A: 729
Register B: 0
Register C: 0

Program: 0,1,5,4,3,0")

(deftest part1-example
  (is (= "4,6,3,5,6,3,5,2,1,0" (d/part1 example1))))

(def example2
  "Register A: 2024
Register B: 0
Register C: 0

Program: 0,3,5,4,3,0")

(deftest part2-example
  (is (= 117440 (d/part2 example2))))

(def answers (delay (day-answers 2024 17)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
