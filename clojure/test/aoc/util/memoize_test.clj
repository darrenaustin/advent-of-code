(ns aoc.util.memoize-test
  (:require
   [aoc.util.memoize :refer [letfn-mem]]
   [clojure.test :refer [deftest is testing]]))

(deftest letfn-mem-test
  (testing "Single recursive function (Fibonacci)"
    (let [call-count (atom 0)]
      (letfn-mem [(fib [n]
                       (swap! call-count inc)
                       (if (<= n 1)
                         n
                         (+ (fib (- n 1)) (fib (- n 2)))))]
                 (is (= 55 (fib 10)))
                 ;; For fib(10), without memoization it would be 177 calls.
                 ;; With memoization, it should be roughly 11 calls (0 to 10).
                 (is (<= @call-count 11)))))

  (testing "Multiple mutually recursive functions (Even/Odd)"
    (let [even-calls (atom 0)
          odd-calls (atom 0)]
      (letfn-mem [(my-even? [n]
                            (swap! even-calls inc)
                            (if (zero? n)
                              true
                              (my-odd? (dec n))))
                  (my-odd? [n]
                           (swap! odd-calls inc)
                           (if (zero? n)
                             false
                             (my-even? (dec n))))]

                 ;; First call: cold cache
                 (is (true? (my-even? 4)))
                 ;; even(4) -> odd(3) -> even(2) -> odd(1) -> even(0)
                 ;; 3 calls to even, 2 calls to odd.
                 (is (= 3 @even-calls))
                 (is (= 2 @odd-calls))

                 ;; Second call: warm cache
                 (reset! even-calls 0)
                 (reset! odd-calls 0)
                 (is (true? (my-even? 4)))
                 (is (= 0 @even-calls))
                 (is (= 0 @odd-calls))

                 ;; New call: partial cache hit
                 ;; odd(5) -> even(4) (Cached!)
                 (reset! even-calls 0)
                 (reset! odd-calls 0)
                 (is (true? (my-odd? 5)))
                 (is (= 1 @odd-calls)) ;; odd(5) called
                 (is (= 0 @even-calls)) ;; even(4) hit cache
                 ))))
