public class SerialMultiplier {
    private AtomicIntegerArray matrixA;
    private AtomicIntegerArray[] matrixB;
    private AtomicIntegerArray[] product;

    public SerialMultiplier(final AtomicIntegerArray matrixA, final AtomicIntegerArray[] matrixB,
            final AtomicIntegerArray[] product) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.product = product;
    }

    
}