(ns intseq.core
  (:require [intseq.ops :as ops]
            [clojure.math.numeric-tower :as math]))

(defn get-seq []
  "Retrieves sequence has an OEIS id of seq-id.
  Returns a list of coordinate pairs (test-pairs) in the form of (n, a(n)).
  Note: Retrieval can be done through Mathematica (specified expression or OEIS query)
        or HTTP request."
  (for [x (range 0 10 1)]
    [x (+ (* x x) (+ 2 x) 2)]))

(def ingredients '(+ - * / mod abs gcd sqrt sin cos tan x 0 1))
;; Specifies ingredients (mathematical operations) to use.
;; Note: not all possible operators are included such as expt, lcm, perm, comb.
;;        not including them right now, because they make the numbers way too big to compute.

(defn error-loop [genome input output]
  "Returns the error of the genome for given input output pair."
  (loop [program genome
         stack ()]
    (if (empty? program)
      (if (empty? stack)
        1000000
        (math/abs (- output (first stack))))
      (recur (rest program)
             (case (first program)
               + (ops/add stack)
               - (ops/sub stack)
               * (ops/mult stack)
               / (ops/div stack)
               mod (ops/mod- stack)
               expt (ops/expt stack)
               abs (ops/abs stack)
               gcd (ops/gcd stack)
               lcm (ops/lcm stack)
               sqrt (ops/sqrt stack)
               sin (ops/sin stack)
               cos (ops/cos stack)
               tan (ops/tan stack)
               perm (ops/perm stack)
               comb (ops/comb stack)
               x (cons input stack)
               (cons (first program) stack))))))

(defn error [genome test-pairs]
  "Returns the error of genome in the context of test-pairs."
  (reduce + (for [pair test-pairs]
              (let [input (first pair)
                    output (second pair)]
                (error-loop genome input output)))))

(defn new-individual [test-pairs]
  "Returns a new, random individual in the context of test-pairs."
  (let [genome (vec (repeatedly 5 #(rand-nth ingredients)))]
    {:genome genome
     :error  (error genome test-pairs)}))

(defn best [individuals]
  "Returns the best of the given individuals."
  (reduce (fn [i1 i2]
            (if (< (:error i1) (:error i2))
              i1
              i2))
          individuals))

(defn add-case-error [candidates test-pair]
  "Returns candidates with their corresponding case errors."
  (let [input (first test-pair)
        output (second test-pair)]
    (map #(conj % {:case-error (error-loop (:genome %) input output)}) candidates)))

(defn lexicase-selection [population test-pairs]
  "Returns an individual from the population using lexicase selection."
  (loop [candidates (distinct population)
         cases (shuffle test-pairs)]
    (if (or (empty? cases)
            (empty? (rest candidates)))
      (rand-nth candidates)
      (let [candidates-w-case-error (add-case-error candidates (first cases))
            min-error (apply min (map :case-error candidates-w-case-error))]
        (recur (filter #(= min-error (:case-error %)) candidates-w-case-error)
               (rest cases))))))

(defn tournament-selection [population]
  "Returns an individual selected from the population using tournament selection."
  (best (repeatedly 2 #(rand-nth population))))

(defn select [population test-pairs select-type]
  "Returns an individual selected from population using specified selection method."
  (case select-type
    :tournament-selection (tournament-selection population)
    :lexicase-selection (lexicase-selection population test-pairs)))

(defn mutate [genome]
  "Returns a possibly-mutated copy of genome."
  (let [with-additions (flatten (for [g genome]
                                  (if (< (rand) 1/20)
                                    (shuffle (list g (rand-nth ingredients)))
                                    g)))
        with-deletions (flatten (for [g with-additions]
                                  (if (< (rand) 1/11)
                                    ()
                                    g)))]
    (vec with-deletions)))

(defn crossover [genome1 genome2]
  "Returns a one-point crossover product of genome1 and genome2"
  (let [crossover-point (rand-int (inc (min (count genome1)
                                            (count genome2))))]
    (vec (concat (take crossover-point genome1)
                 (drop crossover-point genome2)))))

(defn make-child [population test-pairs select-type crossover?]
  "Returns a new, evaluated child, produced by mutating the result
  of crossing over parents that are selected from the given population."
  (let [new-genome (mutate (if crossover? (crossover (:genome (select population test-pairs select-type))
                                                      (:genome (select population test-pairs select-type)))
                                          (:genome (select population test-pairs select-type))))]
    {:genome new-genome
     :error  (error new-genome test-pairs)}))

(defn report [generation population]
  "Prints a report on the status of the population at the given generation."
  (let [current-best (best population)]
    (println {:generation   generation
              :best-error   (:error current-best)
              :diversity    (float (/ (count (distinct population))
                                      (count population)))
              :average-size (float (/ (->> population
                                           (map :genome)
                                           (map count)
                                           (reduce +))
                                      (count population)))
              :best-genome  (:genome current-best)})))

(defn gp [population-size generations test-pairs select-type crossover?]
  "Runs genetic programming to find a function that perfectly fits the test-pairs data
  in the context of the given population-size and number of generations to run."
  (loop [population (repeatedly population-size
                                #(new-individual test-pairs))
         generation 0]
    (report generation population)
    (if (or (= (:error (best population)) 0)
            (>= generation generations))
      (best population)
      (recur (repeatedly population-size
                         #(make-child population test-pairs select-type crossover?))
             (inc generation)))))


#_(gp 200 100 (get-seq) :lexicase-selection true)