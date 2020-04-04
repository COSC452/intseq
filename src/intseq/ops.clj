(ns intseq.ops
  (:require [clojure.math.numeric-tower :as math]
            [clojure.math.combinatorics :as combo]))

(defn add [stack]
  (if (< (count stack) 2)
    stack
    (cons (+ (second stack) (first stack))
          (rest (rest stack)))))

(defn sub [stack]
  (if (< (count stack) 2)
    stack
    (cons (- (second stack) (first stack))
          (rest (rest stack)))))

(defn mult [stack]
  (if (< (count stack) 2)
    stack
    (cons (* (second stack) (first stack))
          (rest (rest stack)))))

(defn div [stack]
  (if (or (< (count stack) 2)
          (zero? (first stack)))
    stack
    (cons (/ (second stack) (first stack))
          (rest (rest stack)))))

(defn mod- [stack]
  (if (or (< (count stack) 2)
          (zero? (first stack)))
    stack
    (cons (mod (second stack) (first stack))
          (rest (rest stack)))))

(defn expt [stack]
  (if (or (< (count stack) 2)
          (neg? (first stack)))
    stack
    (cons (math/expt (second stack) (first stack))
          (rest (rest stack)))))

(defn abs [stack]
  (if (< (count stack) 1)
    stack
    (cons (math/abs (first stack))
          (rest stack))))

(defn gcd [stack]
  (if (< (count stack) 2)
    stack
    (cons (math/gcd (second stack) (first stack))
          (rest (rest stack)))))

(defn lcm [stack]
  (if (< (count stack) 2)
    stack
    (cons (math/lcm (second stack) (first stack))
          (rest (rest stack)))))

(defn sqrt [stack]
  (if (< (count stack) 1)
    stack
    (cons (long (Math/sqrt (first stack)))
          (rest stack))))

(defn sin [stack]
  (if (< (count stack) 1)
    stack
    (cons (long (Math/sin (first stack)))
          (rest stack))))

(defn cos [stack]
  (if (< (count stack) 1)
    stack
    (cons (long (Math/cos (first stack)))
          (rest stack))))

(defn tan [stack]
  (if (< (count stack) 1)
    stack
    (cons (long (Math/tan (first stack)))
          (rest stack))))

(defn perm [stack]
  (if (< (count stack) 1)
    stack
    (cons (long (combo/count-permutations (range (first stack))))
          (rest stack))))

(defn comb [stack]
  (if (< (count stack) 2)
    stack
    (cons (long (combo/count-combinations (range (second stack)) (first stack)))
          (rest (rest stack)))))