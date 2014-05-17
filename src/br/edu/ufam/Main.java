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
        int[][] dadosImagem = converterImagemParaVetor(img);
        int[][] dadosNegativo = criarNegativo(dadosImagem);
        salvarImagemEmJPEG(converterVetorEmImagem(dadosNegativo), "negativo.jpg");
        List<Point> centroides = encontrarCentroides(dadosImagem, Color.white.getRGB(), Color.black.getRGB());
        System.out.println("centroides: " + centroides.size());
        // TODO eliminar o circulo ao redor dos centroides
        // TODO calcular caminhamento minimo para 4-n
        // TODO calcular caminhamento minimo para 8-n
        // TODO extrair a borda interna do background (piso)
        // TODO aplicar janela de interesse (produto de kronecher) e fazer zoom usando interpolacao bilinear
        // TODO implementar a equalizacao da imagem
    }

    private static List<Point> encontrarCentroides(int[][] dados, int corCentroide, int corEntorno) {
        List<Point> centroides = new ArrayList<Point>();
        for (int x = 0; x < dados.length; x++) {
            for (int y = 0; y < dados[x].length; y++) {
                // Desprezar as bordas da imagem
                if (x <= 0 || y <= 0 || x+1 == dados.length || y+1==dados[x].length) {
                    continue;
                }
                // Busca na vizinhança 4 se o o ponto atual é um centroide
                boolean ehCentroide = dados[x][y] == corCentroide && dados[x-1][y] == corEntorno && dados[x][y+1] == corEntorno
                        && dados[x+1][y] == corEntorno && dados[x][y-1] == corEntorno;
                if (ehCentroide) {
                    centroides.add(new Point(x, y));
                }
            }
        }
        return centroides;
    }

    private static BufferedImage carregarImagem(String caminhoImagem) throws IOException {
        return ImageIO.read(new File(caminhoImagem));
    }

    private static void salvarImagemEmJPEG(BufferedImage imgNegativo, String caminho) throws IOException {
        ImageIO.write(imgNegativo, "jpg", new File(caminho));
    }

    private static int[][] converterImagemParaVetor(BufferedImage img) {
        int largura = img.getWidth();
        int altura = img.getHeight();
        int[][] dados = new int[largura][altura];
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < altura; y++) {
                dados[x][y] = img.getRGB(x, y);
            }
        }
        return dados;
    }

    private static BufferedImage converterVetorEmImagem(int[][] dados) {
        BufferedImage retorno = new BufferedImage(dados.length, dados[0].length, ColorSpace.TYPE_RGB);
        int largura = retorno.getWidth();
        int altura = retorno.getHeight();
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < altura; y++) {
                retorno.setRGB(x, y, dados[x][y]);
            }
        }
        return retorno;
    }

    private static int[][] criarNegativo(int[][] dados) {
        int[][] negativo = new int[dados.length][dados[0].length];
        for (int x = 0; x < negativo.length; x++) {
            for (int y = 0; y < negativo[x].length; y++) {
                Color c = new Color(dados[x][y]);
                c = new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
                negativo[x][y] = c.getRGB();
            }
        }
        return negativo;
    }

}
