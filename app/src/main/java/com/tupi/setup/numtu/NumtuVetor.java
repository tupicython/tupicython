package com.tupi.setup.numtu;

import java.util.Arrays;
import java.util.List;

public class NumtuVetor {

    public final double[] dados;

    public NumtuVetor(double[] dados) {
        this.dados = dados;
    }

    public NumtuVetor(int tamanho) {
        this.dados = new double[tamanho];
    }

    // ============ CRIAÇÃO ============
    public static NumtuVetor zeros(int n) {
        return new NumtuVetor(n);
    }

    public static NumtuVetor uns(int n) {
        double[] d = new double[n];
        Arrays.fill(d, 1.0);
        return new NumtuVetor(d);
    }

    public static NumtuVetor aleatorio(int n, double escala) {
        double[] d = new double[n];
        for (int i = 0; i < n; i++) d[i] = (Math.random() * 2 - 1) * escala;
        return new NumtuVetor(d);
    }

    public static NumtuVetor aleatorioNormal(int n, double escala) {
        double[] d = new double[n];
        for (int i = 0; i < n; i++) d[i] = gauss() * escala;
        return new NumtuVetor(d);
    }

    public static NumtuVetor preenche(int n, double valor) {
        double[] d = new double[n];
        Arrays.fill(d, valor);
        return new NumtuVetor(d);
    }

    public static NumtuVetor intervalo(double inicio, double fim, double passo) {
        int n = (int) Math.floor((fim - inicio) / passo) + 1;
        double[] d = new double[n];
        for (int i = 0; i < n; i++) d[i] = inicio + i * passo;
        return new NumtuVetor(d);
    }

    public static NumtuVetor linspace(double inicio, double fim, int n) {
        double[] d = new double[n];
        if (n == 1) d[0] = inicio;
        else {
            double passo = (fim - inicio) / (n - 1);
            for (int i = 0; i < n; i++) d[i] = inicio + i * passo;
        }
        return new NumtuVetor(d);
    }

    public static NumtuVetor criarDeLista(List<Object> lista) {
        double[] d = new double[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            Object o = lista.get(i);
            if (o instanceof Number) d[i] = ((Number) o).doubleValue();
            else throw new RuntimeException("Vetor só aceita números");
        }
        return new NumtuVetor(d);
    }

    private static double gauss() {
        double u1 = Math.random(), u2 = Math.random();
        return Math.sqrt(-2 * Math.log(u1 + 1e-12)) * Math.cos(2 * Math.PI * u2);
    }

    // ============ ACESSO ============
    public int tamanho() { return dados.length; }
    public double obter(int i) { return dados[i]; }
    public void definir(int i, double v) { dados[i] = v; }
    public NumtuVetor copia() { return new NumtuVetor(dados.clone()); }

    // ============ ARITMÉTICA ============
    public NumtuVetor somar(NumtuVetor o) {
        double[] r = new double[dados.length];
        for (int i = 0; i < dados.length; i++) r[i] = dados[i] + o.dados[i];
        return new NumtuVetor(r);
    }

    public NumtuVetor subtrair(NumtuVetor o) {
        double[] r = new double[dados.length];
        for (int i = 0; i < dados.length; i++) r[i] = dados[i] - o.dados[i];
        return new NumtuVetor(r);
    }

    public NumtuVetor multiplicar(double e) {
        double[] r = new double[dados.length];
        for (int i = 0; i < dados.length; i++) r[i] = dados[i] * e;
        return new NumtuVetor(r);
    }

    public NumtuVetor multiplicarElemento(NumtuVetor o) {
        double[] r = new double[dados.length];
        for (int i = 0; i < dados.length; i++) r[i] = dados[i] * o.dados[i];
        return new NumtuVetor(r);
    }

    public NumtuVetor dividir(double e) {
        double[] r = new double[dados.length];
        for (int i = 0; i < dados.length; i++) r[i] = dados[i] / e;
        return new NumtuVetor(r);
    }

    public NumtuVetor somarEscalar(double x) {
        double[] r = new double[dados.length];
        for (int i = 0; i < dados.length; i++) r[i] = dados[i] + x;
        return new NumtuVetor(r);
    }

    public NumtuVetor subtrairEscalar(double x) {
        double[] r = new double[dados.length];
        for (int i = 0; i < dados.length; i++) r[i] = dados[i] - x;
        return new NumtuVetor(r);
    }

