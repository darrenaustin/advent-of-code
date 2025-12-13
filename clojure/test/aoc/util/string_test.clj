(ns aoc.util.string-test
  (:require
   [aoc.util.string :as s]
   [clojure.test :refer :all]))

(deftest blocks-test
  (testing "blocks splits on double newlines"
    (is (= ["a" "b"] (s/blocks "a\n\nb")))
    (is (= ["a" "b"] (s/blocks "a\n  \nb")))
    (is (= ["a" "b" "c"] (s/blocks "a\n\nb\n\nc")))))

(deftest lines-test
  (testing "lines splits on newlines"
    (is (= ["a" "b"] (s/lines "a\nb")))
    (is (= ["a" "b" "c"] (s/lines "a\nb\nc")))
    (is (= ["a" "" "b"] (s/lines "a\n\nb")))))

(deftest parse-blocks-test
  (testing "parse-blocks applies parsers to blocks"
    (let [input "10\n\n20\n\nfoo"]
      (is (= {:a 10 :b 20 :c "foo"}
             (s/parse-blocks input
                             [[:a s/int]
                              [:b s/int]
                              [:c identity]]))))))

(deftest ints-test
  (testing "ints extracts all integers from a string"
    (is (= [1 2 3] (s/ints "1 2 3")))
    (is (= [1 2 3] (s/ints "1, 2, 3")))
    (is (= [-1 -2 -3] (s/ints "-1 -2 -3")))
    (is (= [123 -456] (s/ints "foo 123 bar -456")))
    (is (= [] (s/ints "no numbers here")))))

(deftest int-test
  (testing "int extracts the first integer from a string"
    (is (= 123 (s/int "123")))
    (is (= 123 (s/int "foo 123")))
    (is (= -123 (s/int "-123")))
    (is (nil? (s/int "no numbers here")))))

(deftest pos-ints-test
  (testing "pos-ints extracts all sequences of digits as integers"
    (is (= [1 2 3] (s/pos-ints "1 2 3")))
    (is (= [1 2 3] (s/pos-ints "-1 -2 -3")))
    (is (= [123 456] (s/pos-ints "123-456")))
    (is (= [] (s/pos-ints "no digits here")))))

(deftest digit-test
  (testing "digit converts char to int"
    (is (= 1 (s/digit \1)))
    (is (zero? (s/digit \0)))
    (is (= 9 (s/digit \9)))))

(deftest digits-test
  (testing "digits converts number/string to vector of digits"
    (is (= [1 2 3] (s/digits 123)))
    (is (= [1 2 3] (s/digits "123")))))

(deftest to-hex-test
  (testing "to-hex converts numbers to hex strings"
    (is (= "ff" (s/to-hex 255)))
    (is (= "0" (s/to-hex 0)))
    (is (= "a" (s/to-hex 10))))

  (testing "to-hex supports padding"
    (is (= "0a" (s/to-hex 10 2)))
    (is (= "00ff" (s/to-hex 255 4)))
    (is (= "ff" (s/to-hex 255 2))))

  (testing "to-hex supports custom padding character"
    (is (= " a" (s/to-hex 10 2 \space)))
    (is (= "__a" (s/to-hex 10 3 \_)))))

(deftest to-binary-test
  (testing "to-binary converts numbers to binary strings"
    (is (= "101" (s/to-binary 5)))
    (is (= "0" (s/to-binary 0)))
    (is (= "1111" (s/to-binary 15))))

  (testing "to-binary supports padding"
    (is (= "0101" (s/to-binary 5 4)))
    (is (= "00000101" (s/to-binary 5 8)))
    (is (= "101" (s/to-binary 5 2))))

  (testing "to-binary supports custom padding character"
    (is (= " 101" (s/to-binary 5 4 \space)))
    (is (= "_101" (s/to-binary 5 4 \_)))))

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
