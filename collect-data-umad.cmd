## Global job properties
universe     = vanilla
notify_user  = mgirjau21@amherst.edu
initialdir   = /mnt/scratch/mgirjau21/intseq
getenv       = True
executable   = run

:: This file only runs two main cases of interest - UMAD without crossover, and UMAD with single-point crossover
:: All other parameters are set at the commonly-accepted "optimal" values used in research

## Job properties
arguments = 200 200 :simple :lexicase-selection 0 true :uniform-crossover true 0.09 0.1 true false true
queue 100

arguments = 200 200 :simple :lexicase-selection 0 true :single-point-crossover true 0.09 0.1 true false true
queue 100
