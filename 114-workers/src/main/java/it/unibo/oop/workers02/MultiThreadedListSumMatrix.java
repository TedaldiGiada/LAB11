package it.unibo.oop.workers02;

import java.util.ArrayList;
import java.util.List;

public final class MultiThreadedListSumMatrix implements SumMatrix {

    private final int threads;
    public MultiThreadedListSumMatrix(final int n) {
        super();
        this.threads = n;
    }
    private static class Worker extends Thread {

        private final double[][] matrix;
        private final int start;
        private final int nelem;
        private long res;

        private Worker(final double[][] matrix, final int start, final int nelem) {
            super();
            this.matrix = matrix;
            this.start = start;
            this.nelem = nelem;
        }

        public void run() {
            for (int i = start; i < matrix.length && i < start + nelem; i++) {
                for(final double d : this.matrix[i]) {
                    this.res += d;
                } 
            }
        }

        public long getResult() {
            return this.res;
        }
    }

    public double sum(final double[][] matrix) {
        int rows = matrix.length / threads + matrix.length % threads;
        List<Worker> tasks = new ArrayList<>(threads);

        for(int i = 0; i < matrix.length; i += rows) {
            tasks.add(new Worker(matrix, i, rows));
        }
        for(final Worker w : tasks) {
            w.start();
        }
        double sum = 0;
        for(final Worker w : tasks) {
            try {
                w.join();
                sum += w.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            
        }
        return sum;
    }
}

    