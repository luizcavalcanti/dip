package br.edu.ufam.dip;

import java.awt.Color;

public class Convolution {

    private static final int RED = 0;
    private static final int GREEN = 1;
    private static final int BLUE = 2;

    public static int[][] convolute(int[][] image, int[][] convMatrix) {
        int[][] output = new int[image.length][image[0].length];
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                int sum = 0;
                int r = getChannelSum(image, convMatrix, x, y, RED)/9;
                r = Math.max(r,0);
                // int g = getChannelSum(image, convMatrix, x, y, GREEN)/9;
                // int b = getChannelSum(image, convMatrix, x, y, BLUE)/9;
                if (r<0 || r>255)
                    System.out.println("r: "+r);
                output[x][y] = new Color(r,r,r).getRGB();
            }
        }
        return output;
    }

    private static int getChannelSum(int[][] image, int[][] convMatrix, int x, int y, int channel) {
        int sum = 0;
        if (x + 1 < image.length) {
            sum += (image[x + 1][y] & 0xff) * convMatrix[2][1];
        }
        if (y + 1 < image[0].length) {
            sum += (image[x][y + 1] & 0xff) * convMatrix[1][2];
        }
        if (x > 0) {
            sum += (image[x - 1][y] & 0xff) * convMatrix[0][1];
        }
        if (y > 0) {
            sum += (image[x][y - 1] & 0xff) * convMatrix[1][0];
        }
        if (x + 1 < image.length && y + 1 < image[0].length) {
            sum += (image[x + 1][y + 1] & 0xff) * convMatrix[2][2];
        }
        if (x > 0 && y + 1 < image[0].length) {
            sum += (image[x - 1][y + 1] & 0xff) * convMatrix[0][2];
        }
        if (x > 0 && y > 0) {
            sum += (image[x - 1][y - 1] & 0xff) * convMatrix[0][0];
        }
        if (x + 1 < image.length && y > 0) {
            sum += (image[x + 1][y - 1] & 0xff) * convMatrix[2][0];
        }
        sum += (image[x][y] & 0xff) * convMatrix[1][1];
        return sum;
    }

}