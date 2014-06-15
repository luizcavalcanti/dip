package br.edu.ufam.dip.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import br.edu.ufam.ImageIOUtils;

public class BoxBlur {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Usage:    BoxBlur <source-image> <dest-image> <iteractions>");
            System.err.println("example:  BoxBlur source.png dest.png 20");
            System.exit(1);
        }
        String origPath = args[0];
        String destPath = args[1];
        int iteractions = Integer.parseInt(args[2]);

        BufferedImage image = ImageIOUtils.loadImageFromFile(origPath);
        int[][] imageData = ImageIOUtils.getImageData(image);
        int[][] output = ImageIOUtils.cloneImageData(imageData);

        for (int i=0; i<iteractions; i++) {
            output = applyFilter(output);
        }

        ImageIO.write(ImageIOUtils.getImageFromData(output), "png", new File(destPath));
    }

    public static int[][] applyFilter(int[][] image) {
        int[][] result = ImageIOUtils.cloneImageData(image);
        int width = result.length;
        int height = result[0].length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result[x][y] = calculatePixelValue(image, x, y);
            }
        }
        return result;
    }

    private static int calculatePixelValue(int[][] image, int x, int y) {
        int count = 0;
        int sumR = 0;
        int sumG = 0;
        int sumB = 0;
        if (x > 0) {
            Color c = new Color(image[x - 1][y]);
            sumR += c.getRed();
            sumG += c.getGreen();
            sumB += c.getBlue();
            count++;
        }
        if (x + 1 < image.length) {
            Color c = new Color(image[x + 1][y]);
            sumR += c.getRed();
            sumG += c.getGreen();
            sumB += c.getBlue();
            count++;
        }
        if (y > 0) {
            Color c = new Color(image[x][y - 1]);
            sumR += c.getRed();
            sumG += c.getGreen();
            sumB += c.getBlue();
            count++;
        }
        if (y + 1 < image[0].length) {
            Color c = new Color(image[x][y + 1]);
            sumR += c.getRed();
            sumG += c.getGreen();
            sumB += c.getBlue();
            count++;
        }
        if (x > 0 && y > 0) {
            Color c = new Color(image[x - 1][y - 1]);
            sumR += c.getRed();
            sumG += c.getGreen();
            sumB += c.getBlue();
            count++;
        }
        if (x > 0 && y + 1 < image[0].length) {
            Color c = new Color(image[x - 1][y + 1]);
            sumR += c.getRed();
            sumG += c.getGreen();
            sumB += c.getBlue();
            count++;
        }
        if (x + 1 < image.length && y + 1 < image[0].length) {
            Color c = new Color(image[x + 1][y + 1]);
            sumR += c.getRed();
            sumG += c.getGreen();
            sumB += c.getBlue();
            count++;
        }
        if (x + 1 < image.length && y > 0) {
            Color c = new Color(image[x + 1][y - 1]);
            sumR += c.getRed();
            sumG += c.getGreen();
            sumB += c.getBlue();
            count++;
        }
        int r = sumR / count;
        int g = sumG / count;
        int b = sumB / count;
        return new Color(r, g, b).getRGB();
    }

}
