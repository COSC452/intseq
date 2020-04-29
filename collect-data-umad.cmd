## Global job properties
universe     = vanilla
notify_user  = mgirjau21@amherst.edu
initialdir   = /mnt/scratch/mgirjau21/intseq
getenv       = True
executable   = run

:: This file only runs two main cases of interest - UMAD without crossover, and UMAD with single-point crossover
::
:: All other parameters are set at commonly-accepted "optimal" values used in research, and based on Prof. Spector's
:: paper on "Program Synthesis using Uniform Mutation by Addition and Deletion"
::
:: USING: population-size = 1000
::        generations = 300
::        seq-id = (:simple, :A037270, :A000292, :A114241, :A168392)
::        selection-type = :lexicase-selection
::        tournament-size = 0 (IGNORED)
::     *  crossover? = true / false
::     *  crossover-type :single-point-crossover (IGNORED when crossover? is false)
::        mutate? = true
::        umad-add-rate = 0.09
::        umad-del-rate = 0.1 (shrinking UMAD exhibits least variability)
::        elitism? = true
::        report? = false
::        export-stats? = true

## Job properties
arguments = 1000 300 :simple :lexicase-selection 0 false :single-point-crossover true 0.09 0.1 true false true
queue 100

arguments = 1000 300 :simple :lexicase-selection 0 true :single-point-crossover true 0.09 0.1 true false true
queue 100

arguments = 1000 300 :A037270 :lexicase-selection 0 false :single-point-crossover true 0.09 0.1 true false true
queue 100

arguments = 1000 300 :A037270 :lexicase-selection 0 true :single-point-crossover true 0.09 0.1 true false true
queue 100

arguments = 1000 300 :A000292 :lexicase-selection 0 false :single-point-crossover true 0.09 0.1 true false true
queue 100

arguments = 1000 300 :A000292 :lexicase-selection 0 true :single-point-crossover true 0.09 0.1 true false true
queue 100

arguments = 1000 300 :A114241 :lexicase-selection 0 false :single-point-crossover true 0.09 0.1 true false true
queue 100

arguments = 1000 300 :A114241 :lexicase-selection 0 true :single-point-crossover true 0.09 0.1 true false true
queue 100

arguments = 1000 300 :A168392 :lexicase-selection 0 false :single-point-crossover true 0.09 0.1 true false true
queue 100

arguments = 1000 300 :A168392 :lexicase-selection 0 true :single-point-crossover true 0.09 0.1 true false true
queue 100
