# intseq

A Clojure library using genetic programming techniques to find formulas for given sequences.


## Motivation
After browsing the [On-Line Encyclopedia of Integer Sequences (OEIS)](https://oeis.org/), we noticed that many of the sequences listed in the database were without formulas. Given the nature of Genetic Programming (GP) to discover solutions to problems that humans do not know how to solve directly, we knew there was an opportunity to create a program that takes advantage of GP in order to solve the problem of finding a closed-form formula for the infamous [Recaman's sequence](https://en.wikipedia.org/wiki/Recam%C3%A1n%27s_sequence). IntSeq attempts to find a formula that holds true for the first n integers in the sequence. Our program also includes testing the resulting formulas for accuracy by seeing whether they hold true for numbers n+1 and beyond. We run our program on five test sequences to determine whether single-point crossover with UMAD or UMAD without crossover performs better for finding a formula to a sequence. We selected the mathematically-significant and aesthetically-appealing Recaman's sequence because while many programs exist to calculate the terms of the sequence, none attempt to find a closed-form formula to describe the sequence.

## What We Did
Built with a minimalist stack-based genetic programming approach in mind, IntSeq is composed of three namespaces. The first namespace, `seqs`, is a collection of all the sequences we used as well as some helper functions to split the sequences into training and testing subsequences. Our sequences are divided into two categories: test sequences and mathematically significant sequences. Test sequences are sequences with existing formulas already. Among the test sequences include [A037270](https://oeis.org/A037270), [A000292](https://oeis.org/A000292), [A114241](https://oeis.org/A114241), [A168392](https://oeis.org/A168392), and a `simple` sequence we created ourselves based on the mathematical expression: x^2 + x + 4. These test sequences were used to compare the performance difference between UMAD with single point crossover and UMAD without crossover. On the other hand, our mathematically significant sequence, [Recman's Sequence](https://oeis.org/A005132), was a sequence which had no closed-form formula; our goal was to find such a formula for this sequence.

The second namespace, `ops`, includes implementations of different integer operators that could be used in our `ingredients` list. These operators included addition, subtraction, multiplication, floor division, max, min, and various other integer operators.

Our final namespace is `core`; this is where our code for evolution is located. `core` maintains a population of digital genomes, implements various selection, crossover and mutation types, and ultimately runs genetic programming to find and return a function that perfectly fits a sequence with a given population-size and number of generations. Our selection methods include lexicase and tournament selection. We implement various crossover methods including single point crossover, uniform crossover, and what we refer to as UMAD crossover. UMAD crossover is the type of crossover used in the paper ["*Program synthesis using uniform mutation by addition and deletion*"](https://dl.acm.org/doi/10.1145/3205455.3205603). Unliked other types of crossover we implemented, UMAD crossover works by adding elements from one genome into another genome, and then deleting certain elements from the resulting genome.

 In addition to these three namespaces, we set up our repository to run on the Condor computing cluster. As a result, we executed our runs on the cluster in hopes of finding a closed-form formula for Recaman's Sequence as well as testing whether UMAD with single point crossover performs better than UMAD without crossover. We investigated both scenarios in 100 independent runs each, for all of our sequences. We fixed all other parameters to reduce variability and picked conventionally-optimal parameters used in research. As such, the following are our fixed parameters:
 1. population size: 1000
 2. generation cap: 300
 3. selection type: lexicase selection
 4. mutation: UMAD
 5. UMAD addition rate: 0.09
 6. UMAD deletion rate: 0.1
 7. elitism

## Results

TODO
