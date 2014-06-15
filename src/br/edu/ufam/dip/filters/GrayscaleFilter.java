package br.edu.ufam.dip.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import br.edu.ufam.ImageIOUtils;

public class GrayscaleFilter {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage:    GrayscaleFilter <source-image> <dest-image>");
            System.err.println("example:  GrayscaleFilter source.png dest.png");
            System.exit(1);
        }
        String origPath = args[0];
        String destPath = args[1];

        BufferedImage image = ImageIOUtils.loadImageFromFile(origPath);
        int[][] imageData = ImageIOUtils.getImageData(image);
        int[][] output = convertToGrayscale(imageData);
        ImageIO.write(ImageIOUtils.getImageFromData(output), "png", new File(destPath));
    }

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
}