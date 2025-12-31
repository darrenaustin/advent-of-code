(ns aoc.util.debug
  (:require
   [clojure.pprint :refer [pprint]]))

(defn dbg
  "Prints the value `o` (and optional `msg`) to stdout and returns `o`.
   Useful for debugging inside expressions or anywhere you want to inspect
   a value without breaking the flow.

   Example:
   (let [x 10
         y (dbg \"y:\" (* x 2))] ;; prints \"y: 20\"
     (+ x y))                  ;; returns 30"
  ([o] (pprint o) (flush) o)
  ([msg o] (print msg (with-out-str (pprint o))) (flush) o))

(defmacro dbg->
  "Threads x through form as the first parameter, printing the result.
   Can be used to inspect the value at a point in the threading
   pipeline by printing the result of the value without breaking the
   flow.

   Example:
   (-> {:a 1 :b 2 :c 3}
       (dbg-> (merge {:c 4 :d 5}))
       (update-vals inc)
       vals))

   Will print \"{:a 1, :b 2, :c 4, :d 5}\" and return (2 3 5 6)"
  ([x]
   `(let [x# ~x] (dbg x#) x#))
  ([x arg1]
   (if (string? arg1)
     `(let [x# ~x] (dbg ~arg1 x#) x#)
     (let [form (if (seq? arg1)
                  (with-meta `(~(first arg1) ~x ~@(next arg1)) (meta arg1))
                  `(~arg1 ~x))]
       `(let [res# ~form] (dbg res#) res#))))
  ([x msg form]
   (let [threaded (if (seq? form)
                    (with-meta `(~(first form) ~x ~@(next form)) (meta form))
                    `(~form ~x))]
     `(let [res# ~threaded] (dbg ~msg res#) res#))))

(defmacro dbg->>
  "Threads x through form as the last parameter, printing the result.
   Can be used to inspect the value at a point in the threading
   pipeline by printing the result of the value without breaking the
   flow.

   Example:
   (->> (range 10)
        (dbg->> \"Squares:\" (map #(* % %)))
        (reduce +)))

   Will print \"Squares: (0 1 4 9 16 25 36 49 64 81)\" and return 285"
  ([x]
   `(let [x# ~x] (dbg x#) x#))
  ([arg1 x]
   (if (string? arg1)
     `(let [x# ~x] (dbg ~arg1 x#) x#)
     (let [form (if (seq? arg1)
                  (with-meta `(~(first arg1) ~@(next arg1) ~x) (meta arg1))
                  `(~arg1 ~x))]
       `(let [res# ~form] (dbg res#) res#))))
  ([msg form x]
   (let [threaded (if (seq? form)
                    (with-meta `(~(first form) ~@(next form) ~x) (meta form))
                    `(~form ~x))]
     `(let [res# ~threaded] (dbg ~msg res#) res#))))
