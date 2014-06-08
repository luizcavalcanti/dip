package br.edu.ufam.dip.segmentation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import br.edu.ufam.ImageIOUtils;

public class AdaptativeThreshold {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage:    AdaptativeThreshold <source-image> <dest-image>");
            System.err.println("example:  AdaptativeThreshold source.png dest.png");
            System.exit(1);
        }
        String origPath = args[0];
        String destPath = args[1];

        BufferedImage image = ImageIOUtils.loadImageFromFile(origPath);
        int[][] imageData = ImageIOUtils.getImageData(image);
        int[][] output = applySegmentation(imageData);
        ImageIO.write(ImageIOUtils.getImageFromData(output), "png", new File(destPath));
    }

    public static int[][] applySegmentation(int[][] imageData) {        
        int width = imageData.length;
        int height = imageData[0].length;
        int[] histogram = getHistogram(imageData);
        int threshold = getOtsuThreshold(histogram, width * height);
        System.out.println("[Otsu's Threshold] Threshold found: " + threshold);
        return SimpleThreshold.applySegmentation(imageData, threshold, -1);
    }

    private static int[] getHistogram(int[][] imageData) {
        int width = imageData.length;
        int height = imageData[0].length;
        int[] output = new int[256];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color c = new Color(imageData[x][y]);
                int grey = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                output[grey] += 1;
            }
        }
        return output;
    }

    private static int getOtsuThreshold(int[] histogram, int totalPixels) {
        float sum = 0;
        for(int i = 0; i < 256; i++) {
            sum += i * histogram[i];
        }

        float sumB = 0;
        int wB = 0;
        int wF = 0;
 
        float varMax = 0;
        int threshold = 0;
 
        for(int i = 0 ; i < 256 ; i++) {
            wB += histogram[i];
            if(wB == 0) continue;

            wF = totalPixels - wB;
            if(wF == 0) break;

            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if(varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }
        return threshold;
    }

}