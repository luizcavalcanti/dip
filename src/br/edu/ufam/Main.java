package br.edu.ufam;

import java.awt.Color;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedImage img = carregarImagem("original.jpg");
        int[][] dadosImagem = converterImagemParaVetorCinza(img);

        int[][] dadosNegativo = criarNegativo(dadosImagem);
        List<Point> centroides = encontrarCentroides(dadosNegativo);
        System.out.println("centroides: " + centroides.size());
        for (Point p : centroides) {
            System.out.println(p.getX() + "," + p.getY());
            eliminarCirculo(dadosNegativo, (int) p.getX(), (int) p.getY());
        }

        // Busca caminho mínimo com 4 vizinhos
        List<Point> caminho4 = AStar.caminhoMinimo4N(dadosNegativo, centroides.get(0), centroides.get(1));
        int[][] dados4N = dadosNegativo.clone();
        for (Point p : caminho4) {
            int x = (int) p.getX();
            int y = (int) p.getY();
            dados4N[x][y] = 255;
        }
        salvarImagemEmPNG(converterVetorCinzaEmImagem(dados4N), "caminho4N.png");

        // Busca caminho mínimo com 8 vizinhos
        List<Point> caminho8 = AStar.caminhoMinimo8N(dadosNegativo, centroides.get(0), centroides.get(1));
        int[][] dados8N = dadosNegativo.clone();
        for (Point p : caminho8) {
            int x = (int) p.getX();
            int y = (int) p.getY();
            dados8N[x][y] = 255;
        }
        salvarImagemEmPNG(converterVetorCinzaEmImagem(dados8N), "caminho8N.png");

        // TODO extrair a borda interna do background (piso)
        // TODO aplicar janela de interesse (produto de kronecher) e fazer zoom usando interpolacao bilinear
        // TODO implementar a equalizacao da imagem
    }

    private static void eliminarCirculo(int[][] img, int x, int y) {
        // aplica um patch de 12x12 pixels pretos a partir do centro
        for (int i = x - 6; i < x + 6; i++) {
            for (int j = y - 6; j < y + 6; j++) {
                img[i][j] = 0;
            }
        }
    }

    private static List<Point> encontrarCentroides(int[][] img) {
        List<Point> centroides = new ArrayList<Point>();
        for (int x = 0; x < img.length; x++) {
            for (int y = 0; y < img[x].length; y++) {
                // Despreza as bordas da imagem
                if (x <= 0 || y <= 0 || x + 1 == img.length || y + 1 == img[x].length) {
                    continue;
                }
                // Busca na vizinhança 4 se o o ponto atual é um centroide
                if (img[x][y] < 5 && img[x - 1][y] >= 250 && img[x][y + 1] >= 250 && img[x + 1][y] >= 250
                        && img[x][y - 1] >= 250) {
                    centroides.add(new Point(x, y));
                }
            }
        }
        return centroides;
    }

    private static BufferedImage carregarImagem(String caminhoImagem) throws IOException {
        return ImageIO.read(new File(caminhoImagem));
    }

    private static void salvarImagemEmPNG(BufferedImage imgNegativo, String caminho) throws IOException {
        ImageIO.write(imgNegativo, "png", new File(caminho));
    }

    private static int[][] converterImagemParaVetorCinza(BufferedImage img) {
        int largura = img.getWidth();
        int altura = img.getHeight();
        int[][] dados = new int[largura][altura];
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < altura; y++) {
                Color c = new Color(img.getRGB(x, y));
                dados[x][y] = (c.getRed() + c.getBlue() + c.getGreen()) / 3;
            }
        }
        return dados;
    }

    private static BufferedImage converterVetorCinzaEmImagem(int[][] dados) {
        BufferedImage retorno = new BufferedImage(dados.length, dados[0].length, ColorSpace.TYPE_RGB);
        int largura = retorno.getWidth();
        int altura = retorno.getHeight();
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < altura; y++) {
                int cinza = dados[x][y];
                retorno.setRGB(x, y, new Color(cinza, cinza, cinza).getRGB());
            }
        }
        return retorno;
    }

    private static int[][] criarNegativo(int[][] dados) {
        int[][] negativo = new int[dados.length][dados[0].length];
        for (int x = 0; x < negativo.length; x++) {
            for (int y = 0; y < negativo[x].length; y++) {
                negativo[x][y] = 255 - dados[x][y];
            }
        }
        return negativo;
    }

}
