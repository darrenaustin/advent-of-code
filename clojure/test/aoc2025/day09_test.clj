;; Test for aoc2025.day9
(ns aoc2025.day09-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2025.day09 :as d]
            [clojure.test :refer :all]))

(def example
  "7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3")

(deftest part1-example
  (is (= 50 (d/part1 example))))

(deftest part2-example
  (is (= 24 (d/part2 example))))

(def answers (delay (day-answers 2025 9)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))


;; (def sample-input
;;   "7,1
;; 11,1
;; 11,7
;; 9,7
;; 9,5
;; 2,5
;; 2,3
;; 7,3")

;; (deftest part2-test
;;   (testing "point-in-poly? logic"
;;     (let [tiles (parse-tiles sample-input)
;;           lines (conj (map vector (drop-last tiles) (drop 1 tiles))
;;                       [(last tiles) (first tiles)])]
;;       ;; (4,4) is inside
;;       (is (true? (point-in-poly? lines [4 4])))
;;       ;; (1,4) is outside (left of x=2)
;;       (is (false? (point-in-poly? lines [1 4])))
;;       ;; (12,4) is outside (right of x=11)
;;       (is (false? (point-in-poly? lines [12 4])))
;;       ;; (8, 2) is inside (between x=7 and x=11, y=2)
;;       ;; y=2 crosses x=7 (1-3) and x=11 (1-7).
;;       ;; Ray from 8 to right crosses x=11. 1 crossing. Inside.
;;       (is (true? (point-in-poly? lines [8 2])))))

;;   (testing "step shape"
;;     (let [points [[10 0] [10 5] [15 5] [15 10] [0 10] [0 0]]
;;           lines (conj (map vector (drop-last points) (drop 1 points))
;;                       [(last points) (first points)])]
;;       ;; (5, 5) is inside.
;;       ;; Ray to right crosses (10,0)-(10,5) at y=5 and (15,5)-(15,10) at y=5.
;;       ;; If inclusive, 2 crossings -> Outside (Wrong).
;;       ;; If [min, max), 1 crossing -> Inside (Correct).
;;       (is (true? (point-in-poly? lines [5 5]))))))
