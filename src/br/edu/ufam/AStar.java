package br.edu.ufam;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AStar {

    public static List<Point> caminhoMinimo8N(int[][] dados, Point partida, Point chegada) {
        return caminhoMinimo(dados, partida, chegada, false);
    }

    public static List<Point> caminhoMinimo4N(int[][] dados, Point partida, Point chegada) {
        return caminhoMinimo(dados, partida, chegada, true);
    }

    private static List<Point> caminhoMinimo(int[][] dados, Point partida, Point chegada, boolean usar4N) {
        Map<Point, Double> custoG = new HashMap<Point, Double>();
        Map<Point, Double> custoF = new HashMap<Point, Double>();

        Set<Point> fechado = new HashSet<Point>();
        Set<Point> aberto = new HashSet<Point>();
        Map<Point, Point> caminho = new HashMap<Point, Point>();

        aberto.add(partida);
        custoG.put(partida, 0D);
        custoF.put(partida, calcularCustoHeuristico(partida, chegada));

        while (!aberto.isEmpty()) {
            // Pega candidato com menor custo
            Point pontoCorrente = null;
            Iterator<Point> it = aberto.iterator();
            while (it.hasNext()) {
                Point p = it.next();
                if (pontoCorrente == null || custoF.get(p) < custoF.get(pontoCorrente)) {
                    pontoCorrente = p;
                }
            }

            if (chegada.equals(pontoCorrente)) {
                return reconstruirCaminho(chegada, caminho);
            }

            aberto.remove(pontoCorrente);
            fechado.add(pontoCorrente);

            // Para cada vizinho...
            List<Point> vizinhos = null;
            if (usar4N) {
                vizinhos = listarVizinhos4N(dados, pontoCorrente);
            } else {
                vizinhos = listarVizinhos8N(dados, pontoCorrente);
            }
            for (Point p : vizinhos) {
                if (fechado.contains(p)) {
                    continue;
                }
                boolean melhor = false;
                double g = custoG.get(pontoCorrente) + calcularCustoVizinhos(dados, pontoCorrente, p);
                double h = calcularCustoHeuristico(p, chegada);
                if (!aberto.contains(p)) {
                    aberto.add(p);
                    melhor = true;
                } else if (g < custoG.get(pontoCorrente)) {
                    melhor = true;
                }
                if (melhor) {
                    caminho.put(p, pontoCorrente);
                    custoG.put(p, g);
                    custoF.put(p, g + h);
                }
            }
        }
        throw new RuntimeException("Não foi possível encontrar um caminho");
    }

    private static List<Point> reconstruirCaminho(Point p, Map<Point, Point> caminho) {
        List<Point> list = new ArrayList<Point>();
        list.add(p);
        if (caminho.containsKey(p)) {
            list.addAll(reconstruirCaminho(caminho.get(p), caminho));
        }
        return list;
    }

    private static double calcularCustoVizinhos(int[][] dados, Point p, Point q) {
        int valorP = dados[(int) p.getX()][(int) p.getY()];
        int valorQ = dados[(int) q.getX()][(int) q.getY()];
        return (Math.abs(valorP - valorQ) + 1) * 1000;
    }

    private static List<Point> listarVizinhos4N(int[][] dados, Point p) {
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

    private static List<Point> listarVizinhos8N(int[][] dados, Point p) {
        List<Point> lista = listarVizinhos4N(dados, p);
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

    private static double calcularCustoHeuristico(Point p, Point q) {
        return Math.sqrt(Math.pow(p.getX() - q.getX(), 2) + Math.pow(p.getY() - q.getY(), 2));
    }

}