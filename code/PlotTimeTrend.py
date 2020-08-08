import numpy as np
import matplotlib.pyplot as plt

from matplotlibrc import *
rcParams['figure.figsize'] = (7.0, 3.75) # change the figure size

## parallel by implementing runnable
n_times_parallel = [[0.00597, 0.00595, 0.00597, 0.00598] ,
                    [0.01312, 0.01288, 0.01326, 0.01524] ,
                    [0.01936, 0.01992, 0.02288, 0.01975] ,
                    [0.02956, 0.02790, 0.02771, 0.02938] ,
                    [0.09018, 0.08434, 0.09046, 0.09655] ,
                    [0.23237, 0.24829, 0.27467, 0.24137] ,
                    [0.59173, 0.61434, 0.59328, 0.59998] ,
                    [1.46291, 1.46840, 1.40300, 1.55797] ,
                    [2.34040, 2.31839, 2.28235, 2.06830]]

## serial
n_times_serial = [  [0.00728, 0.00373, 0.00795, 0.01214] ,
                    [0.02097, 0.01156, 0.01084, 0.02805] ,
                    [0.02026, 0.01566, 0.01947, 0.01621] ,
                    [0.03079, 0.03668, 0.04123, 0.05489] ,
                    [0.08723, 0.08964, 0.09017, 0.16562] ,
                    [0.49359, 0.49512, 0.48956, 0.59313] ,
                    [1.25222, 1.83438, 1.48103, 1.68395] ,
                    [3.08529, 3.34064, 3.08639, 3.17783] ,
                    [4.88401, 5.05147, 5.20713, 5.06619]]

n_size = [50, 100, 150, 200, 300, 500, 700, 900, 1000]

## calculate average compute time, as well as the standard deviation
## parallel by implementing runnable
time_std_parallel = np.std(n_times_parallel, axis=1)
time_ave_parallel = np.mean(n_times_parallel, axis=1)
## serial
time_std_serial   = np.std(n_times_serial, axis=1)
time_ave_serial   = np.mean(n_times_serial, axis=1)

## use dark theme
plt.style.use('dark_background')
## parallel by implementing runnable
plt.errorbar(n_size, time_ave_parallel, yerr=time_std_parallel, label='Parallel Computation')
## serial
plt.errorbar(n_size, time_ave_serial, yerr=time_std_serial, label='Serial Computation')
## label figure
plt.xlabel('Matrix size')
plt.ylabel('Average Compute Time [s]')
## add legend
plt.legend(loc='upper left')
## show figure
plt.show()
