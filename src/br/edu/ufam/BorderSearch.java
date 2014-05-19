package br.edu.ufam;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class BorderSearch {

    public static List<Point> extrairBordaInterna(int[][] imagem, Point origem) {
        List<Point> retorno = new ArrayList<Point>();
        int[][] bordas = new int[imagem.length][imagem[0].length];
        int[][] buscados = new int[imagem.length][imagem[0].length];
        buscaVizinhos(imagem, (int) origem.getX(), (int) origem.getY(), bordas, buscados);
        for (int x = 0; x < bordas.length; x++) {
            for (int y = 0; y < bordas[0].length; y++) {
                if (bordas[x][y] == 1) {
                    System.out.println("achou");
                    retorno.add(new Point(x, y));
                }
            }
        }
        return retorno;
    }

    private static void buscaVizinhos(int[][] imagem, int x, int y, int[][] bordas, int[][] buscados) {
        int atual = imagem[x][y];

        if (buscados[x][y] == 1)
            return;

        buscados[x][y] = 1;

        if (buscados[x][y - 1] == 0 && y > 0) {
            if (imagem[x][y - 1] != atual) {
                bordas[x][y] = 1;
                return;
            }
        }

        if (buscados[x - 1][y] == 0 && x > 0) {
            if (imagem[x - 1][y] != atual) {
                bordas[x][y] = 1;
                return;
            }
        }

        if (buscados[x + 1][y] == 0 && x + 1 < imagem.length) {
            if (imagem[x + 1][y] != atual) {
                bordas[x][y] = 1;
                return;
            }
        }

        if (buscados[x][y + 1] == 0 && y + 1 < imagem[0].length) {
            if (imagem[x][y + 1] != atual) {
                bordas[x][y] = 1;
                return;
            }
        }
        buscaVizinhos(imagem, x, y - 1, bordas, buscados);
        buscaVizinhos(imagem, x - 1, y, bordas, buscados);
        buscaVizinhos(imagem, x + 1, y, bordas, buscados);
        buscaVizinhos(imagem, x, y + 1, bordas, buscados);
    }
}
