package br.edu.ufam;

import java.awt.Color;

public class ImageTransformations {

    public static int[][] regionOfInterest(int[][] image, int[][] mask) {
        int width = image.length;
        int height = image[0].length;
        int[][] output = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                output[x][y] = image[x][y] * mask[x][y];
            }
        }
        return output;
    }

}
