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

        int[][] imgData = ImageIOUtils.getImageData(imgRobot);
        int[][] imgDataGrayscale = ImageTransformations.convertToGrayscale(imgData);

        // Criação de imagem negativa
        int[][] imgNegative = ImageTransformations.convertToNegative(imgDataGrayscale);
        ImageIOUtils.saveJPEGImage(ImageIOUtils.getImageFromData(imgNegative), "negative");

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
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(imgNoCentroids), "negative-no_centroid");

        // Busca caminho mínimo com 4 vizinhos
        List<Point> path4N = AStar.get4NShortestPath(imgNoCentroids, centroids.get(0), centroids.get(1));
        int[][] data4N = ImageIOUtils.cloneImageData(imgNoCentroids);
        for (Point p : path4N) {
            int x = (int) p.getX();
            int y = (int) p.getY();
            data4N[x][y] = 255;
        }
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(data4N), "4Npath");

        // Busca caminho mínimo com 8 vizinhos
        List<Point> path8N = AStar.get8NShortestPath(imgNoCentroids, centroids.get(0), centroids.get(1));
        int[][] data8N = ImageIOUtils.cloneImageData(imgNoCentroids);
        for (Point p : path8N) {
            int x = (int) p.getX();
            int y = (int) p.getY();
            data8N[x][y] = 255;
        }
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(data8N), "8Npath");

        // Negative image => histogram equalization
        int[][] dadosEqualizado = HistogramEqualization.equalizeHistogram(imgNegative, 255, 0, 255);
        ImageIOUtils.savePNGImage(ImageIOUtils.getImageFromData(dadosEqualizado), "equalized");

        // Get the connected region starting from the first centroid
        Set<Point> bordaInterna = ConnectedRegionSearch.extractConnectedRegion(imgNoCentroids, centroids.get(0));
        BufferedImage imgRegion = ImageIOUtils.getImageFromData(imgNoCentroids);
        Graphics g = imgRegion.getGraphics();
        g.setColor(Color.YELLOW);
        System.out.println(bordaInterna.size());
        for (Point p : bordaInterna) {
            g.fillRect((int) p.getX(), (int) p.getY(), 1, 1);
        }
        ImageIOUtils.savePNGImage(imgRegion, "boundaries");

        // TODO aplicar janela de interesse (produto de kronecher)
        // TODO fazer zoom usando interpolacao bilinear

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
