import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicIntegerArray;

/*
 * Author : Neco Kriel
 * Resources : Naveen Nandan
 * 
 * Description : This program performs scalable parallel row-wise matrix multiplication by making use of multi-threading. 
 * A thread pool is used for space efficiency (to limit the maximum number of threads spawned).
 */
public class MainMatrixMultiplication {
    // thread-safe data structures for storing matrices data
    private static AtomicIntegerArray[] matrix_a;
    private static AtomicIntegerArray[] matrix_b;
    private static AtomicIntegerArray[] matrix_product;

    // Initialise Matrices
    public static void InitializeMatrices(int matrix_size, int matrix_max_num) {
        // assumption: matrices a and b are both double[matrix_size][matrix_size]

        // initialise matrix_a
        matrix_a = new AtomicIntegerArray[matrix_size]; // matrix: vector of arrays
        // for each row of the matrix
        for (int i = 0; i < matrix_size; i++) {
            // initialise the column values
            AtomicIntegerArray tmp_row = new AtomicIntegerArray(matrix_size);
            for (int j = 0; j < matrix_size; j++) {
                // create a random value in [-matrix_max_num, matrix_max_num]
                double tmp_random_double = (Math.random() * 2 * matrix_max_num) - matrix_max_num;
                tmp_row.set(j, (int)tmp_random_double);
            }
            matrix_a[i] = tmp_row;
        }
        System.out.println("\tInitialised matrix A...");

        // initialise matrix_b
        matrix_b = new AtomicIntegerArray[matrix_size]; // matrix: vector of arrays
        // for each row of the matrix
        for (int i = 0; i < matrix_size; i++) {
            // initialise the column values
            AtomicIntegerArray tmp_row = new AtomicIntegerArray(matrix_size);
            for (int j = 0; j < matrix_size; j++) {
                // create a random value in [-matrix_max_num, matrix_max_num]
                double tmp_random_double = (Math.random() * 2 * matrix_max_num) - matrix_max_num;
                tmp_row.set(j, (int)tmp_random_double);
            }
            matrix_b[i] = tmp_row;
        }
        System.out.println("\tInitialised matrix B...");

        // initialise matrix_a
        matrix_product = new AtomicIntegerArray[matrix_size]; // matrix: vector of arrays
        // for each row of the matrix
        for (int i = 0; i < matrix_size; i++) {
            // initialise the column values
            AtomicIntegerArray tmp_row = new AtomicIntegerArray(matrix_size);
            for (int j = 0; j < matrix_size; j++) {
                tmp_row.set(j, 0);
            }
            matrix_product[i] = tmp_row;
        }
        System.out.println("\tInitialised the result matrix...");
    }

    // Main Program
	public static void main(String[] args) {
        try {
            // check that the main program starts running
            System.out.println("Running the main program...");
            System.out.println("");

            // define matrix size
            int matrix_size = 256;
            int matrix_max_num = 500;

            // initialising matrices (a and b) with random values
            System.out.format("Initialising matrices of dimensions: %d x %d\n", matrix_size, matrix_size);
            InitializeMatrices(matrix_size, matrix_max_num);
            System.out.println("");

            // thread pool for space efficiency (maximum of 10 threads spawned at a given time)
            ExecutorService executor = Executors.newFixedThreadPool(10);

            // row-wise parallel distribution to multiple threads
            System.out.println("Multiplying matrices A and B...");
            for (int i = 0; i < matrix_size; i++) {
                for (int j = 0; j < matrix_size; j++) {
                    // submit thread to pool for row-wise multiplication
                    ParallelMultiplier multiplyThread = new ParallelMultiplier(i, j, matrix_a[i], matrix_b, matrix_product);
                    executor.execute(multiplyThread);
                }
            }
            System.out.println("Finished multiplying...");
            System.out.println("");
            
            // // row-wise parallel display
            // for (int i = 0; i < matrix_size; i++) {
            //     // submit thread to pool for row-wise display
            //     ParallelPrinter displayThread = new ParallelPrinter(i, matrix_product);
            //     executor.execute(displayThread);
            //     System.out.println();
            // }

            // close all the opened threads
            executor.shutdown();

            // serial matrix
            

            // finished program
            System.out.println("Finished the main program...");
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}