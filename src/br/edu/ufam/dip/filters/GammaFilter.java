package br.edu.ufam.dip.filters;

import java.awt.Color;

public class GammaFilter {

    public static int[][] applyFilter(int[][] image, double gamma) {
        int width = image.length;
        int height = image[0].length;
        int[][] output = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color c = new Color(image[x][y]);
                int r = (int) (Math.pow(c.getRed(), gamma));
                int g = (int) (Math.pow(c.getGreen(), gamma));
                int b = (int) (Math.pow(c.getBlue(), gamma));
                output[x][y] = new Color(Math.min(r, 255), Math.min(g, 255), Math.min(b, 255)).getRGB();
            }
        }
        int con[] = new int[3];
        int max[] = max(output);
        con[0] = 255 / max[0];
        con[1] = 255 / max[1];
        con[2] = 255 / max[2];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color c = new Color(output[x][y]);
                int r = (int) Math.min(255, c.getRed() * con[0]);
                int g = (int) Math.min(255, c.getGreen() * con[1]);
                int b = (int) Math.min(255, c.getBlue() * con[2]);
                output[x][y] = new Color(Math.min(r, 255), Math.min(g, 255), Math.min(b, 255)).getRGB();
            }
        }
        return output;
    }

    private static int[] max(int[][] image) {
        int width = image.length;
        int height = image[0].length;
        int max[] = new int[3];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color c = new Color(image[x][y]);
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();
                max[0] = Math.max(max[0], r);
                max[1] = Math.max(max[1], g);
                max[2] = Math.max(max[2], b);
            }
        }
        return max;
    }
}
