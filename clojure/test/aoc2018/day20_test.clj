;; Test for aoc2018.day20
(ns aoc2018.day20-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day20 :as d]
            [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
    (= expected (d/part1 input))
    3 "^WNE$"
    10 "^ENWWW(NEEE|SSE(EE|N))$"
    18 "^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$"
    23 "^ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))$"
    31 "^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))$"))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2018 20) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
