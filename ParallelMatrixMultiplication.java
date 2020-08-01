package parallel;

import java.util.concurrent.*;

public class ParallelMatrixMultiplication {

	private double[][] a;
	private double[][] b;
	private double[][] c;
	private int matrix_size;
	private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors(), MINIMUM_THRESHOLD = 64;

	private final ExecutorService exec = Executors.newFixedThreadPool(POOL_SIZE);

	// constructor saves matrices and their size
	ParallelMatrixMultiplication(double[][] a, double[][] b, int matrix_size) {
		// assumption: matrices a and b are both double[matrix_size][matrix_size]
		this.matrix_size = matrix_size;
		this.a = a;
		this.b = b;
		this.c = new double[matrix_size][matrix_size];
	}

	// execute the matrix multiplication
	public void multiply() {
		Future f = exec.submit(new MultiplyTask(a, b, c, 0, 0, 0, 0, 0, 0, a.length));
		try {
			f.get();
			exec.shutdown();
		} catch (Exception e) {
		}
	}

	// return the result matrix (c = a * b)
	public double[][] getResult() {
		return c;
	}

	class MultiplyTask implements Runnable {
		private double[][] a;
		private double[][] b;
		private double[][] c;
		private int a_i, a_j, b_i, b_j, c_i, c_j, size;

		MultiplyTask(double[][] a, double[][] b, double[][] c, int a_i, int a_j, int b_i, int b_j, int c_i, int c_j, int size) {
			this.a = a;
			this.b = b;
			this.c = c;
			this.a_i = a_i;
			this.a_j = a_j;
			this.b_i = b_i;
			this.b_j = b_j;
			this.c_i = c_i;
			this.c_j = c_j;
			this.size = size;
		}

		public void run() {
			// System.out.format("[%d,%d]x[%d,%d](%d)\n", a_i, a_j, b_i, b_j, size);
			int h = size/2;
			if (size <= MINIMUM_THRESHOLD) {
				for (int i = 0; i < size; ++i) {
					for (int j = 0; j < size; ++j) {
						for (int k = 0; k < size; ++k) {
							c[c_i+i][c_j+j] += a[a_i+i][a_j+k] * b[b_i+k][b_j+j];
						}
					}
				}
			} else {
				MultiplyTask[] tasks = {
					new MultiplyTask(a, b, c, a_i, a_j, b_i, b_j, c_i, c_j, h),
					new MultiplyTask(a, b, c, a_i, a_j+h, b_i+h, b_j, c_i, c_j, h),
					new MultiplyTask(a, b, c, a_i, a_j, b_i, b_j+h, c_i, c_j+h, h),
					new MultiplyTask(a, b, c, a_i, a_j+h, b_i+h, b_j+h, c_i, c_j+h, h),
					new MultiplyTask(a, b, c, a_i+h, a_j, b_i, b_j, c_i+h, c_j, h),
					new MultiplyTask(a, b, c, a_i+h, a_j+h, b_i+h, b_j, c_i+h, c_j, h),
					new MultiplyTask(a, b, c, a_i+h, a_j, b_i, b_j+h, c_i+h, c_j+h, h),
					new MultiplyTask(a, b, c, a_i+h, a_j+h, b_i+h, b_j+h, c_i+h, c_j+h, h)
				};

				FutureTask[] fs = new FutureTask[tasks.length/2];

				for (int i = 0; i < tasks.length; i+=2) {
					fs[i/2] = new FutureTask(new Sequentializer(tasks[i], tasks[i+1]), null);
					exec.execute(fs[i/2]);
				}
				for (int i = 0; i < fs.length; ++i) {
					fs[i].run();
				}
				try {
					for (int i = 0; i < fs.length; ++i) {
						fs[i].get();
					}
				} catch (Exception e) {
				}
			}
		}
	}

	class Sequentializer implements Runnable{
		private MultiplyTask first, second;
		Sequentializer(MultiplyTask first, MultiplyTask second) {
			this.first = first;
			this.second = second;
		}
		public void run() {
			first.run();
			second.run();
		}
	}

}