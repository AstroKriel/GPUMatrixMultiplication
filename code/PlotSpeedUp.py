import numpy as np
import matplotlib.pyplot as plt

from matplotlibrc import *
rcParams['figure.figsize'] = (7.0, 3.75) # change the figure size

## parallel by implementing runnable
n_times_parallel = [[9.17925, 8.89485, 8.89089, 9.39462] ,
                    [4.86028, 4.36663, 4.49427, 4.31107] ,
                    [2.79622, 2.76447, 2.79418, 2.79559] ,
                    [2.37145, 2.26056, 2.24067, 2.42319] ,
                    [2.27929, 2.09533, 1.90053, 2.17420] ,
                    [2.43642, 2.37358, 2.17872, 2.23339] ,
                    [2.26717, 2.40103, 2.43798, 2.16603] ,
                    [2.49240, 2.36265, 2.15504, 2.75178] ,
                    [2.46495, 2.60506, 2.53880, 2.44803] ,
                    [2.50243, 2.52491, 2.53364, 2.48524]]

## serial
n_times_serial = [5.20698, 4.97030, 4.96130, 5.02992]

n_size = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

## calculate average compute time, as well as the standard deviation
n_speedup = [[(tmp_serial_val / tmp_parallel_val) for tmp_serial_val, tmp_parallel_val in zip(n_times_serial, tmp_parallel_array)] for tmp_parallel_array in n_times_parallel]
speedup_ave = np.mean(n_speedup, axis=1)
speedup_std = np.std(n_speedup, axis=1)

## use dark theme
plt.style.use('dark_background')
## parallel by implementing runnable
plt.errorbar(n_size, speedup_ave, yerr=speedup_std)
## label figure
plt.xlabel(r'Number of Processors')
plt.ylabel(r'Parallel Speedup')
plt.title(r'Matrix Multiplication (size $= 1000 \times 1000$)')
## show figure
plt.show()
