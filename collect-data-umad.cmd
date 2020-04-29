## Global job properties
universe     = vanilla
notify_user  = mgirjau21@amherst.edu
initialdir   = /mnt/scratch/mgirjau21/intseq
getenv       = True
executable   = run

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
queue 1

arguments = 1000 300 :A168392 :lexicase-selection 0 false :single-point-crossover true 0.09 0.1 true false true
queue 1

arguments = 1000 300 :A168392 :lexicase-selection 0 true :single-point-crossover true 0.09 0.1 true false true
queue 1
