(ns aoc.util.debug-test
  (:require
   [aoc.util.debug :refer [dbg dbg-> dbg->>]]
   [clojure.test :refer [deftest is testing]]))

(deftest dbg-test
  (testing "dbg returns the value"
    (is (= 42 (binding [*out* (java.io.StringWriter.)] (dbg 42))))
    (is (= 42 (binding [*out* (java.io.StringWriter.)] (dbg "msg" 42)))))

  (testing "dbg prints the value"
    (is (= "42\n" (with-out-str (dbg 42))))
    (is (= "The answer: 42\n" (with-out-str (dbg "The answer:" 42)))))

  (testing "dbg doesn't interfere with a let expression"
    (let [output (java.io.StringWriter.)
          result (binding [*out* output]
                   (let [xs (range 10)
                         squares (dbg (map #(* % %) xs))
                         sum (reduce + squares)]
                     sum))]
      (is (= "(0 1 4 9 16 25 36 49 64 81)\n" (str output)))
      (is (= 285 result)))))

(deftest dbg->-test
  (testing "dbg-> passes value through"
    (is (= -2 (binding [*out* (java.io.StringWriter.)] (-> 1 (dbg-> inc) -))))
    (is (= -1 (binding [*out* (java.io.StringWriter.)] (-> 1 (dbg->) -))))
    (is (= -1 (binding [*out* (java.io.StringWriter.)] (-> 1 (dbg-> "msg") -)))))

  (testing "dbg-> prints intermediate values"
    (is (= "2\n" (with-out-str (-> 1 (dbg-> inc) -))))
    (is (= "msg 2\n" (with-out-str (-> 1 (dbg-> "msg" inc) -)))))

  (testing "dbg-> doesn't interfere with a -> threaded expression"
    (let [output (java.io.StringWriter.)
          result (binding [*out* output]
                   (-> {:a 1 :b 2 :c 3}
                       (dbg-> (assoc :c 4 :d 5))
                       (update-vals inc)
                       vals))]
      (is (= "{:a 1, :b 2, :c 4, :d 5}\n" (str output)))
      (is (= [2 3 5 6] result)))))

(deftest dbg->>-test
  (testing "dbg->> passes value through"
    (is (= -2 (binding [*out* (java.io.StringWriter.)] (->> 1 (dbg->> inc) -))))
    (is (= -1 (binding [*out* (java.io.StringWriter.)] (->> 1 (dbg->>) -))))
    (is (= -1 (binding [*out* (java.io.StringWriter.)] (->> 1 (dbg->> "msg") -)))))

  (testing "dbg->> prints intermediate values"
    (is (= "2\n" (with-out-str (->> 1 (dbg->> inc) -))))
    (is (= "msg 2\n" (with-out-str (->> 1 (dbg->> "msg" inc) -)))))

  (testing "dbg->> doesn't interfere with a ->> threaded expression"
    (let [output (java.io.StringWriter.)
          result (binding [*out* output]
                   (->> (range 10)
                        (dbg->> "Squares:" (map #(* % %)))
                        (reduce +)))]
      (is (= "Squares: (0 1 4 9 16 25 36 49 64 81)\n" (str output)))
      (is (= 285 result)))))
