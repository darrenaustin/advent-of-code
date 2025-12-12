(ns aoc.util.string-test
  (:require
   [aoc.util.string :as s]
   [clojure.test :refer :all]))

(deftest split-blocks-test
  (testing "split-blocks splits on double newlines"
    (is (= ["a" "b"] (s/split-blocks "a\n\nb")))
    (is (= ["a" "b"] (s/split-blocks "a\n  \nb")))
    (is (= ["a" "b" "c"] (s/split-blocks "a\n\nb\n\nc")))))

(deftest parse-ints-test
  (testing "parse-ints extracts all integers"
    (is (= [1 2 3] (s/parse-ints "1 2 3")))
    (is (= [-1 2 -3] (s/parse-ints "-1 2 -3")))
    (is (= [10 20] (s/parse-ints "foo 10 bar 20")))
    (is (= [0 1] (s/parse-ints "0 1")))
    (is (= [0 1] (s/parse-ints "00 01")))))

(deftest parse-int-test
  (testing "parse-int extracts the first integer"
    (is (= 1 (s/parse-int "1 2 3")))
    (is (= -1 (s/parse-int "-1 2 3")))
    (is (= 10 (s/parse-int "foo 10 bar 20")))))

(deftest parse-pos-ints-test
  (testing "parse-pos-ints extracts only positive integers (digits)"
    (is (= [1 2 3] (s/parse-pos-ints "1 2 3")))
    (is (= [1 2 3] (s/parse-pos-ints "-1 2 -3"))) ;; Note: treats - as separator
    (is (= [10 20] (s/parse-pos-ints "foo 10 bar 20")))))

(deftest digit-test
  (testing "digit converts char to int"
    (is (= 1 (s/digit \1)))
    (is (zero? (s/digit \0)))
    (is (= 9 (s/digit \9)))))

(deftest digits-test
  (testing "digits converts number/string to vector of digits"
    (is (= [1 2 3] (s/digits 123)))
    (is (= [1 2 3] (s/digits "123")))))

(deftest ->hex-test
  (testing "->hex converts numbers to hex strings"
    (is (= "ff" (s/->hex 255)))
    (is (= "0" (s/->hex 0)))
    (is (= "a" (s/->hex 10))))

  (testing "->hex supports padding"
    (is (= "0a" (s/->hex 10 2)))
    (is (= "00ff" (s/->hex 255 4)))
    (is (= "ff" (s/->hex 255 2))))

  (testing "->hex supports custom padding character"
    (is (= " a" (s/->hex 10 2 \space)))
    (is (= "__a" (s/->hex 10 3 \_)))))

(deftest ->bin-test
  (testing "->bin converts numbers to binary strings"
    (is (= "101" (s/->bin 5)))
    (is (= "0" (s/->bin 0)))
    (is (= "1111" (s/->bin 15))))

  (testing "->bin supports padding"
    (is (= "0101" (s/->bin 5 4)))
    (is (= "00000101" (s/->bin 5 8)))
    (is (= "101" (s/->bin 5 2))))

  (testing "->bin supports custom padding character"
    (is (= " 101" (s/->bin 5 4 \space)))
    (is (= "_101" (s/->bin 5 4 \_)))))

(deftest string<-test
  (testing "string< checks for monotonically increasing order"
    (is (true? (s/string< "a" "b")))
    (is (true? (s/string< "a" "b" "c")))
    (is (false? (s/string< "b" "a")))
    (is (false? (s/string< "a" "a")))
    (is (false? (s/string< "a" "c" "b")))))

(deftest string>-test
  (testing "string> checks for monotonically decreasing order"
    (is (true? (s/string> "b" "a")))
    (is (true? (s/string> "c" "b" "a")))
    (is (false? (s/string> "a" "b")))
    (is (false? (s/string> "a" "a")))
    (is (false? (s/string> "c" "a" "b")))))
