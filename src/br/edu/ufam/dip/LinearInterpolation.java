package br.edu.ufam.dip;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import br.edu.ufam.ImageIOUtils;

public class LinearInterpolation {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Usage:    LinearInterpolation <source-image> <dest-image> <factor>");
            System.err.println("example:  LinearInterpolation source.png dest.png 1.5");
            System.exit(1);
        }
        String origPath = args[0];
        String destPath = args[1];
        double factor = Double.parseDouble(args[2]);

        BufferedImage image = ImageIOUtils.loadImageFromFile(origPath);
        int[][] imageData = ImageIOUtils.getImageData(image);
        int[][] output = resize(imageData, factor);
        ImageIO.write(ImageIOUtils.getImageFromData(output), "png", new File(destPath));
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

}