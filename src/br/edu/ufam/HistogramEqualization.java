package br.edu.ufam;

import java.awt.Color;

public class HistogramEqualization {

    public static int[][] equalizeHistogram(int[][] imageData, int bucketCount, int min, int max) {
        float range = (max - min) + 1;
        double bucketSize = Math.ceil(range / bucketCount);
        int[][] out = new int[imageData.length][imageData[0].length];
        float[] hist = buildGrayscaleHistogram(imageData, bucketCount, min, max);

        for (int i = 0; i < imageData.length; i++) {
            for (int j = 0; j < imageData[0].length; j++) {
                int pixVal = new Color(imageData[i][j]).getRed();
                int binLoc = (int) ((pixVal - min) / bucketSize);
                if (binLoc == bucketCount)
                    binLoc--;

                float cdf = 0.f;
                for (int k = 0; k <= binLoc; k++) {
                    cdf += hist[k];
                }
                int newColor = Math.round(cdf * (max - min + min));
                out[i][j] = new Color(newColor, newColor, newColor).getRGB();
            }
        }
        return out;
    }

    private static float[] buildGrayscaleHistogram(int[][] imageData, int bucketCount, int min, int max) {
        int[] aux = new int[bucketCount];
        float range = (max - min) + 1.f;
        double bucketSize = Math.ceil(range / bucketCount);

        int cont = 0;
        for (int i = 0; i < imageData.length; i++) {
            for (int j = 0; j < imageData[0].length; j++) {
                int pixVal = new Color(imageData[i][j]).getRed();
                int bucket = (int) ((pixVal - min) / bucketSize);
                if (bucket == bucketCount)
                    bucket--;
                aux[bucket] += 1;
                cont++;
            }
        }

        float[] hist = new float[bucketCount];

        for (int i = 0; i < bucketCount; i++) {
            hist[i] = ((float) aux[i] / (float) cont);
        }

        return hist;
    }
}
