public class SerialMatrixMultiplication {
    // global variables
    private int matrix_size;
    private double[][] a;
	private double[][] b;
    private double[][] c;

    // constructor saves matrices and their size
	SerialMatrixMultiplication(double[][] a, double[][] b, int matrix_size) {
        // assumption: matrices a and b are both double[matrix_size][matrix_size]
		this.matrix_size = matrix_size;
		this.a = a;
		this.b = b;
		this.c = new double[matrix_size][matrix_size];
    }

    // multiply matrix in seriel
    public void multiplyMatrices() {
        for (int i = 0; i < matrix_size; ++i) {
			for (int j = 0; j < matrix_size; ++j) {
				for (int k = 0; k < matrix_size; ++k) {
					c[i][j] += a[i][k] * b[k][j];
				}
			}
		}
    }

    // return the result of matrix multiplication: c = a * b
	public double[][] getResult() {
		return c;
    }
}