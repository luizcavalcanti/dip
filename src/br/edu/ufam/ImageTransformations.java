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

    public static int[][] convertToNegative(int[][] dados) {
        int[][] negative = new int[dados.length][dados[0].length];
        for (int x = 0; x < negative.length; x++) {
            for (int y = 0; y < negative[x].length; y++) {
                negative[x][y] = Color.WHITE.getRGB() - dados[x][y];
            }
        }
        return negative;
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

    private static int max(int[][] image) {
        int width = image.length;
        int height = image[0].length;
        int max = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color c = new Color(image[x][y]);
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();
                int gray = r + g + b / 3;
                max = Math.max(max, gray);
            }
        }
        return max;
    }

    public static int[][] gammaFilter(int[][] image, double gamma) {
        int width = image.length;
        int height = image[0].length;
        int[][] output = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color c = new Color(image[x][y]);
                int r = (int) (Math.pow(c.getRed(), gamma));
                int g = (int) (Math.pow(c.getGreen(), gamma));
                int b = (int) (Math.pow(c.getBlue(), gamma));
                output[x][y] = new Color(r, g, b).getRGB();
            }
        }
        int con = 255 / max(output);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color c = new Color(output[x][y]);
                int r = (int) Math.min(255, c.getRed() * con);
                int g = (int) Math.min(255, c.getGreen() * con);
                int b = (int) Math.min(255, c.getBlue() * con);
                output[x][y] = new Color(r, g, b).getRGB();
            }
        }
        return output;
    }

}
