package matrix.mulitiplcation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicIntegerArray;

/*
 * Author : Neco Kriel
 * Resources : Naveen Nandan (https://github.com/nav-nandan/parallel-matrix-multiplier/blob/master/src/parallel/matrix/multiplication/ParallelMultiplier.java)
 * 
 * Description : This program performs scalable parallel row-wise matrix multiplication on two square matrices
 * by making use of multi-threading. A thread pool is used for space efficiency (limiting the maximum number
 * of threads spawned).
 */
public class MainMatrixMultiplication {
    // thread-safe data structures for storing matrices data
    private static AtomicIntegerArray[] matrix_a;
    private static AtomicIntegerArray[] matrix_b;
    private static AtomicIntegerArray[] matrix_product;


    // Initialise a matrix filled with random values
    public static AtomicIntegerArray[] InitialiseRandomMatrix(int matrix_size, int matrix_max_num) {
        AtomicIntegerArray[] matrix = new AtomicIntegerArray[matrix_size]; // matrix is stored as a vector of arrays
        // for each row of the matrix
        for (int i = 0; i < matrix_size; i++) {
            // store the column values in an array
            AtomicIntegerArray tmp_row = new AtomicIntegerArray(matrix_size);
            // for each column element
            for (int j = 0; j < matrix_size; j++) {
                // create a random value in [0, matrix_max_num]
                tmp_row.set(j, (int)(Math.random() * matrix_max_num));
            }
            // store the array of column elements in the vector
            matrix[i] = tmp_row;
        }
        return matrix;
    }


    // Initialise a matrix filled with zeros 
    public static AtomicIntegerArray[] InitialiseZeroMatrix(int matrix_size, int matrix_max_num) {
        AtomicIntegerArray[] matrix = new AtomicIntegerArray[matrix_size]; // matrix is stored as a vector of arrays
        // for each row of the matrix
        for (int i = 0; i < matrix_size; i++) {
            // store the column values in an array
            AtomicIntegerArray tmp_row = new AtomicIntegerArray(matrix_size);
            // for each column element
            for (int j = 0; j < matrix_size; j++) {
                // set the element to zero
                tmp_row.set(j, 0);
            }
            // store the array of column elements in the vector
            matrix[i] = tmp_row;
        }
        return matrix;
    }


    // Initialise all matrices
    public static void InitializeMatrices(int matrix_size, int matrix_max_num) {
        // initialise matrix_a
        matrix_a = new AtomicIntegerArray[matrix_size]; // matrix is a vector of arrays
        matrix_a = InitialiseRandomMatrix(matrix_size, matrix_max_num);
        System.out.println("\tInitialised matrix A...");

        // initialise matrix_b
        matrix_b = new AtomicIntegerArray[matrix_size]; // matrix: vector of arrays
        matrix_b = InitialiseRandomMatrix(matrix_size, matrix_max_num);
        System.out.println("\tInitialised matrix B...");

        // initialise matrix_a
        matrix_product = new AtomicIntegerArray[matrix_size]; // matrix: vector of arrays
        matrix_product = InitialiseZeroMatrix(matrix_size, matrix_max_num);
        System.out.println("\tInitialised the result matrix...");
    }


    public static void ParallelMatrixDistributor(ExecutorService executor, int matrix_size,
            AtomicIntegerArray[] matrix_a, AtomicIntegerArray[] matrix_b, 
            AtomicIntegerArray[] matrix_product) {
        // for each matrix row
        for (int i = 0; i < matrix_size; i++) {
            // and column elemnt in the row
            for (int j = 0; j < matrix_size; j++) {
                // calculate the product result for the PRODUCT[row, column] value
                ParallelMultiplier multiplyThread = new ParallelMultiplier(i, j, matrix_size, matrix_a[i], matrix_b, matrix_product);
                // submit the thread to pool for row-wise multiplication
                executor.execute(multiplyThread);
            }
        }
    }


    public static double TimeParallelMatrixProduct(ExecutorService executor, int matrix_size,
            AtomicIntegerArray[] matrix_a, AtomicIntegerArray[] matrix_b, 
            AtomicIntegerArray[] matrix_product,
            int repeat_num_times) {
        double start = System.currentTimeMillis();
        for (int i = 0; i < repeat_num_times; i++) {
            ParallelMatrixDistributor(executor, matrix_size, matrix_a, matrix_b, matrix_product);
        }
        double finish = System.currentTimeMillis();
        return (finish-start)/(1000 * (double)repeat_num_times);
    }


    // Print matrix to the console in serial
    public static void SerialPrintMatrix(AtomicIntegerArray[] matrix, int matrix_size, 
            boolean bool_debug_mode) {
        // for each row
        for (int i = 0; i < matrix_size; i++) {
            // for each column
            for (int j = 0; j < matrix_size; j++) {
                // print the element to the console
                System.out.print(matrix[i].get(j));
                System.out.print(" ");
            }
            // add ";..." for matlab formating if debug mode is on
            if (bool_debug_mode & (i < matrix_size - 1)) {
                System.out.print(";...");
            }
            // start a new line for every row
            System.out.println();
        }
    }

    // Main Program
	public static void main(String[] args) {
        try {
            // check that the main program starts running
            System.out.println("Running the main program...");
            System.out.println("");

            // debug mode boolean
            boolean bool_debug_mode = false;
            boolean bool_print_matrices = false;

            // define matrix size
            int matrix_size = 100;
            int matrix_max_num = 1000;

            // define the number of times to repeat calculations to find the average evaluation time
            int repeat_num_times = 5;

            // initialising matrices (a and b) with random values
            System.out.format("Initialising matrices of dimensions: %d x %d\n", matrix_size, matrix_size);
            InitializeMatrices(matrix_size, matrix_max_num);
            System.out.println("");

            // thread pool for space efficiency (maximum of 10 threads spawned at a given time)
            ExecutorService executor = Executors.newFixedThreadPool(10);

            // row-wise parallel distribution to multiple threads
            System.out.println("Multiplying matrices A and B...");
            double ave_time_parallel = TimeParallelMatrixProduct(executor, matrix_size, matrix_a, matrix_b, matrix_product, repeat_num_times);
            String str_ave_time_parallel = String.format("%.3f", ave_time_parallel);
            System.out.printf("Finished multiplying with ave time: %s seconds.\n", str_ave_time_parallel);
            System.out.println("");

            // print matrix A
            if (bool_print_matrices) {
                System.out.println("Printing matrix A...");
                SerialPrintMatrix(matrix_a, matrix_size, bool_debug_mode);
                System.out.println("Finished printing...");
                System.out.println("");
            }

            // print matrix B
            if (bool_print_matrices) {
                System.out.println("Printing matrix B...");
                SerialPrintMatrix(matrix_b, matrix_size, bool_debug_mode);
                System.out.println("Finished printing...");
                System.out.println("");
            }

            // print the product matrix
            if (bool_print_matrices) {
                System.out.println("Printing the product matrix...");
                SerialPrintMatrix(matrix_product, matrix_size, bool_debug_mode);
                System.out.println("Finished printing...");
                System.out.println("");
            }

            // close all the opened threads
            executor.shutdown();

            // finished program
            System.out.println("Finished the main program...");
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}