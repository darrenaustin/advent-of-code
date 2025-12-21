(ns aoc.util.bounded-priority-map
  {:clj-kondo/ignore [:unused-binding]}
  (:require
   [clojure.data.priority-map :as pm])
  (:import
   [clojure.lang
    Associative
    IFn
    IHashEq
    ILookup
    IPersistentCollection
    IPersistentMap
    MapEquivalence
    Seqable
    Sorted]))

(deftype BoundedPriorityMap [pm bound]
  IPersistentMap
  (assoc [this k v]
    (if (contains? pm k)
      (BoundedPriorityMap. (assoc pm k v) bound)
      (if (or (nil? bound) (< (count pm) bound))
        (BoundedPriorityMap. (assoc pm k v) bound)
        (let [last-entry (first (rseq pm))
              comp (.comparator ^Sorted pm)
              should-add? (if comp
                            (neg? (.compare comp v (val last-entry)))
                            (neg? (compare v (val last-entry))))]
          (if should-add?
            (BoundedPriorityMap. (-> pm (dissoc (key last-entry)) (assoc k v)) bound)
            this)))))
  (assocEx [this k v]
    (if (contains? pm k)
      (throw (Exception. "Key already present"))
      (.assoc this k v)))
  (without [this k]
    (BoundedPriorityMap. (dissoc pm k) bound))

  IPersistentCollection
  (count [this] (count pm))
  (cons [this o]
    (if (map? o)
      (reduce (fn [m [k v]] (.assoc m k v)) this o)
      (if (vector? o)
        (.assoc this (nth o 0) (nth o 1))
        (throw (Exception. "Don't know how to cons to BoundedPriorityMap")))))
  (empty [this] (BoundedPriorityMap. (empty pm) bound))
  (equiv [this o]
    (and (instance? BoundedPriorityMap o)
         (= bound (.bound ^BoundedPriorityMap o))
         (.equiv pm (.pm ^BoundedPriorityMap o))))

  Seqable
  (seq [this] (seq pm))

  ILookup
  (valAt [this k] (.valAt pm k))
  (valAt [this k not-found] (.valAt pm k not-found))

  Associative
  (containsKey [this k] (.containsKey pm k))
  (entryAt [this k] (.entryAt pm k))

  Sorted
  (comparator [this] (.comparator ^Sorted pm))
  (entryKey [this k] (.entryKey ^Sorted pm k))
  (seq [this ascending] (.seq ^Sorted pm ascending))
  (seqFrom [this k ascending] (.seqFrom ^Sorted pm k ascending))

  MapEquivalence

  IHashEq
  (hasheq [this] (hash [pm bound]))

  Object
  (hashCode [this] (hash [pm bound]))
  (equals [this o]
    (and (instance? BoundedPriorityMap o)
         (= bound (.bound ^BoundedPriorityMap o))
         (.equals pm (.pm ^BoundedPriorityMap o))))
  (toString [this] (str pm))

  IFn
  (invoke [this k] (.valAt pm k))
  (invoke [this k not-found] (.valAt pm k not-found))

  java.lang.Iterable
  (iterator [this] (.iterator ^java.lang.Iterable pm)))

(defn bounded-priority-map
  "Creates a new bounded priority map with the given capacity bound.
   If bound is nil, behaves like a standard priority map.
   Additional args are key-value pairs."
  [bound & keyvals]
  (let [pm (apply pm/priority-map keyvals)]
    (if (and bound (> (count pm) bound))
      (BoundedPriorityMap. (into (empty pm) (take bound pm)) bound)
      (BoundedPriorityMap. pm bound))))

(defn bounded-priority-map-by
  "Creates a new bounded priority map with the given comparator and capacity bound.
   If bound is nil, behaves like a standard priority map.
   Additional args are key-value pairs."
  [comparator bound & keyvals]
  (let [pm (apply pm/priority-map-by comparator keyvals)]
    (if (and bound (> (count pm) bound))
      (BoundedPriorityMap. (into (empty pm) (take bound pm)) bound)
      (BoundedPriorityMap. pm bound))))

(defmethod print-method BoundedPriorityMap
  print-method-bounded-priority-map
  [o w]
  (.write w (str o)))
