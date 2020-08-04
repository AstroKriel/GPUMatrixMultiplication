package matrix.mulitiplcation;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class ParallelMultiplier extends Thread {
    // important variables
    private int row;
    private int col;
    private int matrix_size;
    private AtomicIntegerArray matrix_a;
    private AtomicIntegerArray[] matrix_b;
    private AtomicIntegerArray[] matrix_product;

    // store the matrices and where the matrix product is up to
    public ParallelMultiplier(final int row, final int col, final int matrix_size,
            final AtomicIntegerArray matrix_a, final AtomicIntegerArray[] matrix_b,
            final AtomicIntegerArray[] matrix_product) {
        // position of where the matrix product is up to
        this.row = row;
        this.col = col;
        // matrix sizes : assuming all matrices[matrix_size][matrix_size]
        this.matrix_size = matrix_size;
        // the two matrices to be multiplied together
        this.matrix_a = matrix_a;
        this.matrix_b = matrix_b;
        // the product matrix to be determined
        this.matrix_product = matrix_product;
    }

    // calculate result stored at matrix_product[row, col]
    public void run() {
        // initialise the element total
        int tmp_product = 0;
        // evaluate the dot product of the vectors A[row, :] and B[:, col]
        for (int elem = 0; elem < matrix_size; elem++) {
            // find the total tmp_product
            tmp_product = tmp_product + (matrix_a.get(elem) * matrix_b[elem].get(col));
        }
        // assign the tmp_product to the product matrix
        matrix_product[row].set(col, tmp_product);
    }

}