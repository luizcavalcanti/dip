package br.edu.ufam.dip;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import br.edu.ufam.ImageIOUtils;

public class Clip {

    public static void main(String[] args) throws IOException {
        if (args.length != 6) {
            System.err.println("Usage:    Clip <source-image> <dest-image> <x> <y> <width> <height>");
            System.err.println("example:  Clip source.png dest.png 0 20 40 50");
            System.exit(1);
        }
        String origPath = args[0];
        String destPath = args[1];
        int x = Integer.parseInt(args[2]);
        int y = Integer.parseInt(args[3]);
        int w = Integer.parseInt(args[4]);
        int h = Integer.parseInt(args[5]);

        BufferedImage image = ImageIOUtils.loadImageFromFile(origPath);
        int[][] imageData = ImageIOUtils.getImageData(image);
        int[][] output = clip(imageData, x, y, w, h);
        ImageIO.write(ImageIOUtils.getImageFromData(output), "png", new File(destPath));
    }

    public static int[][] clip(int[][] image, int x, int y, int width, int height) {
        int[][] output = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                output[i][j] = image[x + i][y + j];
            }
        }
        return output;
    }

}