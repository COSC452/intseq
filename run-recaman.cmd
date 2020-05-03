## Global job properties
universe     = vanilla
notify_user  = mgirjau21@amherst.edu
initialdir   = /mnt/scratch/mgirjau21/intseq
getenv       = True
executable   = run

## Job properties
arguments = 1000 300 :A005132 :lexicase-selection 0 false :single-point-crossover true 0.09 0.1 true false true
queue 50

arguments = 1000 300 :A005132 :lexicase-selection 0 true :single-point-crossover true 0.09 0.1 true false true
queue 50
