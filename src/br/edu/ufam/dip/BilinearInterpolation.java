package br.edu.ufam.dip;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import br.edu.ufam.ImageIOUtils;

public class BilinearInterpolation {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Usage:    BilinearInterpolation <source-image> <dest-image> <factor>");
            System.err.println("example:  BilinearInterpolation source.png dest.png 1.5");
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
        int a, b, c, d, x, y, index;
        int w = image.length;
        int h = image[0].length;
        float xRatio = (float)w/(float)newWidth;
        float yRatio = (float)h/(float)newHeight;
        float diffX, diffY, blue, red, green;
        int offset = 0;
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                x = (int)(xRatio * i);
                y = (int)(yRatio * j);
                diffX = (xRatio * i) - x;
                diffY = (yRatio * j) - y;
                a = image[Math.max(x-1,0)][Math.max(y-1,0)];
                b = image[Math.min(x+1,w-1)][Math.max(y-1,0)];
                c = image[Math.max(x-1,0)][Math.min(y+1,h-1)];
                d = image[Math.min(x+1,w-1)][Math.min(y+1,h-1)];

                blue = (a&0xff)*(1-diffX)*(1-diffY) + (b&0xff)*(diffX)*(1-diffY) +
                    (c&0xff)*(diffY)*(1-diffX)   + (d&0xff)*(diffX*diffY);

                green = ((a>>8)&0xff)*(1-diffX)*(1-diffY) + ((b>>8)&0xff)*(diffX)*(1-diffY) +
                        ((c>>8)&0xff)*(diffY)*(1-diffX)   + ((d>>8)&0xff)*(diffX*diffY);

                red = ((a>>16)&0xff)*(1-diffX)*(1-diffY) + ((b>>16)&0xff)*(diffX)*(1-diffY) +
                      ((c>>16)&0xff)*(diffY)*(1-diffX)   + ((d>>16)&0xff)*(diffX*diffY);

                output[i][j] = 0xff000000 |
                    ((((int)red)<<16)&0xff0000) |
                    ((((int)green)<<8)&0xff00) |
                    ((int)blue);
            }
        }
        return output;
    }

}