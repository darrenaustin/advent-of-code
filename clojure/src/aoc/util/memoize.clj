(ns aoc.util.memoize)

(defmacro let-memoized
  "Defines a memoized function locally. The function can recursively call itself
   using the bound name, and those calls will be memoized.

   Usage:
   (let-memoized [fib (fn [n]
                        (if (<= n 1)
                          n
                          (+ (fib (dec n)) (fib (- n 2)))))]
     (fib 50))"
  [[name f-decl] & body]
  `(let [cache# (atom {})]
     (letfn [(~name [& args#]
               (if-let [e# (find @cache# args#)]
                 (val e#)
                 (let [res# (apply ~f-decl args#)]
                   (swap! cache# assoc args# res#)
                   res#)))]
       ~@body)))
