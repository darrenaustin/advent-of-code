(ns aoc.util.memoize)

(defmacro letfn-mem
  "Defines local memoized functions. The functions can recursively call
   themselves (and each other) using the bound names, and those calls will be memoized.
   The syntax mimics `letfn`.

   Usage:
   (letfn-mem [(fib [n]
                 (if (<= n 1)
                   n
                   (+ (fib (- n 1)) (fib (- n 2)))))]
     (fib 100))"
  {:clj-kondo/lint-as 'clojure.core/letfn}
  [fnspecs & body]
  (let [caches (repeatedly (count fnspecs) #(gensym "cache"))
        new-specs (map (fn [spec cache]
                         (let [[fname & decls] spec]
                           `(~fname [& args#]
                                    (if-let [e# (find @~cache args#)]
                                      (val e#)
                                      (let [res# (apply (fn ~@decls) args#)]
                                        (swap! ~cache assoc args# res#)
                                        res#)))))
                       fnspecs caches)]
    `(let [~@(interleave caches (repeat `(atom {})))]
       (letfn [~@new-specs]
         ~@body))))
