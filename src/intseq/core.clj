(ns intseq.core
  (:require [intseq.ops :as ops]
            [intseq.seqs :as seqs]
            [clojure.math.numeric-tower :as math]))

(defmacro time-process
  [& body]
  `(let [s# (new java.io.StringWriter)]
     (binding [*out* s#]
       (let [r# ~@body]
         (assoc r# :time-elapsed (.replaceAll (str s#) "[^0-9\\.]" ""))))))

(defn get-seq
  "Retrieves a sequence by its OEIS id, returning the terms as a list of coordinate pairs
  (test-pairs) in the form of (n, a(n)). The optional argument <keyword> subset allows for
  getting only the training terms (:training) or only the testing terms (:testing)."
  ([seq-id]
   (case seq-id
     :simple seqs/simple
     :A037270 seqs/A037270
     :A000292 seqs/A000292
     :A114241 seqs/A114241
     :A168392 seqs/A168392
     :A005132 seqs/A005132
     :A000040 seqs/A000040))
  ([seq-id subset]
   (get (seqs/split-sequence (get-seq seq-id)) subset)))

(defn evaluate [genome input output]
  "If the <output> argument is nil, returns the result obtained by evaluating the genome at a specific input.
  For a non-nil <output>, returns the absolute difference between the evaluated genome and the theoretical output,
  i.e. the error of the genome for a specific input-output pair."
  (loop [program genome
         stack ()]
    (if (= (first stack) :overflow)
      10000000
      (if (empty? program)
        (if (empty? stack)
          10000000
          (if (nil? output) (first stack) (math/abs (- output (first stack)))))
        (recur (rest program)
               (case (first program)
                 + (ops/add stack)
                 - (ops/sub stack)
                 * (ops/mult stack)
                 / (ops/div stack)
                 mod (ops/mod- stack)
                 expt (ops/expt stack)
                 max (ops/max- stack)
                 min (ops/min- stack)
                 log_e (ops/log_e stack)
                 log_10 (ops/log_10 stack)
                 abs (ops/abs stack)
                 gcd (ops/gcd stack)
                 lcm (ops/lcm stack)
                 sqrt (ops/sqrt stack)
                 cbrt (ops/cbrt stack)
                 sin (ops/sin stack)
                 cos (ops/cos stack)
                 tan (ops/tan stack)
                 perm (ops/perm stack)
                 comb (ops/comb stack)
                 x (cons input stack)
                 (cons (first program) stack)))))))

(defn convert [genome]
  "Converts a stack-based genome to a Clojure function, which is then returned."
  (fn [n]
    (evaluate genome n nil)))                               ;; no output, so nil

;; Specifies ingredients (mathematical operations) to use.
;; Note: not all possible operators are included such as expt, lcm, perm, comb.
;;       not including them right now, because they make the numbers way too big to compute.
(def ingredients '(+ - * / x -1 0 1))

(defn error [genome test-pairs]
  "Returns the error of genome in the context of test-pairs."
  (reduce + (for [pair test-pairs]
              (let [input (first pair)
                    output (second pair)]
                (evaluate genome input output)))))

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
    (map #(conj % {:case-error (evaluate (:genome %) input output)}) candidates)))

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

(defn tournament-selection [population tournament-size]
  "Returns an individual selected from the population using tournament selection."
  (best (repeatedly tournament-size #(rand-nth population))))

(defn select [population test-pairs selection-type tournament-size]
  "Returns an individual selected from population using specified selection method."
  (case selection-type
    :tournament-selection (tournament-selection population tournament-size)
    :lexicase-selection (lexicase-selection population test-pairs)))

(defn mutate [genome umad-add-rate umad-del-rate]
  "Returns a possibly-mutated copy of genome."
  (let [with-additions (flatten (for [g genome]
                                  (if (< (rand) umad-add-rate)
                                    (shuffle (list g (rand-nth ingredients)))
                                    g)))
        with-deletions (flatten (for [g with-additions]
                                  (if (< (rand) umad-del-rate)
                                    ()
                                    g)))]
    (vec with-deletions)))

(defn umad-crossover [genome1 genome2 umad-add-rate umad-del-rate]
  "Performs umad-crossover on two genomes and returns a new genome.
  NO MUTATION should be done after this operation; mutation argument in -main should be set to false."
  (let [with-additions (flatten (map (fn [g1 g2]
                                       (if (< (rand) umad-add-rate)
                                         (shuffle (list g1 g2))
                                         g1))
                                     genome1 genome2))
        with-deletions (flatten (for [g with-additions]
                                  (if (< (rand) umad-del-rate)
                                    ()
                                    g)))]
    (vec with-deletions)))

(defn single-point-crossover [genome1 genome2]
  "Performs single-point-crossover on two genomes and returns a new genome."
  (let [crossover-point (rand-int (inc (min (count genome1)
                                            (count genome2))))]
    (vec (concat (take crossover-point genome1)
                 (drop crossover-point genome2)))))

(defn uniform-crossover [genome1 genome2]
  "Performs uniform-crossover on two genomes and returns a new genome."
  (let [longer-genome (if (> (count genome1) (count genome2))
                        genome1
                        genome2)
        tail-genome-start-pos (- (count longer-genome)
                                 (Math/abs (- (count genome1) (count genome2))))]
    (vec (flatten (conj (vec (map (fn [g1 g2]
                                    (if (rand-nth [true false]) g1 g2))
                                  genome1
                                  genome2))
                        (vec (take (rand-nth (range (+ 1 (Math/abs (- (count genome1) (count genome2))))))
                                   (subvec longer-genome tail-genome-start-pos))))))))

(defn crossover [genome1 genome2 crossover-type umad-add-rate umad-del-rate]
  "Returns a one-point crossover product of genome1 and genome2"
  (case crossover-type
    :umad-crossover (umad-crossover genome1 genome2 umad-add-rate umad-del-rate)
    :single-point-crossover (single-point-crossover genome1 genome2)
    :uniform-crossover (uniform-crossover genome1 genome2)))

(defn make-child [population test-pairs selection-type tournament-size crossover? crossover-type mutate? umad-add-rate umad-del-rate]
  "Returns a new, evaluated child, produced by mutating the result
  of crossing over parents that are selected from the given population."
  (let [parent-genome1 (:genome (select population test-pairs selection-type tournament-size))
        parent-genome2 (:genome (select population test-pairs selection-type tournament-size))
        crossover-genome (crossover parent-genome1 parent-genome2 crossover-type umad-add-rate umad-del-rate)
        new-genome (if crossover?
                     (if mutate?
                       (mutate crossover-genome umad-add-rate umad-del-rate) ;; crossover and mutate
                       crossover-genome)                    ;; crossover but no don't mutate
                     (if mutate?
                       (mutate parent-genome1 umad-add-rate umad-del-rate) ;; don't crossover but mutate
                       parent-genome1))]                    ;; don't crossover and don't mutate
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

(defn gp [population-size generations test-pairs selection-type tournament-size crossover?
          crossover-type mutate? umad-add-rate umad-del-rate elitism? report?]
  "Runs genetic programming to find and return a function that perfectly fits the test-pairs data
  in the context of the given population-size and number of generations to run-simple."
  (loop [population (repeatedly population-size #(new-individual test-pairs))
         generation 0]
    (when report?
      (report generation population))
    (let [best-individual (best population)]
      (if (or (= (:error best-individual) 0)
              (>= generation generations))
        (do
          (if (= (:error best-individual) 0)
            (println "Found solution for training cases")
            (println "Found approximation for training cases"))
          {:genome       (:genome best-individual)
           :function     (convert (:genome best-individual))
           :generation   generation
           :best-error   (:error best-individual)
           :diversity    (float (/ (count (distinct population))
                                   (count population)))
           :average-size (float (/ (->> population
                                        (map :genome)
                                        (map count)
                                        (reduce +))
                                   (count population)))})
        (recur (if elitism?
                 (conj (repeatedly (dec population-size)
                                   #(make-child population test-pairs selection-type
                                                tournament-size crossover? crossover-type
                                                mutate? umad-add-rate umad-del-rate))
                       best-individual)
                 (repeatedly population-size
                             #(make-child population test-pairs selection-type
                                          tournament-size crossover? crossover-type
                                          mutate? umad-add-rate umad-del-rate)))
               (inc generation))))))

(defn -main [& args]
  "Input: <int> population-size,
          <int> generations,
          <keyword> seq-id: {:simple :A037270 :A000292 :A114241 :A168392 :A005132 :A000040},
          <keyword> selection-type: {:lexicase-selection :tournament-selection},
          <int> tournament-size, (will be ignored if using lexicase selection)
          <boolean> crossover?,
          <keyword> crossover-type: {:single-point-crossover :uniform-crossover :umad-crossover},
          <boolean> mutate?,
          <float> umad-add-rate: [0, 1.0],
          <float> umad-del-rate: [0, 1.0],
          <boolean> elitism?,
          <boolean> report?,
          <boolean> export-stats?

   Note: setting export-stats? to true will automatically make report? be false

   Example Input:
          lein run 200 200 :simple :lexicase-selection 0 true :single-point-crossover true 0.09 0.1 true true false"
  (let [population-size (read-string (nth args 0))
        generations (read-string (nth args 1))
        seq-id (read-string (nth args 2))
        selection-type (read-string (nth args 3))
        tournament-size (read-string (nth args 4))
        crossover? (read-string (nth args 5))
        crossover-type (read-string (nth args 6))
        mutate? (read-string (nth args 7))
        umad-add-rate (read-string (nth args 8))
        umad-del-rate (read-string (nth args 9))
        elitism? (read-string (nth args 10))
        report? (read-string (nth args 11))
        export-stats? (read-string (nth args 12))
        training-terms (get-seq seq-id :training)
        result (if export-stats?
                 (time-process (time (gp population-size generations training-terms selection-type tournament-size
                                         crossover? crossover-type mutate? umad-add-rate umad-del-rate elitism? false)))
                 (time (gp population-size generations training-terms selection-type tournament-size
                           crossover? crossover-type mutate? umad-add-rate umad-del-rate elitism? report?)))
        success? (and (= 0 (:best-error result))
                      (seqs/check (:function result) (get-seq seq-id)))]
    (if success?
      (println "Solution matches testing cases - Success!")
      (println "Solution approximate or does not match test cases - Failure!"))
    (if export-stats?
      (let [filename (str "data/" population-size "_" generations "_" (name seq-id) "_"
                          (name selection-type) "_" crossover? "_" (name crossover-type) "_"
                          mutate? "_" umad-add-rate "_" umad-del-rate "_" elitism? ".csv")
            stats-map (assoc (dissoc result :function) :success? success?)
            stats-csv (str (clojure.string/join ", " (vals stats-map)) "\n")]
        ;; first time we write to .csv, add in the column names
        (when (not (.exists (clojure.java.io/as-file filename)))
          (spit filename "genome, generations, best_error, diversity, avg_size, time_taken, found_solution\n"))
        (spit filename stats-csv :append true)))))
