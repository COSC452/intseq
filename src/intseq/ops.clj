(ns intseq.ops
  (:require [clojure.math.numeric-tower :as math]
            [clojure.math.combinatorics :as combo]))

(defn add [stack]
  (if (< (count stack) 2)
    stack
    (try (cons (+ (second stack) (first stack))
               (rest (rest stack)))
         (catch ArithmeticException e '(:overflow)))))

(defn sub [stack]
  (if (< (count stack) 2)
    stack
    (try (cons (- (second stack) (first stack))
               (rest (rest stack)))
         (catch ArithmeticException e '(:overflow)))))

(defn mult [stack]
  (if (< (count stack) 2)
    stack
    (try (cons (* (second stack) (first stack))
               (rest (rest stack)))
         (catch ArithmeticException e '(:overflow)))))

(defn div [stack]
  (if (or (< (count stack) 2)
          (zero? (first stack)))
    stack
    (try (cons (long (/ (second stack) (first stack)))
               (rest (rest stack)))
         (catch IllegalArgumentException e '(:overflow)))))

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
    (try (cons (long (math/expt (second stack) (first stack)))
               (rest (rest stack)))
         (catch IllegalArgumentException e '(:overflow)))))

(defn max- [stack]
"Pushes the maximum of the top two items onto the top of the stack."
  (if (< (count stack) 2)
    stack
    (cons (max (second stack) (first stack))
          (rest (rest stack)))))

(defn min- [stack]
"Pushes the minimum of the top two items onto the top of the stack."
  (if (< (count stack) 2)
    stack
    (cons (min (second stack) (first stack))
          (rest (rest stack)))))

(defn log_e [stack]
  (if (< (count stack) 1)
    stack
    (try (cons (long (Math/log (math/abs (first stack))))
               (rest stack))
         (catch IllegalArgumentException e '(:overflow)))))

(defn log_10 [stack]
  (if (< (count stack) 1)
    stack
    (try (cons (long (Math/log10 (math/abs (first stack))))
               (rest stack))
         (catch IllegalArgumentException e '(:overflow)))))

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
    (try (cons (long (math/lcm (second stack) (first stack)))
               (rest (rest stack)))
         (catch IllegalArgumentException e '(:overflow)))))

(defn sqrt [stack]
  (if (< (count stack) 1)
    stack
    (cons (long (Math/sqrt (first stack)))
          (rest stack))))

(defn cbrt [stack]
  (if (< (count stack) 1)
    stack
    (cons (long (Math/cbrt (first stack)))
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
    (try (cons (long (combo/count-permutations (range (first stack))))
               (rest stack))
         (catch IllegalArgumentException e '(:overflow)))))

(defn comb [stack]
  (if (< (count stack) 2)
    stack
    (try (cons (long (combo/count-combinations (range (second stack)) (first stack)))
               (rest (rest stack)))
         (catch IllegalArgumentException e '(:overflow)))))
