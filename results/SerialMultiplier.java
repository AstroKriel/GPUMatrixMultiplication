package matrix.mulitiplcation;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class SerialMultiplier {
    // important variables
    private AtomicIntegerArray[] matrix_a;
    private AtomicIntegerArray[] matrix_b;
    private AtomicIntegerArray[] matrix_product;
    private int matrix_size;

    public SerialMultiplier(int matrix_size, 
            final AtomicIntegerArray[] matrix_a, 
            final AtomicIntegerArray[] matrix_b,
            final AtomicIntegerArray[] matrix_product) {
        // matrix sizes : assuming all matrices[matrix_size][matrix_size]
        this.matrix_size = matrix_size;
        // the two matrices to be multiplied together
        this.matrix_a = matrix_a;
        this.matrix_b = matrix_b;
        // the product matrix to be determined
        this.matrix_product = matrix_product;
    }

    public void MultiplyMatrices() {
        // for each row of matrix A
        for (int row = 0; row < matrix_size; row++) {
            // for each column of matrix B
            for (int col = 0; col < matrix_size; col++) {
                // initialise inner product
                int tmp_product = 0;
                // evaluate the dot product of the vectors A[row, :] and B[:, column]
                for (int elem = 0; elem < matrix_size; elem++) {
                    tmp_product = tmp_product + (matrix_a[row].get(elem) * matrix_b[elem].get(col));
                }
                // assign the tmp_product to the product matrix
                matrix_product[row].set(col, tmp_product);
            }
        }
    }

    public AtomicIntegerArray[] getProduct() {
        return matrix_product;
    }
}
