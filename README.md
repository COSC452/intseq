# intseq

A Clojure library using genetic programming techniques to find formulas for given sequences.


## Motivation
After browsing the [On-Line Encyclopedia of Integer Sequences (OEIS)](https://oeis.org/), we noticed that many of the sequences listed in the database were without formulas. Given the nature of Genetic Programming (GP) to discover solutions to problems that humans do not know how to solve directly, we knew there was an opportunity to create a program that takes advantage of GP to solve the problem of finding a closed-form formula for the infamous [Recaman's sequence](https://en.wikipedia.org/wiki/Recam%C3%A1n%27s_sequence). IntSeq attempts to find a formula that holds for the first n integers in the sequence. Our program also includes testing the resulting formulas for accuracy by seeing whether they hold for numbers n+1 and beyond. We run our program on five test sequences to determine whether single-point crossover with UMAD or UMAD without crossover performs better for finding a formula for a sequence. We selected the mathematically-significant and aesthetically-appealing Recaman's sequence because while many programs exist to calculate the terms of the sequence, none attempt to find a closed-form formula to describe the sequence.

## What We Did
Built with a minimalist stack-based genetic programming approach in mind, IntSeq is composed of three namespaces. The first namespace, `seqs`, is a collection of all the sequences we used as well as some helper functions to split the sequences into training and testing subsequences. Our sequences are divided into two categories: test sequences and mathematically significant sequences. Test sequences are sequences with existing formulas already. Among the test sequences include [A037270](https://oeis.org/A037270), [A000292](https://oeis.org/A000292), [A114241](https://oeis.org/A114241), [A168392](https://oeis.org/A168392), and a `simple` sequence we created ourselves based on the mathematical expression: x^2 + x + 4. These test sequences were used to compare the performance difference between UMAD with single-point crossover and UMAD without crossover. On the other hand, our mathematically significant sequence, [Recman's Sequence](https://oeis.org/A005132), was a sequence that had no closed-form formula; our goal was to find such a formula for this sequence.

The second namespace, `ops`, includes implementations of different integer operators that could be used in our `ingredients` list. These operators included addition, subtraction, multiplication, floor division, max, min, and various other integer operators.

Our final namespace is `core`; this is where our code for evolution is located. `core` maintains a population of digital genomes, implements various selection, crossover, and mutation types, and ultimately runs genetic programming to find and return a function that perfectly fits a sequence with a given population size and a number of generations. Our selection methods include lexicase and tournament selection. We implement various crossover methods including single-point crossover, uniform crossover, and what we refer to as UMAD crossover. UMAD crossover is the type of crossover used in the paper ["*Program synthesis using uniform mutation by addition and deletion*"](https://dl.acm.org/doi/10.1145/3205455.3205603). Unlike other types of crossover we implemented, UMAD crossover works by adding elements from one genome into another genome and then deleting certain elements from the resulting genome.

 In addition to these three namespaces, we set up our repository to run on the Condor computing cluster. As a result, we executed our runs on the cluster in hopes of finding a closed-form formula for Recaman's Sequence as well as testing whether UMAD with single-point crossover performs better than UMAD without crossover. We investigated both scenarios in 100 independent runs each, for all of our sequences. We fixed all other parameters to reduce variability and picked conventionally-optimal parameters used in research. As such, the following are our fixed parameters:
 1. population size: 1000
 2. generation cap: 300
 3. selection type: lexicase selection
 4. mutation: UMAD
 5. UMAD addition rate: 0.09
 6. UMAD deletion rate: 0.1
 7. elitism

## Results

We began with a hypothesis that UMAD with single-point crossover would perform better than UMAD with no crossover. After testing out our hypothesis with 100 independent runs for all 5 test sequences for a total of 1000 runs on the Condor Computing Cluster, here are our results:

**Our program found solutions for 3 out of the 5 test sequences (simple, A037270, A168392).**

Overall we saw **no significant differences between the two**, but UMAD with crossover was more successful for more difficult sequences (e.g. A168392, p = 0.02), and faster for simpler sequences (e.g. simple or A037270, p < 0.001). In addition we noticed that UMAD with crossover tends to increase the average size of genomes across the board (p ≪ 0.001).

### Recamán Sequence
Using the same parameters as previously described, we ran our program with both single-point crossover and no crossover. As the Recamán sequence is more complex, we expected better results (e.g. average error) from UMAD with crossover. Indeed, we found that (with p ≪ 0.001):
* UMAD with crossover produces a mean error of 18.74 (range 14-20)
* UMAD without crossover produces a mean error of 20.54 (range 17-23)

The algorithm did not ultimately find a closed form. More experimentation and code improvement might be necessary for a mathematical breakthrough.

Possible directions for future work include the Implementation, usage, and optimization of more mathematical operators, identification of “sequence subtypes” and running individual analyses, further optimization of parameters, increased number of test runs and data collection and the incorporation of simplification algorithms for the genomes.

