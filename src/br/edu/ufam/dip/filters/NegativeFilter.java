package br.edu.ufam.dip.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import br.edu.ufam.ImageIOUtils;

public class NegativeFilter {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage:    NegativeFilter <source-image> <dest-image>");
            System.err.println("example:  NegativeFilter source.png dest.png");
            System.exit(1);
        }
        String origPath = args[0];
        String destPath = args[1];

        BufferedImage image = ImageIOUtils.loadImageFromFile(origPath);
        int[][] imageData = ImageIOUtils.getImageData(image);
        int[][] output = convertToNegative(imageData);
        ImageIO.write(ImageIOUtils.getImageFromData(output), "png", new File(destPath));
    }
    
    public static int[][] convertToNegative(int[][] imageData) {
        int[][] negative = new int[imageData.length][imageData[0].length];
        for (int x = 0; x < negative.length; x++) {
            for (int y = 0; y < negative[x].length; y++) {
                negative[x][y] = Color.WHITE.getRGB() - imageData[x][y];
            }
        }
        return negative;
    }

}