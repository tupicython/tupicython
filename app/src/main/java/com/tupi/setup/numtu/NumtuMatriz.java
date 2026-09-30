package com.tupi.setup.numtu;

import java.util.List;

public class NumtuMatriz {

    public final double[][] dados;
    public final int linhas;
    public final int colunas;

    public NumtuMatriz(int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.dados = new double[linhas][colunas];
    }

    public NumtuMatriz(double[][] dados) {
        this.dados = dados;
        this.linhas = dados.length;
        this.colunas = dados.length > 0 ? dados[0].length : 0;
    }

	public int total() { return linhas * colunas; }
	
    // ============ CRIAÇÃO ============
    public static NumtuMatriz zeros(int l, int c) { return new NumtuMatriz(l, c); }

    public static NumtuMatriz uns(int l, int c) {
        NumtuMatriz m = new NumtuMatriz(l, c);
        for (int i = 0; i < l; i++)
            for (int j = 0; j < c; j++) m.dados[i][j] = 1.0;
        return m;
    }

    public static NumtuMatriz aleatorio(int l, int c, double escala) {
        NumtuMatriz m = new NumtuMatriz(l, c);
        for (int i = 0; i < l; i++)
            for (int j = 0; j < c; j++)
                m.dados[i][j] = (Math.random() * 2 - 1) * escala;
        return m;
    }

    public static NumtuMatriz aleatorioNormal(int l, int c, double escala) {
        NumtuMatriz m = new NumtuMatriz(l, c);
        for (int i = 0; i < l; i++)
            for (int j = 0; j < c; j++)
                m.dados[i][j] = gauss() * escala;
        return m;
    }

    public static NumtuMatriz identidade(int n) {
        NumtuMatriz m = new NumtuMatriz(n, n);
        for (int i = 0; i < n; i++) m.dados[i][i] = 1.0;
        return m;
    }

    public static NumtuMatriz criarDeLista(List<Object> linhasLista) {
        int l = linhasLista.size();
        int c = ((List<Object>) linhasLista.get(0)).size();
        double[][] d = new double[l][c];
        for (int i = 0; i < l; i++) {
            List<Object> linha = (List<Object>) linhasLista.get(i);
            for (int j = 0; j < c; j++)
                d[i][j] = ((Number) linha.get(j)).doubleValue();
        }
        return new NumtuMatriz(d);
    }

    private static double gauss() {
        double u1 = Math.random(), u2 = Math.random();
        return Math.sqrt(-2 * Math.log(u1 + 1e-12)) * Math.cos(2 * Math.PI * u2);
    }

    // ============ ACESSO ============
    public double obter(int i, int j) { return dados[i][j]; }
    public void definir(int i, int j, double v) { dados[i][j] = v; }
    public NumtuMatriz copia() {
        double[][] d = new double[linhas][colunas];
        for (int i = 0; i < linhas; i++) d[i] = dados[i].clone();
        return new NumtuMatriz(d);
    }

    // ============ ARITMÉTICA ============
    public NumtuMatriz somar(NumtuMatriz o) {
        double[][] r = new double[linhas][colunas];
        for (int i = 0; i < linhas; i++)
            for (int j = 0; j < colunas; j++)
                r[i][j] = dados[i][j] + o.dados[i][j];
        return new NumtuMatriz(r);
    }

    public NumtuMatriz subtrair(NumtuMatriz o) {
        double[][] r = new double[linhas][colunas];
        for (int i = 0; i < linhas; i++)
            for (int j = 0; j < colunas; j++)
                r[i][j] = dados[i][j] - o.dados[i][j];
        return new NumtuMatriz(r);
    }

    public NumtuMatriz multiplicar(double e) {
        double[][] r = new double[linhas][colunas];
        for (int i = 0; i < linhas; i++)
            for (int j = 0; j < colunas; j++)
                r[i][j] = dados[i][j] * e;
        return new NumtuMatriz(r);
    }

    public NumtuMatriz multiplicarElemento(NumtuMatriz o) {
        double[][] r = new double[linhas][colunas];
        for (int i = 0; i < linhas; i++)
            for (int j = 0; j < colunas; j++)
                r[i][j] = dados[i][j] * o.dados[i][j];
        return new NumtuMatriz(r);
    }

    // Somar vetor linha (broadcast) — cada linha ganha o vetor
    public NumtuMatriz somarVetor(NumtuVetor v) {
        double[][] r = new double[linhas][colunas];
        for (int i = 0; i < linhas; i++)
            for (int j = 0; j < colunas; j++)
                r[i][j] = dados[i][j] + v.dados[j];
        return new NumtuMatriz(r);
    }

    // ================= MATMUL — coração do Transformer =================
    public NumtuMatriz matmul(NumtuMatriz o) {
        if (colunas != o.linhas)
            throw new RuntimeException("matmul: dimensões incompatíveis (" +
									   linhas + "x" + colunas + " * " + o.linhas + "x" + o.colunas + ")");
        double[][] r = new double[linhas][o.colunas];
        for (int i = 0; i < linhas; i++) {
            for (int k = 0; k < colunas; k++) {
                double a = dados[i][k];
                if (a == 0) continue;
                for (int j = 0; j < o.colunas; j++)
                    r[i][j] += a * o.dados[k][j];
            }
        }
        return new NumtuMatriz(r);
    }

    public NumtuVetor matmulVetor(NumtuVetor v) {
        double[] r = new double[linhas];
        for (int i = 0; i < linhas; i++) {
            double s = 0;
            for (int j = 0; j < colunas; j++) s += dados[i][j] * v.dados[j];
            r[i] = s;
        }
        return new NumtuVetor(r);
    }

    public NumtuMatriz transpose() {
        double[][] r = new double[colunas][linhas];
        for (int i = 0; i < linhas; i++)
            for (int j = 0; j < colunas; j++)
                r[j][i] = dados[i][j];
        return new NumtuMatriz(r);
    }

    // ============ ELEMENTWISE ============
    public NumtuMatriz exp() {
        double[][] r = new double[linhas][colunas];
        for (int i = 0; i < linhas; i++)
            for (int j = 0; j < colunas; j++) r[i][j] = Math.exp(dados[i][j]);
        return new NumtuMatriz(r);
    }
    public NumtuMatriz log() {
        double[][] r = new double[linhas][colunas];
        for (int i = 0; i < linhas; i++)
            for (int j = 0; j < colunas; j++) r[i][j] = Math.log(dados[i][j]);
        return new NumtuMatriz(r);
    }

    public NumtuVetor achatar() {
        double[] r = new double[linhas * colunas];
        int k = 0;
        for (int i = 0; i < linhas; i++)
            for (int j = 0; j < colunas; j++) r[k++] = dados[i][j];
        return new NumtuVetor(r);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < linhas; i++) {
            if (i > 0) sb.append(",\n ");
            sb.append("[");
            for (int j = 0; j < colunas; j++) {
                if (j > 0) sb.append(", ");
                double d = dados[i][j];
                if (d == Math.floor(d) && !Double.isInfinite(d) && Math.abs(d) < 1e15)
                    sb.append((long) d);
                else
                    sb.append(String.format(java.util.Locale.US, "%.4f", d));
            }
            sb.append("]");
        }
        sb.append("]");
        return sb.toString();
    }
}
