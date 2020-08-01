public class SerialMultiplier {
    private AtomicIntegerArray[] matrixA;
    private AtomicIntegerArray[] matrixB;
    private AtomicIntegerArray[] product;

    public SerialMultiplier(final AtomicIntegerArray[] matrixA, final AtomicIntegerArray[] matrixB,
            final AtomicIntegerArray[] product) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.product = product;
    }

    public void MultiplyMatrices() {
        for (int row = 0; row < matrixA.length(); row++) {
            int value = 0;
            for (int i = 0; i < matrixA.length(); i++) {
                value = value + (matrixA.get(i) * matrixB[i].get(column));
            }
            product[row].set(column, value);
        }
    }
}