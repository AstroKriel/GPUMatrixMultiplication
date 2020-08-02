import java.util.concurrent.atomic.AtomicIntegerArray;

public class ParallelMultiplier extends Thread {
    // important variables
    private int row;
    private int column;
    private AtomicIntegerArray matrixA;
    private AtomicIntegerArray[] matrixB;
    private AtomicIntegerArray[] product;

    // store the matrices and where the matrix product is up to
    public ParallelMultiplier(final int row, final int column,
            final AtomicIntegerArray matrixA, final AtomicIntegerArray[] matrixB,
            final AtomicIntegerArray[] product) {
        // position of where the matrix product is up to
        this.row = row;
        this.column = column;
        // the two matrices to be multiplied together
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        // the product matrix to be determined
        this.product = product;
    }

    // calculate result stored at PRODUCT[row, column]
    public void run() {
        // initialise the element total
        int value = 0;
        // evaluate the dot product of the vector A[row, :] and B[:, column]
        for (int i = 0; i < matrixA.length(); i++) {
            // find the total value
            value = value + (matrixA.get(i) * matrixB[i].get(column));
        }
        // assign the value to the product matrix
        product[row].set(column, value);
    }
}