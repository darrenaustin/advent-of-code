(ns aoc.util.char-test
  (:require
   [aoc.util.char :as c]
   [clojure.test :refer :all]))

(deftest lower-case?-test
  (is (true? (c/lower-case? \a)))
  (is (false? (c/lower-case? \A)))
  (is (false? (c/lower-case? \1))))

(deftest lower-case-test
  (is (= \a (c/lower-case \a)))
  (is (= \a (c/lower-case \A))))

(deftest upper-case?-test
  (is (true? (c/upper-case? \A)))
  (is (false? (c/upper-case? \a)))
  (is (false? (c/upper-case? \1))))

(deftest upper-case-test
  (is (= \A (c/upper-case \a)))
  (is (= \A (c/upper-case \A))))

(deftest digit?-test
  (is (true? (c/digit? \0)))
  (is (true? (c/digit? \9)))
  (is (false? (c/digit? \a))))

(deftest digit-test
  (is (= 0 (c/digit \0)))
  (is (= 5 (c/digit \5)))
  (is (= 9 (c/digit \9))))
