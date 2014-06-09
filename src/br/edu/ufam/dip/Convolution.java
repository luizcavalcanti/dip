package br.edu.ufam.dip;

public class Convolution {

    public static void main(String[] args) {
        int[][] convmatrix = {{1,2,3},{4,5,6},{7,8,9}};
        // int[][] image = {{0,0,0,0,0},{0,0,0,0,0},{0,0,1,0,0},{0,0,0,0,0},{0,0,0,0,0}};
        int[][] image = {{0,0,0,0,0},{0,1,1,1,0},{0,1,1,1,0},{0,1,1,1,0},{0,0,0,0,0}};
        // int[][] image = {{0,0,0,0,0},{0,1,1,1,0},{0,1,0,1,0},{0,1,1,1,0},{0,0,0,0,0}};
        int[][] output = new int[image.length][image[0].length];

        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                int sum = 0;
                int count = 0;
                if (x + 1 < image.length) {
                    sum += image[x + 1][y] * convmatrix[2][1];
                }
                if (y + 1 < image[0].length) {
                    sum += image[x][y + 1] * convmatrix[1][2];
                }
                if (x > 0) {
                    sum += image[x - 1][y] * convmatrix[0][1];
                }
                if (y > 0) {
                    sum += image[x][y - 1] * convmatrix[1][0];
                }
                if (x + 1 < image.length && y + 1 < image[0].length) {
                    sum += image[x + 1][y + 1] * convmatrix[2][2];
                }
                if (x > 0 && y + 1 < image[0].length) {
                    sum += image[x - 1][y + 1] * convmatrix[0][2];
                }
                if (x > 0 && y > 0) {
                    sum += image[x - 1][y - 1] * convmatrix[0][0];
                }
                if (x + 1 < image.length && y > 0) {
                    sum += image[x + 1][y - 1] * convmatrix[2][0];
                }
                sum += image[x][y] * convmatrix[1][1];
                output[x][y] = sum;
            }
        }
        printArray(image);
        System.out.println("");
        System.out.println("");
        printArray(output);
    }

    private static void printArray(int[][] array) {
        for (int x = 0; x < array.length; x++) {
            System.out.print("[");
            for (int y = 0; y < array[0].length; y++) {
                if (array[x][y]<10) 
                    System.out.print(" ");
                System.out.print(array[x][y]+" ");
            }
            System.out.print("]\n");
        } 
    }

}