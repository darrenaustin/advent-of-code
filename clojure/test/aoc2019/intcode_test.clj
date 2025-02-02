(ns aoc2019.intcode-test
  (:require [aoc2019.intcode :as i]
            [clojure.test :refer :all]))

(deftest execute-day02-examples
  (are [expected input]
    (= expected (i/mem->vec (i/execute input)))
    [3500 9 10 70 2 3 11 0 99 30 40 50] [1 9 10 3 2 3 11 0 99 30 40 50]
    [2 0 0 0 99] [1 0 0 0 99]
    [2 3 0 6 99] [2 3 0 3 99]
    [2 4 4 5 99 9801] [2 4 4 5 99 0]
    [30 1 1 4 2 5 6 0 99] [1 1 1 4 99 5 6 0 99]))

(deftest immediate-mode-test
  (is (= [1002 4 3 4 99] (i/mem->vec (i/execute [1002 4 3 4 33])))))

(deftest negative-params-test
  (is (= [1101 100 -1 4 99] (i/mem->vec (i/execute [1101 100 -1 4 0])))))

(deftest input-test
  (is (= [3 7 3 8 3 9 99 1 2 3] (i/mem->vec (i/execute [3 7 3 8 3 9 99 0 0 0] '(1 2 3) nil)))))

(deftest output-test
  (is (= [1 2 3 104] (:output (i/execute [104 1 104 2 104 3 4 0 99])))))

(deftest jump-if-true-test
  (is (= 9 (:pc (i/execute [1105 1 9 99 0 0 0 0 0 99]))))
  (is (= 3 (:pc (i/execute [1105 0 9 99 0 0 0 0 0 99])))))

(deftest jump-if-false-test
  (is (= 3 (:pc (i/execute [1106 1 9 99 0 0 0 0 0 99]))))
  (is (= 9 (:pc (i/execute [1106 0 9 99 0 0 0 0 0 99])))))

(deftest less-than-test
  (is (= [1107 1 9 5 99 1] (i/mem->vec (i/execute [1107 1 9 5 99 0]))))
  (is (= [1107 9 9 5 99 0] (i/mem->vec (i/execute [1107 9 9 5 99 0]))))
  (is (= [1107 9 1 5 99 0] (i/mem->vec (i/execute [1107 9 1 5 99 0])))))

(deftest equals-test
  (is (= [1108 1 9 5 99 0] (i/mem->vec (i/execute [1108 1 9 5 99 0]))))
  (is (= [1108 9 9 5 99 1] (i/mem->vec (i/execute [1108 9 9 5 99 0]))))
  (is (= [1108 9 1 5 99 0] (i/mem->vec (i/execute [1108 9 1 5 99 0])))))

(deftest compare-8-test
  ; position mode equals
  (is (= [1] (:output (i/execute [3 9 8 9 10 9 4 9 99 -1 8] '(8) []))))
  (is (= [0] (:output (i/execute [3 9 8 9 10 9 4 9 99 -1 8] '(0) []))))

  ; position mode less than
  (is (= [1] (:output (i/execute [3 9 7 9 10 9 4 9 99 -1 8] '(4) []))))
  (is (= [0] (:output (i/execute [3 9 7 9 10 9 4 9 99 -1 8] '(8) []))))
  (is (= [0] (:output (i/execute [3 9 7 9 10 9 4 9 99 -1 8] '(10) []))))

  ; immediate mode equals
  (is (= [1] (:output (i/execute [3 3 1108 -1 8 3 4 3 99] '(8) []))))
  (is (= [0] (:output (i/execute [3 3 1108 -1 8 3 4 3 99] '(4) []))))

  ; immediate mode less than
  (is (= [1] (:output (i/execute [3 3 1107 -1 8 3 4 3 99] '(4) []))))
  (is (= [0] (:output (i/execute [3 3 1107 -1 8 3 4 3 99] '(8) []))))
  (is (= [0] (:output (i/execute [3 3 1107 -1 8 3 4 3 99] '(10) [])))))

(deftest non-zero-test
  (is (= [1] (:output (i/execute [3 12 6 12 15 1 13 14 13 4 13 99 -1 0 1 9] '(10) []))))
  (is (= [0] (:output (i/execute [3 12 6 12 15 1 13 14 13 4 13 99 -1 0 1 9] '(0) [])))))

(deftest large-comparison-test
  (let [program [3 21 1008 21 8 20 1005 20 22 107 8 21 20 1006 20 31 1106 0 36 98 0 0 1002 21 125 20 4 20 1105 1 46 104 999 1105 1 46 1101 1000 1 20 4 20 1105 1 46 98 99]]
    (is (= [999] (:output (i/execute program '(4) []))))
    (is (= [1000] (:output (i/execute program '(8) []))))
    (is (= [1001] (:output (i/execute program '(42) []))))))

(deftest adjust-relative-base-test
  (is (= 4242 (:relative-base (i/execute [109 4242 99]))))
  (is (= 100 (:relative-base (i/execute [109 4242 109 -4142 99])))))

(deftest relative-base-test
  (is (= [42] (:output (i/execute [109 -1 204 6 99 42 -10]))))
  (is (= 42 ((:mem (i/execute [109 -1 21102 7 6 101 99])) 100))))

(deftest copy-itself-test
  (let [program [109 1 204 -1 1001 100 1 100 1008 100 16 101 1006 101 0 99]]
    (is (= program (:output (i/execute program))))))

(deftest large-nums-test
  (is (= [1219070632396864] (:output (i/execute [1102 34915192 34915192 7 4 7 99 0]))))
  (is (= [1125899906842624] (:output (i/execute [104 1125899906842624 99])))))
