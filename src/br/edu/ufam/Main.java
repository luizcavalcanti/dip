package br.edu.ufam;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
        Set<Point> internalRegion = ConnectedRegionSearch.extractConnectedRegion(imgNoCentroids, centroids.get(0), 20);
        BufferedImage imgRegion = ImageIOUtils.getImageFromData(imgNoCentroids);
        Graphics g = imgRegion.getGraphics();
        g.setColor(Color.YELLOW);
        for (Point p : internalRegion) {
            g.fillRect((int) p.getX(), (int) p.getY(), 1, 1);
        }
        ImageIOUtils.savePNGImage(imgRegion, "output/boundaries");

        // Zoom 0.5x utilizando interpolação linear
        int[][] imgLand = ImageIOUtils.getImageData(ImageIOUtils.loadImageFromFile("landscape.png"));
        int[][] resizedImage = ImageTransformations.resize(imgLand, 640, 400);
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(resizedImage), "output/landscape-zoomed_out");

        // Zoom 1.5x utilizando interpolação linear
        int[][] imgRobotZoom = ImageIOUtils.getImageData(ImageIOUtils.loadImageFromFile("robot.jpg"));
        int[][] resizedImage2 = ImageTransformations.resize(imgRobotZoom, 450, 354);
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(resizedImage2), "output/robot-zoomed_in");

        // Região de imagem colorida
        int[][] imgDataLandscape = ImageIOUtils.getImageData(ImageIOUtils.loadImageFromFile("landscape.png"));
        internalRegion = ConnectedRegionSearch.extractConnectedRegion(imgDataLandscape, centroids.get(0), 20);
        BufferedImage imgRegionLand = ImageIOUtils.getImageFromData(imgDataLandscape);
        g = imgRegionLand.getGraphics();
        g.setColor(Color.YELLOW);
        for (Point p : internalRegion) {
            g.fillRect((int) p.getX(), (int) p.getY(), 1, 1);
        }
        ImageIOUtils.savePNGImage(imgRegionLand, "output/landscape-boundaries");

        // Região de imagem tons-de-cinza
        int[][] imgLandBW = ImageTransformations.convertToGrayscale(ImageIOUtils.getImageData(ImageIOUtils
                .loadImageFromFile("landscape.png")));
        internalRegion = ConnectedRegionSearch.extractConnectedRegion(imgLandBW, centroids.get(0), 20);
        BufferedImage imgRegionLandBW = ImageIOUtils.getImageFromData(imgLandBW);
        g = imgRegionLandBW.getGraphics();
        g.setColor(Color.YELLOW);
        for (Point p : internalRegion) {
            g.fillRect((int) p.getX(), (int) p.getY(), 1, 1);
        }
        ImageIOUtils.savePNGImage(imgRegionLandBW, "output/landscape-boundaries_bw");

        int[][] clipRobot = ImageTransformations.clip(imgNoCentroids, 75, 60, 40, 40);
        clipRobot = ImageTransformations.resize(clipRobot, 90, 90);
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(clipRobot), "output/robot_close_up");

        // Clip de imagem
        int[][] clip = ImageTransformations.clip(imgLandBW, 430, 370, 400, 170);
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(clip), "output/clip");

        // Extrair regiões da imagem
        extractAllRegions(imgDataLandscape, 30, "output/land_region_");

        // Aplicar janela de interesse
        int[][] mask = getVisibleMask(imgNoCentroids, 75, 60, 40, 40);
        int[][] regionOfInterest = ImageTransformations.regionOfInterest(imgNoCentroids, mask);
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(regionOfInterest), "output/regionOfInterest");

        // Aplicação de gamma nas imagens
        BufferedImage imgLandGamma = ImageIOUtils.loadImageFromFile("landscape.png");
        int[][] data = ImageTransformations.convertToGrayscale(ImageIOUtils.getImageData(imgLandGamma));
        int[][] filtro = ImageTransformations.gammaFilter(data, 0.6);
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(filtro), "output/gamma06");
        filtro = ImageTransformations.gammaFilter(data, 0.4);
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(filtro), "output/gamma04");
        filtro = ImageTransformations.gammaFilter(data, 0.3);
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(filtro), "output/gamma03");
    }

    private static int[][] getVisibleMask(int[][] image, int x, int y, int width, int height) {
        int[][] mask = new int[image.length][image[0].length];
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                mask[i][j] = 1;
            }
        }
        return mask;
    }

    private static void extractAllRegions(int[][] image, int threshold, String baseFileName) throws IOException {
        Set<Point> foundPixels = new HashSet<Point>();
        int width = image.length;
        int height = image[0].length;
        int regionCount = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Point current = new Point(x, y);
                if (foundPixels.contains(current))
                    continue;
                Set<Point> points = ConnectedRegionSearch.extractConnectedRegion(image, current, threshold);
                foundPixels.addAll(points);
                if (points.size() > 100) {
                    int[][] output = createWhiteImage(width, height);
                    for (Point p : points) {
                        output[(int) p.getX()][(int) p.getY()] = image[x][y];
                    }
                    ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(output), baseFileName + regionCount);
                    regionCount++;
                }
            }
        }
    }

    private static int[][] createWhiteImage(int width, int height) {
        int[][] output = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                output[i][j] = new Color(255, 255, 255).getRGB();
            }
        }
        return output;
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
