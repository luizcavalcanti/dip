package br.edu.ufam;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AStar {

    private static int WALKING_4N = 0;
    private static int WALKING_8N = 1;

    public static List<Point> get4NShortestPath(int[][] dados, Point partida, Point chegada) {
        return aStar(dados, partida, chegada, WALKING_4N);
    }

    public static List<Point> get8NShortestPath(int[][] dados, Point partida, Point chegada) {
        return aStar(dados, partida, chegada, WALKING_8N);
    }

    private static List<Point> aStar(int[][] imageData, Point start, Point end, int walkingMethod) {
        Map<Point, Double> gCost = new HashMap<Point, Double>();
        Map<Point, Double> fCost = new HashMap<Point, Double>();

        Set<Point> closed = new HashSet<Point>();
        Set<Point> open = new HashSet<Point>();
        Map<Point, Point> path = new HashMap<Point, Point>();

        open.add(start);
        gCost.put(start, 0D);
        fCost.put(start, calculateHeuristicCost(start, end));

        while (!open.isEmpty()) {
            // Pega candidato com menor custo
            Point currentPoint = null;
            Iterator<Point> it = open.iterator();
            while (it.hasNext()) {
                Point p = it.next();
                if (currentPoint == null || fCost.get(p) < fCost.get(currentPoint)) {
                    currentPoint = p;
                }
            }

            if (end.equals(currentPoint)) {
                return reconstructPath(end, path);
            }

            open.remove(currentPoint);
            closed.add(currentPoint);

            // Para cada vizinho...
            List<Point> neighbors = null;
            if (walkingMethod == WALKING_4N) {
                neighbors = list4Neighbors(imageData, currentPoint);
            } else {
                neighbors = list8Neighbors(imageData, currentPoint);
            }
            for (Point p : neighbors) {
                if (closed.contains(p)) {
                    continue;
                }
                boolean melhor = false;
                double g = gCost.get(currentPoint) + calculateNeighborCost(imageData, currentPoint, p);
                double h = calculateHeuristicCost(p, end);
                if (!open.contains(p)) {
                    open.add(p);
                    melhor = true;
                } else if (g < gCost.get(currentPoint)) {
                    melhor = true;
                }
                if (melhor) {
                    path.put(p, currentPoint);
                    gCost.put(p, g);
                    fCost.put(p, g + h);
                }
            }
        }
        throw new RuntimeException("Can't find a valid path from " + start + " to " + end);
    }

    private static List<Point> reconstructPath(Point p, Map<Point, Point> path) {
        List<Point> list = new ArrayList<Point>();
        list.add(p);
        if (path.containsKey(p)) {
            list.addAll(reconstructPath(path.get(p), path));
        }
        return list;
    }

    private static double calculateNeighborCost(int[][] imageData, Point p, Point q) {
        Color pPixel = new Color(imageData[(int) p.getX()][(int) p.getY()]);
        Color qPixel = new Color(imageData[(int) q.getX()][(int) q.getY()]);
        int rDiff = Math.abs(pPixel.getRed() - qPixel.getRed());
        int gDiff = Math.abs(pPixel.getGreen() - qPixel.getGreen());
        int bDiff = Math.abs(pPixel.getBlue() - qPixel.getBlue());
        return (rDiff + gDiff + bDiff) * 1000;
    }

    private static List<Point> list4Neighbors(int[][] dados, Point p) {
        List<Point> lista = new ArrayList<Point>();
        int x = (int) p.getX();
        int y = (int) p.getY();
        if (x + 1 < dados.length) {
            lista.add(new Point(x + 1, y));
        }
        if (y + 1 < dados[0].length) {
            lista.add(new Point(x, y + 1));
        }
        if (x > 0) {
            lista.add(new Point(x - 1, y));
        }
        if (y > 0) {
            lista.add(new Point(x, y - 1));
        }
        return lista;
    }

    private static List<Point> list8Neighbors(int[][] dados, Point p) {
        List<Point> lista = list4Neighbors(dados, p);
        int x = (int) p.getX();
        int y = (int) p.getY();
        if (x + 1 < dados.length && y + 1 < dados[0].length) {
            lista.add(new Point(x + 1, y + 1));
        }
        if (x > 0 && y + 1 < dados[0].length) {
            lista.add(new Point(x - 1, y + 1));
        }
        if (x > 0 && y > 0) {
            lista.add(new Point(x - 1, y - 1));
        }
        if (x + 1 < dados.length && y > 0) {
            lista.add(new Point(x + 1, y - 1));
        }
        return lista;
    }

    private static double calculateHeuristicCost(Point p, Point q) {
        return Math.sqrt(Math.pow(p.getX() - q.getX(), 2) + Math.pow(p.getY() - q.getY(), 2));
    }

}