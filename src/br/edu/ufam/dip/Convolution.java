package br.edu.ufam.dip;

public class Convolution {

    private static final int RED = 0;
    private static final int GREEN = 1;
    private static final int BLUE = 2;

    public static int[][] convolute(int[][] image, int[][] convMatrix) {
        int[][] output = new int[image.length][image[0].length];
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                output[x][y] = getNeighborsMean(image, convMatrix, x, y);
            }
        }
        return output;
    }

    private static int getNeighborsMean(int[][] image, int[][] convMatrix, int x, int y) {
        int sum = 0;
        int count = 0;
        if (x + 1 < image.length) {
            sum += image[x + 1][y] * convMatrix[2][1];
            count++;
        }
        if (y + 1 < image[0].length) {
            sum += image[x][y + 1] * convMatrix[1][2];
            count++;
        }
        if (x > 0) {
            sum += image[x - 1][y] * convMatrix[0][1];
            count++;
        }
        if (y > 0) {
            sum += image[x][y - 1] * convMatrix[1][0];
            count++;
        }
        if (x + 1 < image.length && y + 1 < image[0].length) {
            sum += image[x + 1][y + 1] * convMatrix[2][2];
            count++;
        }
        if (x > 0 && y + 1 < image[0].length) {
            sum += image[x - 1][y + 1] * convMatrix[0][2];
            count++;
        }
        if (x > 0 && y > 0) {
            sum += image[x - 1][y - 1] * convMatrix[0][0];
            count++;
        }
        if (x + 1 < image.length && y > 0) {
            sum += image[x + 1][y - 1] * convMatrix[2][0];
            count++;
        }
        sum += image[x][y] * convMatrix[1][1];
        count++;
        return (int) (sum/count);
    }

}