    // ============ ELEMENTWISE ============
    public NumtuVetor exp() {
        double[] r = new double[dados.length];
        for (int i = 0; i < dados.length; i++) r[i] = Math.exp(dados[i]);
        return new NumtuVetor(r);
    }
    public NumtuVetor log() {
        double[] r = new double[dados.length];
        for (int i = 0; i < dados.length; i++) r[i] = Math.log(dados[i]);
        return new NumtuVetor(r);
    }
    public NumtuVetor seno() {
        double[] r = new double[dados.length];
        for (int i = 0; i < dados.length; i++) r[i] = Math.sin(dados[i]);
        return new NumtuVetor(r);
    }
    public NumtuVetor cosseno() {
        double[] r = new double[dados.length];
        for (int i = 0; i < dados.length; i++) r[i] = Math.cos(dados[i]);
        return new NumtuVetor(r);
    }
    public NumtuVetor raiz() {
        double[] r = new double[dados.length];
        for (int i = 0; i < dados.length; i++) r[i] = Math.sqrt(dados[i]);
        return new NumtuVetor(r);
    }
    public NumtuVetor absoluto() {
        double[] r = new double[dados.length];
        for (int i = 0; i < dados.length; i++) r[i] = Math.abs(dados[i]);
        return new NumtuVetor(r);
    }
    public NumtuVetor potencia(double p) {
        double[] r = new double[dados.length];
        for (int i = 0; i < dados.length; i++) r[i] = Math.pow(dados[i], p);
        return new NumtuVetor(r);
    }
    public NumtuVetor clip(double min, double max) {
        double[] r = new double[dados.length];
        for (int i = 0; i < dados.length; i++) r[i] = Math.max(min, Math.min(max, dados[i]));
        return new NumtuVetor(r);
    }

    public NumtuVetor aplicar(java.util.function.DoubleUnaryOperator f) {
        double[] r = new double[dados.length];
        for (int i = 0; i < dados.length; i++) r[i] = f.applyAsDouble(dados[i]);
        return new NumtuVetor(r);
    }

    // ============ REDUÇÕES ============
    public double soma() {
        double s = 0;
        for (double d : dados) s += d;
        return s;
    }
    public double media() { return soma() / dados.length; }
    public double maximo() {
        double m = Double.NEGATIVE_INFINITY;
        for (double d : dados) if (d > m) m = d;
        return m;
    }
    public double minimo() {
        double m = Double.POSITIVE_INFINITY;
        for (double d : dados) if (d < m) m = d;
        return m;
    }
    public int argmax() {
        int idx = 0;
        double m = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < dados.length; i++) if (dados[i] > m) { m = dados[i]; idx = i; }
        return idx;
    }
    public double produto() {
        double p = 1.0;
        for (double d : dados) p *= d;
        return p;
    }
    public NumtuVetor acumulado() {
        double[] r = new double[dados.length];
        double acc = 0;
        for (int i = 0; i < dados.length; i++) { acc += dados[i]; r[i] = acc; }
        return new NumtuVetor(r);
    }

    // ============ ÁLGEBRA ============
    public double produtoEscalar(NumtuVetor o) {
        double s = 0;
        for (int i = 0; i < dados.length; i++) s += dados[i] * o.dados[i];
        return s;
    }
    public double norma() { return Math.sqrt(produtoEscalar(this)); }
    public double distancia(NumtuVetor o) { return subtrair(o).norma(); }

    public boolean igual(NumtuVetor o) {
        if (dados.length != o.dados.length) return false;
        for (int i = 0; i < dados.length; i++)
            if (Math.abs(dados[i] - o.dados[i]) > 1e-9) return false;
        return true;
    }

    public static NumtuVetor concatenar(NumtuVetor a, NumtuVetor b) {
        double[] r = new double[a.dados.length + b.dados.length];
        System.arraycopy(a.dados, 0, r, 0, a.dados.length);
        System.arraycopy(b.dados, 0, r, a.dados.length, b.dados.length);
        return new NumtuVetor(r);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < dados.length; i++) {
            if (i > 0) sb.append(", ");
            double d = dados[i];
            if (d == Math.floor(d) && !Double.isInfinite(d) && Math.abs(d) < 1e15)
                sb.append((long) d);
            else
                sb.append(String.format(java.util.Locale.US, "%.4f", d));
        }
        sb.append("]");
        return sb.toString();
    }
}
