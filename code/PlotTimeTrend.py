import numpy as np
import matplotlib.pyplot as plt

from matplotlibrc import *
rcParams['figure.figsize'] = (6.0, 3.75) # change the figure size

## parallel by implementing runnable
n_times_parallel_runnable = [[0.00900, 0.00360, 0.00360, 0.01740] ,
                    [0.03740, 0.02740, 0.00720, 0.01800] ,
                    [0.03840, 0.01680, 0.04120, 0.01480] ,
                    [0.05820, 0.05660, 0.03340, 0.03680] ,
                    [0.05040, 0.04560, 0.07060, 0.05500] ,
                    [0.12680, 0.09860, 0.08660, 0.12980] ,
                    [0.19740, 0.19500, 0.17000, 0.16100] ,
                    [0.25280, 0.25440, 0.25240, 0.24000] ,
                    [0.29760, 0.38060, 0.25260, 0.36360]]

## parallel by extending thread
n_times_parallel_thread = [[0.01900, 0.02700, 0.01500, 0.02200] ,
                    [0.09300, 0.04000, 0.07700, 0.05300] ,
                    [0.07000, 0.06700, 0.07600, 0.09800] ,
                    [0.11700, 0.09300, 0.15300, 0.20300] ,
                    [0.20100, 0.31400, 0.18800, 0.25000] ,
                    [0.49600, 0.42000, 0.48200, 0.44700] ,
                    [1.07400, 1.02500, 1.04800, 0.98000] ,
                    [2.02000, 1.75600, 1.86800, 1.86200] ,
                    [2.50000, 2.59800, 2.63100, 2.49800]]

## serial
n_times_serial = [  [0.00360, 0.00420, 0.00400, 0.00440] ,
                    [0.00640, 0.00620, 0.00600, 0.00620] ,
                    [0.01120, 0.01180, 0.01160, 0.01260] ,
                    [0.02780, 0.02720, 0.02880, 0.03300] ,
                    [0.12060, 0.08860, 0.09100, 0.10240] ,
                    [0.43160, 0.42900, 0.43160, 0.43240] ,
                    [1.53200, 1.53460, 1.60460, 1.56540] ,
                    [3.21260, 3.22380, 3.47400, 3.77820] ,
                    [5.69560, 5.16840, 6.04680, 5.55100]]

n_size = [50, 100, 150, 200, 300, 500, 700, 900, 1000]

## calculate average compute time, as well as the standard deviation
## parallel by implementing runnable
time_std_parallel_runnable = np.std(n_times_parallel_runnable, axis=1)
time_ave_parallel_runnable = np.mean(n_times_parallel_runnable, axis=1)
## parallel by extending thread
time_std_parallel_thread = np.std(n_times_parallel_thread, axis=1)
time_ave_parallel_thread = np.mean(n_times_parallel_thread, axis=1)
## serial
time_std_serial   = np.std(n_times_serial, axis=1)
time_ave_serial   = np.mean(n_times_serial, axis=1)

## use dark theme
plt.style.use('dark_background')
## parallel by implementing runnable
plt.errorbar(n_size, time_ave_parallel_runnable, yerr=time_std_parallel_runnable, 
    label='Parallel Computation (Implement Runnable)')
## parallel by extending thread
plt.errorbar(n_size, time_ave_parallel_thread, yerr=time_std_parallel_thread, 
    label='Parallel Computation (Extend Thread)')
## serial
plt.errorbar(n_size, time_ave_serial, yerr=time_std_serial, 
    label='Serial Computation')
## label figure
plt.xlabel('Number of Elements')
plt.ylabel('Average Compute Time [s]')
## add legend
plt.legend(loc='upper left')
## show figure
plt.show()
