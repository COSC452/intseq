# intseq

A Clojure library using genetic programming techniques to find formulas for given sequences.


## Motivation
After browsing the [On-Line Encyclopedia of Integer Sequences (OEIS)](https://oeis.org/), we noticed that many of the sequences listed in the database were without formulas. Given the nature of Genetic Programming (GP) to discover solutions to problems that humans do not know how to solve directly, we knew there was an opportunity to create a program that takes advantage of GP in order to solve the problem of finding a closed-form formula for the infamous [Recáman's sequence](https://en.wikipedia.org/wiki/Recam%C3%A1n%27s_sequence). IntSeq attempts to find a formula that holds true for the first n integers in the sequence. Our program also includes testing the formulas for accuracy by seeing whether they hold true for numbers n+1 and beyond. We selected the mathematically-significant and aesthetically-appealing Recáman's sequence because while many programs exist to calculate the terms of the sequence, none attempt to find a closed-form formula to describe the sequence.

## What We Did
Built with a minimalist stack-based genetic programming approach in mind, IntSeq is composed of three main components: The first is the "core" which maintains a population of digital genomes, implements various selection, crossover and mutation operators, and ultimately runs genetic programming to find and return a function that perfectly fits the test-pairs data with a given population-size and number of generations. Our selection methods include Lexicase and Tournament selection. We implement various crossover methods including single-point crossover, uniform crossover, and [Uniform Mutation by Addition and Deletion (UMAD)](https://dl.acm.org/doi/10.1145/3205455.3205603) crossover. The second component, the "ops", involves the implemementation of different integer operations. The third component is a collection of analysis and statistics tools, including a test environment of four sequences with existing formulas ([A037270](https://oeis.org/A037270), [A000292](https://oeis.org/A000292), [A114241](https://oeis.org/A114241), and [A168392](https://oeis.org/A168392)) in order to compare the perfomance difference between single-point crossover with UMAD and UMAD without crossover. We chose one sequence without a function (Recáman's sequence) in hopes of finding a formula to describe it. In addition to these three primary components, we have set up our repository to run on the Condor computing cluster. As a result, we executed our runs on the cluster.

## Results

FIXME

## License

Copyright © 2020 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
