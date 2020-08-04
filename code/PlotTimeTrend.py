import numpy as np
import matplotlib.pyplot as plt

from matplotlibrc import *
rcParams['figure.figsize'] = (6.0, 3.75) # change the figure size

n_times_parallel = [[0.01900, 0.02700, 0.01500, 0.02200] ,
                    [0.09300, 0.04000, 0.07700, 0.05300] ,
                    [0.07000, 0.06700, 0.07600, 0.09800] ,
                    [0.11700, 0.09300, 0.15300, 0.20300] ,
                    [0.20100, 0.31400, 0.18800, 0.25000] ,
                    [0.49600, 0.42000, 0.48200, 0.44700] ,
                    [1.07400, 1.02500, 1.04800, 0.98000] ,
                    [2.02000, 1.75600, 1.86800, 1.86200] ,
                    [2.50000, 2.59800, 2.63100, 2.49800]]

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

time_std_parallel = np.std(n_times_parallel, axis=1)
time_ave_parallel = np.mean(n_times_parallel, axis=1)
time_std_serial   = np.std(n_times_serial, axis=1)
time_ave_serial   = np.mean(n_times_serial, axis=1)

plt.style.use('dark_background')
plt.errorbar(n_size, time_ave_parallel, yerr=time_std_parallel, label='Parallel Computation')
plt.errorbar(n_size, time_ave_serial, yerr=time_std_serial, label='Serial Computation')
plt.xlabel('Number of Elements')
plt.ylabel('Average Compute Time [s]')
plt.legend(loc='upper left')

plt.show()
