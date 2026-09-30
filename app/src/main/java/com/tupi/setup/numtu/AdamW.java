package com.tupi.setup.numtu;

import java.util.HashMap;
import java.util.Map;

public class AdamW {

    public double beta1 = 0.9;
    public double beta2 = 0.999;
    public double eps = 1e-8;
    public long t = 0;

    // Estado: m e v por nome de parâmetro
    public Map<String, double[]> m = new HashMap<String, double[]>();
    public Map<String, double[]> v = new HashMap<String, double[]>();

    public void passo(Map<String, Object> params,
                      Map<String, Object> grads,
                      double lr,
                      double weightDecay) {
        t++;
        double b1t = 1.0 - Math.pow(beta1, t);
        double b2t = 1.0 - Math.pow(beta2, t);

        for (String nome : params.keySet()) {
            Object pObj = params.get(nome);
            Object gObj = grads.get(nome);
            if (gObj == null || pObj == null) continue;

            double[] p = paraArray(pObj);
            double[] g = paraArray(gObj);

            // Inicializa m e v pra esse param
            if (!m.containsKey(nome)) m.put(nome, new double[p.length]);
            if (!v.containsKey(nome)) v.put(nome, new double[p.length]);

            double[] mi = m.get(nome);
            double[] vi = v.get(nome);

            // Aplica weight decay
            boolean semDecay = nome.equals("ln_f_g") || nome.equals("b_out")
                || nome.startsWith("ln") || nome.startsWith("b_")
                || nome.endsWith("_b") || nome.endsWith("_g");

            for (int i = 0; i < p.length; i++) {
                double gi = g[i];
                if (Double.isNaN(gi) || Double.isInfinite(gi)) gi = 0;
                if (!semDecay) gi += weightDecay * p[i];

                mi[i] = beta1 * mi[i] + (1 - beta1) * gi;
                vi[i] = beta2 * vi[i] + (1 - beta2) * gi * gi;

                double mHat = mi[i] / b1t;
                double vHat = vi[i] / b2t;
                p[i] -= lr * mHat / (Math.sqrt(vHat) + eps);
            }

            // Escreve de volta
            escreveArray(pObj, p);
        }
    }

    // ============ helpers ============
    static double[] paraArray(Object o) {
        if (o instanceof NumtuVetor) return ((NumtuVetor) o).dados.clone();
        if (o instanceof NumtuMatriz) {
            NumtuMatriz m = (NumtuMatriz) o;
            double[] r = new double[m.linhas * m.colunas];
            int k = 0;
            for (int i = 0; i < m.linhas; i++)
                for (int j = 0; j < m.colunas; j++)
                    r[k++] = m.dados[i][j];
            return r;
        }
        NumtuTensor t = (NumtuTensor) o;
        return t.dados.clone();
    }

    static void escreveArray(Object o, double[] v) {
        if (o instanceof NumtuVetor) {
            System.arraycopy(v, 0, ((NumtuVetor) o).dados, 0, v.length);
            return;
        }
        if (o instanceof NumtuMatriz) {
            NumtuMatriz m = (NumtuMatriz) o;
            int k = 0;
            for (int i = 0; i < m.linhas; i++)
                for (int j = 0; j < m.colunas; j++)
                    m.dados[i][j] = v[k++];
            return;
        }
        NumtuTensor t = (NumtuTensor) o;
        System.arraycopy(v, 0, t.dados, 0, v.length);
    }
}
