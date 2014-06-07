package br.edu.ufam.dip.segmentation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import br.edu.ufam.ImageIOUtils;

public class Thresholding {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Usage: Thresholding <source-image> <dest-image> <threshold>");
            System.exit(1);
        }
        String origPath = args[0];
        String destPath = args[1];
        int threshold = Integer.parseInt(args[2]);
        BufferedImage image = ImageIOUtils.loadImageFromFile(origPath);
        int[][] imageData = ImageIOUtils.getImageData(image);
        int[][] output = applySegmentation(imageData, threshold);
        ImageIO.write(ImageIOUtils.getImageFromData(output), "png", new File(destPath));
    }

    private static int[][] applySegmentation(int[][] imageData, int threshold) {
        int width = imageData.length;
        int height = imageData[0].length;
        int[][] output = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color c = new Color(imageData[x][y]);
                int grey = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                if (grey > threshold) {
                    output[x][y] = new Color(255, 255, 255).getRGB();
                }
            }
        }
        return output;
    }
}
