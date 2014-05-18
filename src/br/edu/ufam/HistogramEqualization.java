package br.edu.ufam;

public class HistogramEqualization {

    public static int[][] equalizarHistograma(int[][] imagem, int baldes, int min, int max) {
        float intervalo = (max - min) + 1;
        double tamanhoBalde = Math.ceil(intervalo / baldes);
        int[][] saida = new int[imagem.length][imagem[0].length];
        float[] histograma = construirHistograma(imagem, baldes, min, max);

        for (int i = 0; i < imagem.length; i++) {
            for (int j = 0; j < imagem[0].length; j++) {
                int pixVal = imagem[i][j];
                int binLoc = (int) ((pixVal - min) / tamanhoBalde);
                if (binLoc == baldes)
                    binLoc--;

                float cdf = 0.f;
                for (int k = 0; k <= binLoc; k++) {
                    cdf += histograma[k];
                }
                float novoPixel = Math.round(cdf * (max - min + min));
                saida[i][j] = (int) novoPixel;
            }
        }
        return saida;
    }

    private static float[] construirHistograma(int[][] imagem, int baldes, int min, int max) {
        int[] aux = new int[baldes];
        float intervalo = (max - min) + 1.f;
        double tamanhoBalde = Math.ceil(intervalo / baldes);

        int cont = 0;
        for (int i = 0; i < imagem.length; i++) {
            for (int j = 0; j < imagem[0].length; j++) {
                int pixVal = imagem[i][j];
                int indice = (int) ((pixVal - min) / tamanhoBalde);
                if (indice == baldes)
                    indice--;
                aux[indice] += 1;
                cont++;
            }
        }

        float[] hist = new float[baldes];

        for (int i = 0; i < baldes; i++) {
            hist[i] = ((float) aux[i] / (float) cont);
        }

        return hist;
    }
}
