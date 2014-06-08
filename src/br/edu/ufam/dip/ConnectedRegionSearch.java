package br.edu.ufam.dip;

import java.awt.Color;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import br.edu.ufam.ImageIOUtils;

public class ConnectedRegionSearch {

    public static void main(String[] args) throws IOException {
        if (args.length != 5) {
            System.err.println("Usage: ConnectedRegionSearch <source-image> <dest-image> startX startY threshold");
            System.err.println("example:");
            System.err.println("          ConnectedRegionSearch source.png dest.png 12 45 30");
            System.exit(1);
        }
        String origPath = args[0];
        String destPath = args[1];
        int x = Integer.parseInt(args[2]);
        int y = Integer.parseInt(args[3]);
        int threshold = Integer.parseInt(args[4]);

        BufferedImage image = ImageIOUtils.loadImageFromFile(origPath);
        int[][] imageData = ImageIOUtils.getImageData(image);
        int[][] output = new int[imageData.length][imageData[0].length];

        Set<Point> points = getConnectedNeighbors(imageData, new Point(x,y), threshold);

        for (Point p : points) {
            output[(int)p.getX()][(int)p.getY()] = Color.WHITE.getRGB();
        }

        ImageIO.write(ImageIOUtils.getImageFromData(output), "png", new File(destPath));
    }

    public static Set<Point> getConnectedNeighbors(int[][] img, Point startPoint, int threshold) {
        Set<Point> connectedRegion = new HashSet<Point>();
        Set<Point> open = new HashSet<Point>();
        Set<Point> closed = new HashSet<Point>();

        open.add(startPoint);
        connectedRegion.add(startPoint);

        int x = 0;
        int y = 0;
        while (!open.isEmpty()) {
            Point p = open.iterator().next();
            open.remove(p);
            closed.add(p);

            x = (int) p.getX();
            y = (int) p.getY();

            int currentPixel = img[x][y];

            if (x > 0) {
                Point left = new Point(x - 1, y);
                if (closeEnough(currentPixel, img[x - 1][y], threshold) && !open.contains(left)
                        && !closed.contains(left)) {
                    open.add(left);
                    connectedRegion.add(left);
                }
            }

            if (y > 0) {
                Point up = new Point(x, y - 1);
                if (closeEnough(currentPixel, img[x][y - 1], threshold) && !open.contains(up) && !closed.contains(up)) {
                    open.add(up);
                    connectedRegion.add(up);
                }
            }

            if (x + 1 < img.length) {
                Point right = new Point(x + 1, y);
                if (closeEnough(currentPixel, img[x + 1][y], threshold) && !open.contains(right)
                        && !closed.contains(right)) {
                    open.add(right);
                    connectedRegion.add(right);
                }
            }

            if (y + 1 < img[0].length) {
                Point down = new Point(x, y + 1);
                if (closeEnough(currentPixel, img[x][y + 1], threshold) && !open.contains(down)
                        && !closed.contains(down)) {
                    open.add(down);
                    connectedRegion.add(down);
                }
            }
        }
        return connectedRegion;
    }

    private static boolean closeEnough(int aPixel, int bPixel, int threshold) {
        Color aColor = new Color(aPixel);
        Color bColor = new Color(bPixel);
        int rDiff = Math.abs(aColor.getRed() - bColor.getRed());
        int gDiff = Math.abs(aColor.getGreen() - bColor.getGreen());
        int bDiff = Math.abs(aColor.getBlue() - bColor.getBlue());
        return (rDiff + gDiff + bDiff) <= threshold;
    }
}
