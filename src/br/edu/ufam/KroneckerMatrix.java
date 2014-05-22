package br.edu.ufam;

public class KroneckerMatrix {

    public static void product(double[][] a, double[][] b, double[][] out) {
        final int m = a.length;
        final int n = a[0].length;
        final int p = b.length;
        final int q = b[0].length;

        if (out == null || out.length != m * p || out[0].length != n * q) {
            throw new RuntimeException("Invalid dimensions for kronecker product");
        }

        for (int i = 0; i < m; i++) {
            final int iOffset = i * p;
            for (int j = 0; j < n; j++) {
                final int jOffset = j * q;
                final double aij = a[i][j];

                for (int k = 0; k < p; k++) {
                    for (int l = 0; l < q; l++) {
                        out[iOffset + k][jOffset + l] = aij * b[k][l];
                    }
                }

            }
        }
    }

}
