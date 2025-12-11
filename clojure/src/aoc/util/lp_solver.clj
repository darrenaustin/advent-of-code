(ns aoc.util.lp-solver
  (:require
   [clojure.math :refer [ceil floor round]])
  (:import
   [org.apache.commons.math3.optim MaxIter OptimizationData PointValuePair]
   [org.apache.commons.math3.optim.linear
    LinearConstraint
    LinearConstraintSet
    LinearObjectiveFunction
    NoFeasibleSolutionException
    NonNegativeConstraint
    Relationship
    SimplexSolver
    UnboundedSolutionException]
   [org.apache.commons.math3.optim.nonlinear.scalar GoalType]))

(defn- round-if-close [^double x]
  (let [r (round x)]
    (if (< (abs (- x r)) 1e-9)
      r
      x)))

(defn solve-lp
  "Solves a linear programming problem.
   objective: vector of coefficients for the objective function
   constraints: sequence of [coeffs relationship value]
     where relationship is :leq, :geq, or :eq
   goal: :minimize or :maximize"
  [objective constraints goal]
  (let [solver (SimplexSolver. 1e-10 10)
        obj-func (LinearObjectiveFunction. (double-array objective) 0.0)
        rel-map {:leq Relationship/LEQ
                 :geq Relationship/GEQ
                 :eq  Relationship/EQ}
        constraint-set (for [[coeffs rel val] constraints]
                         (LinearConstraint. (double-array coeffs)
                                            (rel-map rel)
                                            (double val)))
        goal-type (if (= goal :maximize) GoalType/MAXIMIZE GoalType/MINIMIZE)
        constraint-set-obj (LinearConstraintSet. constraint-set)
        opt-data [obj-func goal-type (MaxIter. 10000) constraint-set-obj (NonNegativeConstraint. true)]
        arr (make-array OptimizationData (count opt-data))]
    (dotimes [i (count opt-data)]
      (aset arr i (nth opt-data i)))
    (let [^PointValuePair solution (SimplexSolver/.optimize solver arr)]
      {:point (mapv round-if-close (PointValuePair/.getPoint solution))
       :value (round-if-close (PointValuePair/.getValue solution))})))

(defn solve-ilp
  "Solves an Integer Linear Programming problem using Branch and Bound.
   Wraps solve-lp.
   objective: vector of coefficients for the objective function
   constraints: sequence of [coeffs relationship value]
   goal: :minimize or :maximize"
  [objective constraints goal]
  (let [best-res (atom nil)
        stack (atom [constraints])]
    (while (seq @stack)
      (let [curr-constraints (peek @stack)
            _ (swap! stack pop)
            res (try (solve-lp objective curr-constraints goal)
                     (catch NoFeasibleSolutionException _ nil)
                     (catch UnboundedSolutionException _ nil))]
        (when res
          (let [pt (mapv round-if-close (:point res))
                val (:value res)]
            ;; Pruning: if we maximize, and val <= best-val, drop.
            ;; If we minimize, and val >= best-val, drop.
            (when-not (and @best-res
                           (if (= goal :maximize)
                             (<= val (:value @best-res))
                             (>= val (:value @best-res))))
              (if (every? integer? pt)
                (reset! best-res {:point pt :value val}) ;; Found a better integer solution
                ;; Branch
                (let [idx (first (keep-indexed (fn [i x] (when-not (integer? x) i)) pt))
                      val (nth pt idx)
                      floor (floor val)
                      ceil (ceil val)
                      coeffs (assoc (vec (repeat (count objective) 0)) idx 1)]
                  ;; Add constraints: x <= floor, x >= ceil
                  (swap! stack conj (conj curr-constraints [coeffs :geq ceil]))
                  (swap! stack conj (conj curr-constraints [coeffs :leq floor])))))))))
    @best-res))
