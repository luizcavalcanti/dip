package br.edu.ufam;

import java.awt.Color;

public class ImageTransformations {

    public static int[][] convertToGrayscale(int[][] imageData) {
        int width = imageData.length;
        int height = imageData[0].length;
        int[][] grayImageData = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color c = new Color(imageData[x][y]);
                int gray = (int) (0.2989 * c.getRed() + 0.5870 * c.getGreen() + 0.1140 * c.getBlue());
                grayImageData[x][y] = new Color(gray, gray, gray).getRGB();
            }
        }
        return grayImageData;
    }

    public static int[][] resize(int[][] image, double zoomFactor) {
        int newWidth = (int) (image.length * zoomFactor);
        int newHeight = (int) (image[0].length * zoomFactor);
        return resize(image, newWidth, newHeight);
    }

    public static int[][] resize(int[][] image, int newWidth, int newHeight) {
        int[][] output = new int[newWidth][newHeight];
        double xRatio = image.length / (double) newWidth;
        double yRatio = image[0].length / (double) newHeight;
        int x, y;
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                x = (int) Math.floor(i * xRatio);
                y = (int) Math.floor(j * yRatio);
                output[i][j] = image[x][y];
            }
        }
        return output;
    }

    public static int[][] clip(int[][] image, int x, int y, int width, int height) {
        int[][] output = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                output[i][j] = image[x + i][y + j];
            }
        }
        return output;
    }

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
