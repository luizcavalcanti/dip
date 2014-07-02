package br.edu.ufam.dip.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import br.edu.ufam.ImageIOUtils;
import br.edu.ufam.dip.Convolution;

public class SobelFilter {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage:    SobelFilter <source-image> <dest-image>");
            System.err.println("example:  SobelFilter source.png dest.png");
            System.exit(1);
        }
        String origPath = args[0];
        String destPath = args[1];

        BufferedImage image = ImageIOUtils.loadImageFromFile(origPath);
        int[][] imageData = ImageIOUtils.getImageData(image);
        int[][] output = applyFilter(imageData);
        ImageIO.write(ImageIOUtils.getImageFromData(output), "png", new File(destPath));
    }

    public static int[][] applyFilter(int[][] image) {
        int width = image.length;
        int height = image[0].length;
        int[][] output = new int[width][height];
        double[][] outX = new double[width][height];
        double[][] outY = new double[width][height];
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                if (x==0 || x==width-1 || y==0 || y==height-1)
                    outX[x][y] = outY[x][y] = output[x][y] = 0;
                else{
                    outX[x][y] = image[x+1][y-1] + 2*image[x+1][y] + image[x+1][y+1] -
                             image[x-1][y-1] - 2*image[x-1][y] - image[x-1][y+1];
                    outY[x][y] = image[x-1][y+1] + 2*image[x][y+1] + image[x+1][y+1] -
                             image[x-1][y-1] - 2*image[x][y-1] - image[x+1][y-1];
                    output[x][y]  = (int)(Math.abs(outX[x][y]) + Math.abs(outY[x][y]));
                }
            }
        }
        return output;
    }
}