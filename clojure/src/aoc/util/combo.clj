(ns aoc.util.combo)

(defn weak-compositions-step
  "Logic for generating weak compositions, mostly used internally
   by weak-compositions.
   This can however be used manually to control the memoization if
   needed.
   recur-fn: The function to call for recursive steps.
   n: Number of parts.
   total: The sum to reach."
  [recur-fn n total]
  (if (= n 1)
    [[total]]
    (for [x (range (inc total))
          y (recur-fn (dec n) (- total x))]
      (cons x y))))

(def weak-compositions
  "Returns all sequences of length n of non-negative integers that sum to total.
   Memoized by default.

   Examples:
   ;; Basic usage (memoized by default)
   (weak-compositions 3 5)
   ;; => ((0 0 5) (0 1 4) (0 2 3) ... (5 0 0))

   ;; Manual control over memoization (e.g. no memoization)
   (defn no-memo-weak-compositions [n total]
     (weak-compositions-step no-memo-weak-compositions n total))

   (no-memo-weak-compositions 3 5)

   ;; Custom memoization (e.g. using a different cache strategy)
   (def custom-memo-weak-compositions
     (memoize ;; or core.memoize/lru, etc.
      (fn [n total]
        (weak-compositions-step custom-memo-weak-compositions n total))))"
  (memoize
   (fn [n total]
     (weak-compositions-step weak-compositions n total))))
