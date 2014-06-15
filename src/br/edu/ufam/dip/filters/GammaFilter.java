package br.edu.ufam.dip.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import br.edu.ufam.ImageIOUtils;

public class GammaFilter {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Usage:    GammaFilter <source-image> <dest-image> <gamma>");
            System.err.println("example:  GammaFilter source.png dest.png 0.4");
            System.exit(1);
        }
        String origPath = args[0];
        String destPath = args[1];
        double gamma = Double.parseDouble(args[2]);

        BufferedImage image = ImageIOUtils.loadImageFromFile(origPath);
        int[][] imageData = ImageIOUtils.getImageData(image);
        int[][] output = applyFilter(imageData, gamma);
        ImageIO.write(ImageIOUtils.getImageFromData(output), "png", new File(destPath));
    }

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
