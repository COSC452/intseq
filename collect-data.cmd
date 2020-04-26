## Global job properties
universe     = vanilla
notify_user  = mgirjau21@amherst.edu
initialdir   = /mnt/scratch/mgirjau21/intseq
getenv       = True
executable   = run

## Job properties
arguments = 200 200 :simple :lexicase-selection true :uniform-crossover true 0.09 0.1 true false true
queue 100

arguments = 200 200 :simple :lexicase-selection true :single-point-crossover true 0.09 0.1 true false true
queue 100

arguments = 200 200 :simple :lexicase-selection true :uniform-crossover true 0.15 0.1 true false true
queue 100
