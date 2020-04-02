(ns intseq.ops)

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
    (cons (Math/floorDiv (second stack) (first stack))
          (rest (rest stack)))))