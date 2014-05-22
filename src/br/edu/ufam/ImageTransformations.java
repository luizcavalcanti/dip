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
        int[][] negativo = new int[dados.length][dados[0].length];
        for (int x = 0; x < negativo.length; x++) {
            for (int y = 0; y < negativo[x].length; y++) {
                negativo[x][y] = Color.WHITE.getRGB() - dados[x][y];
            }
        }
        return negativo;
    }

}
