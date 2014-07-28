package br.edu.ufam.dip.morphology;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import br.edu.ufam.ImageIOUtils;

public class Dilation {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Usage:    Dilation <source-image> <spectre-image> <threshold>");
            System.err.println("example:  Dilation source.png source_spectre.png 200");
            System.exit(1);
        }
        String origPath = args[0];
        String destPath = args[1];
        int threshold = Integer.parseInt(args[2]);

        BufferedImage image = ImageIOUtils.loadImageFromFile(origPath);
        // GAMBI
        Graphics g = image.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,image.getWidth(), image.getHeight());
        g.setColor(Color.WHITE);
        g.fillRect(100, 100, 200, 200);
        int[][] gambi = ImageIOUtils.getImageData(image);
        ImageIO.write(ImageIOUtils.getImageFromData(gambi), "png", new File(destPath+"_.jpg"));
        // FIM GAMBI
        int[][] imageData = ImageIOUtils.getImageData(image);
        int[][] output = applyDilation(imageData, threshold);
        ImageIO.write(ImageIOUtils.getImageFromData(output), "png", new File(destPath));

    }

    public static int[][] applyDilation(int[][] image, int threshold) {
        int width = image.length;
        int height = image[0].length;
        int[][] output = new int[width][height];
        int[][] strut = {{0,1,0},
                         {1,1,1},
                         {0,1,0}};

        for (int x = 0; x<width; x++) {
            for (int y = 0; y<height; y++) {
                Color c = new Color(image[x][y]);
                int r = c.getRed();
                if (r > threshold) {
                    if (x + 1 < width && output[x + 1][y] == 0) {
                        output[x + 1][y] = c.getRGB();
                    }
                    if (y + 1 < height && output[x][y + 1] == 0) {
                        output[x][y + 1] = c.getRGB();
                    }
                    if (x > 0 && output[x - 1][y] == 0) {
                        output[x - 1][y] = c.getRGB();;
                    }
                    if (y > 0 && output[x][y - 1] == 0) {
                        output[x][y - 1] = c.getRGB();;
                    }
                    if (x + 1 < width && y + 1 < height && output[x + 1][y + 1] == 0) {
                        output[x + 1][y + 1] = c.getRGB();;
                    }
                    if (x > 0 && y + 1 < height && output[x - 1][y + 1] == 0) {
                        output[x - 1][y + 1] = c.getRGB();;
                    }
                    if (x > 0 && y > 0 && output[x - 1][y - 1] == 0) {
                        output[x - 1][y - 1] = c.getRGB();;
                    }
                    if (x + 1 < width && y > 0 && output[x + 1][y - 1] == 0) {
                        output[x + 1][y - 1] = c.getRGB();;
                    }
                }
            }
        }
        return output;
    }

}