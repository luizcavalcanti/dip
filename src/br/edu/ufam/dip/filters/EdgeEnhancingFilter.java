package br.edu.ufam.dip.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import br.edu.ufam.ImageIOUtils;

public class EdgeEnhancingFilter {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Usage:    EdgeEnhancingFilter <source-image> <dest-image> <iteractions>");
            System.err.println("example:  EdgeEnhancingFilter source.png dest.png 20");
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
        int[][] result = embossConvolution(image);
        result = sharpeningConvolution(result);
        return result;
    }

    private static int[][] embossConvolution(int[][] image) {
        float[][] convMatrix = {{-2, 0, 0},
                                { 0, 1, 0},
                                { 0, 0, 2}};
        return convolute(image, convMatrix);
    }

    private static int[][] sharpeningConvolution(int[][] image) {
        float[][] convMatrix = {{-1/9, -1/9, -1/9},
                                {-1/9, 17/9, -1/9},
                                {-1/9, -1/9, -1/9}};
        return convolute(image, convMatrix);
    }

    private static int[][] convolute(int[][] image, float[][] convMatrix) {
        if (convMatrix.length!=3 || convMatrix[0].length != 3) {
            throw new RuntimeException("Convolution matrix should be 3x3");
        }

        int[][] output = new int[image.length][image[0].length];

        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                double sum = 0;
                if (x + 1 < image.length) {
                    int c = new Color(image[x + 1][y]).getRed();
                    sum += c * convMatrix[2][1];
                }
                if (y + 1 < image[0].length) {
                    int c = new Color(image[x][y + 1]).getRed();
                    sum += c * convMatrix[1][2];
                }
                if (x > 0) {
                    int c = new Color(image[x - 1][y]).getRed();
                    sum += c * convMatrix[0][1];
                }
                if (y > 0) {
                    int c = new Color(image[x][y - 1]).getRed();
                    sum += c * convMatrix[1][0];
                }
                if (x + 1 < image.length && y + 1 < image[0].length) {
                    int c = new Color(image[x + 1][y + 1]).getRed();
                    sum += c * convMatrix[2][2];
                }
                if (x > 0 && y + 1 < image[0].length) {
                    int c = new Color(image[x - 1][y + 1]).getRed();
                    sum += c * convMatrix[0][2];
                }
                if (x > 0 && y > 0) {
                    int c = new Color(image[x - 1][y - 1]).getRed();
                    sum += c * convMatrix[0][0];
                }
                if (x + 1 < image.length && y > 0) {
                    int c = new Color(image[x + 1][y - 1]).getRed();
                    sum += c * convMatrix[2][0];
                }
                int c = new Color(image[x][y]).getRed();
                sum += c * convMatrix[1][1];
                sum = Math.max(Math.min(sum,255),0);
                output[x][y] = new Color((int)sum,(int)sum,(int)sum).getRGB();
            }
        }
        return output;
    }

    

}
