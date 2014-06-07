package br.edu.ufam.dip.segmentation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import br.edu.ufam.ImageIOUtils;

public class SimpleThresholding {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Usage: Thresholding <source-image> <dest-image> <threshold or interval>");
            System.err.println("example:");
            System.err.println("          Thresholding source.png dest.png 85");
            System.err.println(" or       Thresholding source.png dest.png 150:220");
            System.exit(1);
        }
        String origPath = args[0];
        String destPath = args[1];
        int threshold1;
        int threshold2;
        if (args[2].indexOf(':')>-1) {
            String[] ts = args[2].split(":");
            threshold1 = Integer.parseInt(ts[0]);
            threshold2 = Integer.parseInt(ts[1]);
        } else {
            threshold1 = Integer.parseInt(args[2]);
            threshold2 = -1;
        }

        BufferedImage image = ImageIOUtils.loadImageFromFile(origPath);
        int[][] imageData = ImageIOUtils.getImageData(image);
        int[][] output = applySegmentation(imageData, threshold1, threshold2);
        ImageIO.write(ImageIOUtils.getImageFromData(output), "png", new File(destPath));
    }

    public static int[][] applySegmentation(int[][] imageData, int threshold1, int threshold2) {
        int width = imageData.length;
        int height = imageData[0].length;
        int[][] output = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color c = new Color(imageData[x][y]);
                int grey = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                if (threshold2==-1) {
                    if (grey > threshold1) {
                        output[x][y] = new Color(255, 255, 255).getRGB();
                    }
                } else if (grey > threshold1 && grey < threshold2) {
                        output[x][y] = new Color(255, 255, 255).getRGB();
                }
            }
        }
        return output;
    }
}
