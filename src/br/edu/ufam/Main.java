package br.edu.ufam;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedImage imgRobot = ImageIOUtils.loadImageFromFile("robot.jpg");

        // Criação de imagem negativa
        int[][] imgDataGrayscale = ImageTransformations.convertToGrayscale(ImageIOUtils.getImageData(imgRobot));
        int[][] imgNegative = ImageTransformations.convertToNegative(imgDataGrayscale);
        ImageIOUtils.saveJPEGImage(ImageIOUtils.getImageFromData(imgNegative), "output/negative");

        // Encontra os dois centroides da imagem
        List<Point> centroids = findCentroids(imgNegative);
        if (centroids.size() != 2) {
            System.err.println("Could not find both centroids in image");
            System.exit(1);
        }
        int[][] imgNoCentroids = ImageIOUtils.cloneImageData(imgNegative);
        for (Point p : centroids) {
            eraseCentroid(imgNoCentroids, (int) p.getX(), (int) p.getY());
        }
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(imgNoCentroids), "output/negative-no_centroid");

        // Busca caminho mínimo com 4 vizinhos
        List<Point> path4N = AStar.get4NShortestPath(imgNoCentroids, centroids.get(0), centroids.get(1));
        int[][] data4N = ImageIOUtils.cloneImageData(imgNoCentroids);
        for (Point p : path4N) {
            int x = (int) p.getX();
            int y = (int) p.getY();
            data4N[x][y] = 255;
        }
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(data4N), "output/4Npath");

        // Busca caminho mínimo com 8 vizinhos
        List<Point> path8N = AStar.get8NShortestPath(imgNoCentroids, centroids.get(0), centroids.get(1));
        int[][] data8N = ImageIOUtils.cloneImageData(imgNoCentroids);
        for (Point p : path8N) {
            int x = (int) p.getX();
            int y = (int) p.getY();
            data8N[x][y] = 255;
        }
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(data8N), "output/8Npath");

        // Negative image => histogram equalization
        int[][] dadosEqualizado = HistogramEqualization.equalizeHistogram(imgNegative, 255, 0, 255);
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(dadosEqualizado), "output/equalized");

        // Get the connected region starting from the first centroid
        Set<Point> internalRegion = ConnectedRegionSearch.extractConnectedRegion(imgNoCentroids, centroids.get(0));
        BufferedImage imgRegion = ImageIOUtils.getImageFromData(imgNoCentroids);
        Graphics g = imgRegion.getGraphics();
        g.setColor(Color.YELLOW);
        for (Point p : internalRegion) {
            g.fillRect((int) p.getX(), (int) p.getY(), 1, 1);
        }
        ImageIOUtils.savePNGImage(imgRegion, "output/boundaries");

        // Zoom 0.5x utilizando interpolação linear
        int[][] imgLandBW2 = ImageIOUtils.getImageData(ImageIOUtils.loadImageFromFile("landscape.png"));
        int[][] resizedImage = ImageTransformations.resize(imgLandBW2, 640, 400);
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(resizedImage), "output/landscape-zoomed_out");

        // Zoom 1.5x utilizando interpolação linear
        int[][] imgRobotZoom = ImageIOUtils.getImageData(ImageIOUtils.loadImageFromFile("robot.jpg"));
        int[][] resizedImage2 = ImageTransformations.resize(imgRobotZoom, 450, 354);
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(resizedImage2), "output/robot-zoomed_in");

        // Região de imagem colorida
        int[][] imgDataLandscape = ImageIOUtils.getImageData(ImageIOUtils.loadImageFromFile("landscape.png"));
        internalRegion = ConnectedRegionSearch.extractConnectedRegion(imgDataLandscape, centroids.get(0));
        BufferedImage imgRegionLand = ImageIOUtils.getImageFromData(imgDataLandscape);
        g = imgRegionLand.getGraphics();
        g.setColor(Color.YELLOW);
        for (Point p : internalRegion) {
            g.fillRect((int) p.getX(), (int) p.getY(), 1, 1);
        }
        ImageIOUtils.savePNGImage(imgRegionLand, "output/landscape-boundaries_bw");

        // Região de imagem tons-de-cinza
        int[][] imgLandBW = ImageTransformations.convertToGrayscale(ImageIOUtils.getImageData(ImageIOUtils
                .loadImageFromFile("landscape.png")));
        internalRegion = ConnectedRegionSearch.extractConnectedRegion(imgLandBW, centroids.get(0));
        BufferedImage imgRegionLandBW = ImageIOUtils.getImageFromData(imgLandBW);
        g = imgRegionLandBW.getGraphics();
        g.setColor(Color.YELLOW);
        for (Point p : internalRegion) {
            g.fillRect((int) p.getX(), (int) p.getY(), 1, 1);
        }
        ImageIOUtils.savePNGImage(imgRegionLandBW, "output/landscape-boundaries_bw");

        // TODO aplicar janela de interesse (produto de kronecher)
        int[][] imgInterestRegion = applyInterestWindow(ImageIOUtils.cloneImageData(imgNoCentroids));
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(imgInterestRegion), "interest_window");

    }

    private static int[][] applyInterestWindow(int[][] image) {
        int m = image.length;
        int n = image[0].length;
        int[][] out = new int[m * m][n * n];
        int[][] mask = new int[m][n];
        KroneckerMatrix.product(image, mask, out);
        return out;
    }

    private static void eraseCentroid(int[][] img, int x, int y) {
        for (int i = x - 6; i < x + 6; i++) {
            for (int j = y - 6; j < y + 6; j++) {
                img[i][j] = 0;
            }
        }
    }

    private static List<Point> findCentroids(int[][] img) {
        List<Point> centroides = new ArrayList<Point>();
        for (int x = 0; x < img.length; x++) {
            for (int y = 0; y < img[x].length; y++) {
                // Despreza as bordas da imagem
                if (x <= 0 || y <= 0 || x + 1 == img.length || y + 1 == img[x].length) {
                    continue;
                }
                // Busca na vizinhança 4 se o o ponto atual é um centroide
                if ((img[x][y] & 0xFF) < 5 && (img[x - 1][y] & 0xFF) >= 250 && (img[x][y + 1] & 0xFF) >= 250
                        && (img[x + 1][y] & 0xFF) >= 250 && (img[x][y - 1] & 0xFF) >= 250) {
                    centroides.add(new Point(x, y));
                }
            }
        }
        return centroides;
    }

}
