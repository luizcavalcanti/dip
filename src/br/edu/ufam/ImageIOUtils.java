package br.edu.ufam;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class ImageIOUtils {

    public static BufferedImage loadImageFromFile(String path) throws IOException {
        return ImageIO.read(new File(path));
    }

    public static void savePNGImage(BufferedImage image, String pathWithoutExtension) throws IOException {
        ImageIO.write(image, "png", new File(pathWithoutExtension + ".png"));
    }

    public static void saveJPEGImage(BufferedImage image, String pathWithoutExtension) throws IOException {
        ImageIO.write(image, "jpg", new File(pathWithoutExtension + ".jpg"));
    }

    public static int[][] getImageData(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] imageData = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                imageData[x][y] = image.getRGB(x, y);
            }
        }
        return imageData;
    }

    public static BufferedImage getImageFromData(int[][] imageData) {
        BufferedImage image = new BufferedImage(imageData.length, imageData[0].length, ColorSpace.TYPE_RGB);
        int width = image.getWidth();
        int height = image.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, imageData[x][y]);
            }
        }
        return image;
    }

    public static int[][] cloneImageData(int[][] source) {
        int[][] clone = new int[source.length][source[0].length];
        for (int i = 0; i < source.length; i++) {
            clone[i] = Arrays.copyOf(source[i], source[i].length);
        }
        return clone;
    }

}
