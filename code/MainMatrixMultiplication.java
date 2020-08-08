package matrix.mulitiplcation;

import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
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
    private static AtomicIntegerArray[] matrix_product_parallel;
    private static AtomicIntegerArray[] matrix_product_serial;


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
        // initialise matrix_product_parallel
        matrix_product_parallel = new AtomicIntegerArray[matrix_size]; // matrix: vector of arrays
        matrix_product_parallel = InitialiseZeroMatrix(matrix_size, matrix_max_num);
        System.out.println("\tInitialised the parallel result matrix...");
        // initialise matrix_product_serial
        matrix_product_serial = new AtomicIntegerArray[matrix_size]; // matrix: vector of arrays
        matrix_product_serial = InitialiseZeroMatrix(matrix_size, matrix_max_num);
        System.out.println("\tInitialised the serial result matrix...");
    }


    // Send the information for matrix multiplication in parallel
    public static void ParallelMatrixDistributor(ExecutorService executor, int matrix_size,
            AtomicIntegerArray[] matrix_a, 
            AtomicIntegerArray[] matrix_b, 
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


    // Send the information for matrix multiplication in serial
    public static AtomicIntegerArray[] SerialMatrixDistributor(int matrix_size,
            AtomicIntegerArray[] matrix_a, 
            AtomicIntegerArray[] matrix_b, 
            AtomicIntegerArray[] matrix_product) {
        // initialise the multiplier object
        SerialMultiplier smm = new SerialMultiplier(matrix_size, matrix_a, matrix_b, matrix_product);
        // multiply the matrices
        smm.MultiplyMatrices();
        // return the product
        return smm.getProduct();
    }


    // Print matrix to the console in serial
    public static void SerialPrintMatrix(AtomicIntegerArray[] matrix, 
            int matrix_size, 
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


    // Check the number of differences 
    public static int CheckErrorInMatrices(int matrix_size,
            AtomicIntegerArray[] matrix_1, 
            AtomicIntegerArray[] matrix_2) {
        int num_errors = 0;
        // for each row
        for (int i = 0; i < matrix_size; i++) {
            // for each column
            for (int j = 0; j < matrix_size; j++) {
                // check if the element is the same for both matrices
                if (Math.abs(matrix_1[i].get(j) - matrix_2[i].get(j)) > 0) {
                    // count the number of elements that are different
                    num_errors += 1;
                }
            }
        }
        return num_errors;
    }


    public static void SaveMatrix(int matrix_size, String filename, AtomicIntegerArray[] matrix) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            for (int i = 0; i < matrix_size; i++) {
                for (int j = 0; j < matrix_size; j++) {
                    if (j == matrix_size-1) {
                        bw.write(matrix[i].get(j) + " \n");
                    } else {
                        bw.write(matrix[i].get(j) + ", ");
                    }
                }
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Main Program
	public static void main(String[] args) {
        try {
            // check that the main program starts running
            System.out.println("Running the main program...");
            System.out.println("");


            // debug mode boolean
            boolean bool_time_parallel  = true; // perform the serial parallel product?
            boolean bool_time_serial    = true; // perform the serial matrix product?
            boolean bool_print_matrices = false; // display the matrices?
            boolean bool_debug_mode     = false; // run the program in debug mode?
            // matrix dimensions and maximum values
            int matrix_size      = 500; // matrix[matrix_size][matrix_size]
            int matrix_max_num   = 10; // maximum number to be used in the random matrices
            // number of times to repeat calculations
            int repeat_num_times = 5;
            // initialise the timing variales
            double start_time;
            double finish_time;
            double ave_time_parallel;
            double ave_time_serial;


            // initialising matrices (a and b) with random values
            System.out.format("Initialising matrices of dimensions: %d x %d\n", matrix_size, matrix_size);
            InitializeMatrices(matrix_size, matrix_max_num);
            System.out.println("");


            // print the matrix A and B
            if (bool_print_matrices) {
                // print matrix A
                System.out.println("Printing matrix A...");
                SerialPrintMatrix(matrix_a, matrix_size, bool_debug_mode);
                System.out.println("Finished printing...");
                System.out.println("");
            
                // print matrix B
                System.out.println("Printing matrix B...");
                SerialPrintMatrix(matrix_b, matrix_size, bool_debug_mode);
                System.out.println("Finished printing...");
                System.out.println("");
            }


            // compuate matrix product in serial 
            if (bool_time_serial) {
                // multiply matrices in serial
                System.out.println("Multiplying matrices A and B in serial...");
                // start timing
                start_time = System.nanoTime();
                // repeat calculation repeat_num_times times
                for (int i = 0; i < repeat_num_times; i++) {
                    matrix_product_serial = SerialMatrixDistributor(matrix_size, matrix_a, matrix_b, matrix_product_serial);
                }
                // stop timing
                finish_time = System.nanoTime();
                // average timing
                ave_time_serial = (finish_time-start_time)/(1e9 * (double)repeat_num_times);
                System.out.printf("Finished multiplying in serial with aveage time: %s seconds.\n", String.format("%.5f", ave_time_serial));
                System.out.println("");
                // print the serial product matrix
                if (bool_print_matrices) {
                    System.out.println("Printing the (serial) product matrix...");
                    SerialPrintMatrix(matrix_product_serial, matrix_size, bool_debug_mode);
                    System.out.println("Finished printing...");
                    System.out.println("");
                }
                // save matrix
                SaveMatrix(matrix_size, "matrix_product_serial.txt", matrix_product_serial);
            }


            // compute matrix product in parallel : row-wise parallel with multiple threads
            if (bool_time_parallel) {
                // thread pool for space efficiency (maximum of 10 threads spawned at a given time)
                ExecutorService executor = Executors.newFixedThreadPool(10);
                // multiply the matrices in parallel
                System.out.println("Multiplying matrices A and B in parallel...");
                // start timing
                start_time = System.nanoTime();
                // repeat calculation repeat_num_times times
                for (int i = 0; i < repeat_num_times; i++) {
                    ParallelMatrixDistributor(executor, matrix_size, matrix_a, matrix_b, matrix_product_parallel);
                }
                // close all the opened threads
                executor.shutdown();
                // blocked the program here until all tasks have finished running
                try { executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); }
                catch (InterruptedException e) { e.printStackTrace(); }
                // stop timing
                finish_time = System.nanoTime();
                // average time
                ave_time_parallel = (finish_time-start_time)/(1e9 * (double)repeat_num_times);
                System.out.printf("Finished multiplying in paralllel with ave time: %s seconds.\n", String.format("%.5f", ave_time_parallel));
                System.out.println("");
                // print the parallel product matrix
                if (bool_print_matrices) {
                    System.out.println("Printing the (parallel) product matrix...");
                    SerialPrintMatrix(matrix_product_parallel, matrix_size, bool_debug_mode);
                    System.out.println("Finished printing...");
                    System.out.println("");
                }
                // save matrix
                SaveMatrix(matrix_size, "matrix_product_parallel.txt", matrix_product_parallel);
            }


            // calculate the number of differences between the serial and parallel products
            if (bool_time_parallel & bool_time_serial) {
                System.out.format("There are: %d differences between the two products\n", CheckErrorInMatrices(matrix_size, matrix_product_parallel, matrix_product_serial));
                System.out.println("");
            }


            // finished program
            System.out.println("Finished the main program...");
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
