package br.edu.ufam;

import java.awt.Color;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class ConnectedRegionSearch {

    public static Set<Point> extractConnectedRegion(int[][] imageData, Point origin) {
        return searchForConnectedNeighbors(imageData, origin);
    }

    private static Set<Point> searchForConnectedNeighbors(int[][] img, Point startPoint) {
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
                if (closeEnough(currentPixel, img[x - 1][y]) && !open.contains(left) && !closed.contains(left)) {
                    open.add(left);
                    connectedRegion.add(left);
                }
            }

            if (y > 0) {
                Point up = new Point(x, y - 1);
                if (closeEnough(currentPixel, img[x][y - 1]) && !open.contains(up) && !closed.contains(up)) {
                    open.add(up);
                    connectedRegion.add(up);
                }
            }

            if (x + 1 < img.length) {
                Point right = new Point(x + 1, y);
                if (closeEnough(currentPixel, img[x + 1][y]) && !open.contains(right) && !closed.contains(right)) {
                    open.add(right);
                    connectedRegion.add(right);
                }
            }

            if (y + 1 < img[0].length) {
                Point down = new Point(x, y + 1);
                if (closeEnough(currentPixel, img[x][y + 1]) && !open.contains(down) && !closed.contains(down)) {
                    open.add(down);
                    connectedRegion.add(down);
                }
            }
        }
        return connectedRegion;
    }

    private static boolean closeEnough(int aPixel, int bPixel) {
        Color aColor = new Color(aPixel);
        Color bColor = new Color(bPixel);
        int rDiff = Math.abs(aColor.getRed() - bColor.getRed());
        int gDiff = Math.abs(aColor.getGreen() - bColor.getGreen());
        int bDiff = Math.abs(aColor.getBlue() - bColor.getBlue());
        return (rDiff + gDiff + bDiff) < 20;
    }
}
