(ns intseq.ops
  (:require [clojure.math.numeric-tower :as math]))

(defn int-add [stack]
  (if (< (count stack) 2)
    stack
    (cons (+ (second stack) (first stack))
          (rest (rest stack)))))

(defn int-sub [stack]
  (if (< (count stack) 2)
    stack
    (cons (- (second stack) (first stack))
          (rest (rest stack)))))

(defn int-mult [stack]
  (if (< (count stack) 2)
    stack
    (cons (* (second stack) (first stack))
          (rest (rest stack)))))

(defn int-div [stack]
  (if (or (< (count stack) 2)
          (zero? (first stack)))
    stack
    (cons (math/floor (/ (second stack) (first stack)))
          (rest (rest stack)))))

(defn int-mod [stack]
  (if (or (< (count stack) 2)
          (zero? (first stack)))
    stack
    (cons (mod (second stack) (first stack))
          (rest (rest stack)))))

(defn int-expt [stack]
  (if (or (< (count stack) 2)
          (neg? (first stack)))
    stack
    (cons (math/expt (second stack) (first stack))
          (rest (rest stack)))))