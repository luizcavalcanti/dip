package br.edu.ufam.dip;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import br.edu.ufam.ImageIOUtils;

public class BicubicInterpolation {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Usage:    BicubicInterpolation <source-image> <dest-image> <factor>");
            System.err.println("example:  BicubicInterpolation source.png dest.png 1.5");
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

    private static float cubicPolate(float v0, float v1, float v2, float v3, float frac) {
        float a = (v3-v2)-(v0-v1);
        float b = (v0-v1)-a;
        float c = v2-v0;
        float d = v1;
        return (float)(a*Math.pow(frac,3)) + (float)(b*Math.pow(frac,2)) + c * frac + d;
    }

    public static int[][] resize(int[][] image, int newWidth, int newHeight) {
        int[][] output = new int[newWidth][newHeight];
        int x, y, index;
        int w = image.length;
        int h = image[0].length;
        float xRatio = (float)w/(float)newWidth;
        float yRatio = (float)h/(float)newHeight;
        int offset = 0;
        int[] aux = new int[5];
        int[][] neighbors = new int[4][4];
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                x = (int)(xRatio * i);
                y = (int)(yRatio * j);
                float fracX = (xRatio * i) - x;
                float fracY = (yRatio * j) - y;
                for (int m=0; m<4; m++) {
                    for (int n=0; n<4; n++) {
                        neighbors[m][n] = image[Math.min(w-1,Math.max(0,x+m-1))][Math.min(h-1,Math.max(0,y+n-1))];
                    }                    
                }

                // Running for blue
                float x1 = cubicPolate(neighbors[0][0] & 0xff, neighbors[1][0] & 0xff, neighbors[2][0] & 0xff, neighbors[3][0] & 0xff, fracX);
                float x2 = cubicPolate(neighbors[0][1] & 0xff, neighbors[1][1] & 0xff, neighbors[2][1] & 0xff, neighbors[3][1] & 0xff, fracX);
                float x3 = cubicPolate(neighbors[0][2] & 0xff, neighbors[1][2] & 0xff, neighbors[2][2] & 0xff, neighbors[3][2] & 0xff, fracX);
                float x4 = cubicPolate(neighbors[0][3] & 0xff, neighbors[1][3] & 0xff, neighbors[2][3] & 0xff, neighbors[3][3] & 0xff, fracX);
                float y1 = cubicPolate(x1, x2, x3, x4, fracY);

                // Running for green
                x1 = cubicPolate((neighbors[0][0]>>8)&0xff, (neighbors[1][0]>>8)&0xff, (neighbors[2][0]>>8)&0xff, (neighbors[3][0]>>8)&0xff, fracX);
                x2 = cubicPolate((neighbors[0][1]>>8)&0xff, (neighbors[1][1]>>8)&0xff, (neighbors[2][1]>>8)&0xff, (neighbors[3][1]>>8)&0xff, fracX);
                x3 = cubicPolate((neighbors[0][2]>>8)&0xff, (neighbors[1][2]>>8)&0xff, (neighbors[2][2]>>8)&0xff, (neighbors[3][2]>>8)&0xff, fracX);
                x4 = cubicPolate((neighbors[0][3]>>8)&0xff, (neighbors[1][3]>>8)&0xff, (neighbors[2][3]>>8)&0xff, (neighbors[3][3]>>8)&0xff, fracX);
                float y2 = cubicPolate(x1, x2, x3, x4, fracY);
                
                // Running for red
                x1 = cubicPolate((neighbors[0][0]>>16)&0xff, (neighbors[1][0]>>16)&0xff, (neighbors[2][0]>>16)&0xff, (neighbors[3][0]>>16)&0xff, fracX);
                x2 = cubicPolate((neighbors[0][1]>>16)&0xff, (neighbors[1][1]>>16)&0xff, (neighbors[2][1]>>16)&0xff, (neighbors[3][1]>>16)&0xff, fracX);
                x3 = cubicPolate((neighbors[0][2]>>16)&0xff, (neighbors[1][2]>>16)&0xff, (neighbors[2][2]>>16)&0xff, (neighbors[3][2]>>16)&0xff, fracX);
                x4 = cubicPolate((neighbors[0][3]>>16)&0xff, (neighbors[1][3]>>16)&0xff, (neighbors[2][3]>>16)&0xff, (neighbors[3][3]>>16)&0xff, fracX);
                float y3 = cubicPolate(x1, x2, x3, x4, fracY);

                int r = Math.max(0,Math.min((int)y3,255));
                int g = Math.max(0,Math.min((int)y2,255));
                int b = Math.max(0,Math.min((int)y1,255));

                output[i][j] = new Color(r, g, b).getRGB();
            }
        }
        return output;
    }

}