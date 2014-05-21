package br.edu.ufam;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

public class Main {

    public static void main(String[] args) throws IOException {
        // Carrega a imagem do disco
        BufferedImage img = carregarImagem("original.jpg");

        // Converte imagem para tons de cinza
        int[][] imgCinza = converterImagemParaVetorCinza(img);

        // Criação de imagem negativa
        int[][] imgNegativo = criarNegativo(imgCinza);
        salvarImagemEmPNG(converterVetorCinzaEmImagem(imgNegativo), "negativo.png");

        // Encontra os dois centroides da imagem
        List<Point> centroides = encontrarCentroides(imgNegativo);
        if (centroides.size() != 2) {
            System.err.println("Não foi possível encontrar dois centróides na imagem");
            System.exit(1);
        }
        int[][] imgSemCentroides = copiarVetor(imgNegativo);
        for (Point p : centroides) {
            eliminarCirculo(imgSemCentroides, (int) p.getX(), (int) p.getY());
        }
        salvarImagemEmPNG(converterVetorCinzaEmImagem(imgNegativo), "negativo-sem_centroides.png");

        // Busca caminho mínimo com 4 vizinhos
        List<Point> caminho4 = AStar.caminhoMinimo4N(imgSemCentroides, centroides.get(0), centroides.get(1));
        int[][] dados4N = copiarVetor(imgSemCentroides);
        for (Point p : caminho4) {
            int x = (int) p.getX();
            int y = (int) p.getY();
            dados4N[x][y] = 255;
        }
        salvarImagemEmPNG(converterVetorCinzaEmImagem(dados4N), "caminho4N.png");

        // Busca caminho mínimo com 8 vizinhos
        List<Point> caminho8 = AStar.caminhoMinimo8N(imgSemCentroides, centroides.get(0), centroides.get(1));
        int[][] dados8N = copiarVetor(imgSemCentroides);
        for (Point p : caminho8) {
            int x = (int) p.getX();
            int y = (int) p.getY();
            dados8N[x][y] = 255;
        }
        salvarImagemEmPNG(converterVetorCinzaEmImagem(dados8N), "caminho8N.png");

        // Equalização da imagem
        int[][] dadosEqualizado = HistogramEqualization.equalizarHistograma(imgNegativo, 255, 0, 255);
        salvarImagemEmPNG(converterVetorCinzaEmImagem(dadosEqualizado), "equalizado.png");

        // Extrai borda interna da imagem a partir do primeiro centróide
        Set<Point> bordaInterna = ConnectedRegionSearch.extractConnectedRegion(imgSemCentroides, centroides.get(0));
        BufferedImage imgBordas = converterVetorCinzaEmImagem(imgSemCentroides);
        Graphics g = imgBordas.getGraphics();
        g.setColor(Color.YELLOW);
        System.out.println(bordaInterna.size());
        for (Point p : bordaInterna) {
            g.fillRect((int) p.getX(), (int) p.getY(), 1, 1);
        }
        salvarImagemEmPNG(imgBordas, "bordas.png");

        // TODO aplicar janela de interesse (produto de kronecher) e fazer zoom usando interpolacao bilinear

    }

    private static int[][] copiarVetor(int[][] vetor) {
        int[][] copia = new int[vetor.length][vetor[0].length];
        for (int i = 0; i < vetor.length; i++) {
            copia[i] = Arrays.copyOf(vetor[i], vetor.length);
        }
        return copia;
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
