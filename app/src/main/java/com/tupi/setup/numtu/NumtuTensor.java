package com.tupi.setup.numtu;

import java.util.Arrays;

public class NumtuTensor {

    public final double[] dados;
    public final int[] shape;
    public final int total;

    public NumtuTensor(int[] shape) {
        this.shape = shape.clone();
        int t = 1;
        for (int s : shape) t *= s;
        this.total = t;
        this.dados = new double[t];
    }

    public NumtuTensor(double[] dados, int[] shape) {
        this.dados = dados;
        this.shape = shape.clone();
        int t = 1;
        for (int s : shape) t *= s;
        if (t != dados.length) {
            throw new RuntimeException("NumtuTensor: " + t + " != " + dados.length);
        }
        this.total = t;
    }

    // ============ CRIAÇÃO ============
    public static NumtuTensor zeros(int... shape) { return new NumtuTensor(shape); }

    public static NumtuTensor uns(int... shape) {
        NumtuTensor t = new NumtuTensor(shape);
        Arrays.fill(t.dados, 1.0);
        return t;
    }

    public static NumtuTensor preenche(double v, int... shape) {
        NumtuTensor t = new NumtuTensor(shape);
        Arrays.fill(t.dados, v);
        return t;
    }

    public static NumtuTensor aleatorio(double escala, int... shape) {
        NumtuTensor t = new NumtuTensor(shape);
        for (int i = 0; i < t.total; i++)
            t.dados[i] = (Math.random() * 2 - 1) * escala;
        return t;
    }

    public static NumtuTensor aleatorioNormal(double escala, int... shape) {
        NumtuTensor t = new NumtuTensor(shape);
        for (int i = 0; i < t.total; i++) t.dados[i] = gauss() * escala;
        return t;
    }

    private static double gauss() {
        double u1 = Math.random(), u2 = Math.random();
        return Math.sqrt(-2 * Math.log(u1 + 1e-12)) * Math.cos(2 * Math.PI * u2);
    }

    // ============ INDEXAÇÃO N-D ============
    public double obter(int... idx) {
        return dados[offset(idx)];
    }

    public void definir(double v, int... idx) {
        dados[offset(idx)] = v;
    }

    private int offset(int[] idx) {
        if (idx.length != shape.length)
            throw new RuntimeException("Indexação: esperado " + shape.length + " dims, veio " + idx.length);
        int off = 0;
        int stride = 1;
        for (int i = shape.length - 1; i >= 0; i--) {
            off += idx[i] * stride;
            stride *= shape[i];
        }
        return off;
    }

    // ============ FORMA ============
    public NumtuTensor reshape(int... novoShape) {
        int t = 1;
        for (int s : novoShape) t *= s;
        if (t != total)
            throw new RuntimeException("reshape: total mudou (" + total + " -> " + t + ")");
        return new NumtuTensor(dados, novoShape);  // compartilha dados!
    }

    public NumtuTensor copia() {
        return new NumtuTensor(dados.clone(), shape);
    }

    public NumtuTensor transpose() {
        // Só 2D
        if (shape.length != 2)
            throw new RuntimeException("transpose() só pra 2D");
        int l = shape[0], c = shape[1];
        double[] r = new double[total];
        for (int i = 0; i < l; i++)
            for (int j = 0; j < c; j++)
                r[j * l + i] = dados[i * c + j];
        return new NumtuTensor(r, new int[]{c, l});
    }

    public NumtuTensor transpose(int... axes) {
		if (axes.length != shape.length)
			throw new RuntimeException("transpose: eixos incompatíveis");

		// Novo shape
		int[] novoShape = new int[shape.length];
		for (int i = 0; i < axes.length; i++) novoShape[i] = shape[axes[i]];

		// Strides do shape original (row-major)
		int[] strideOrig = new int[shape.length];
		int s = 1;
		for (int i = shape.length - 1; i >= 0; i--) {
			strideOrig[i] = s;
			s *= shape[i];
		}

		double[] r = new double[total];
		int[] idxOrig = new int[shape.length];
		int[] idxNovo = new int[shape.length];

		for (int i = 0; i < total; i++) {
			// Desmembra i nos índices da NOVA ordem
			int rem = i;
			for (int d = novoShape.length - 1; d >= 0; d--) {
				idxNovo[d] = rem % novoShape[d];
				rem /= novoShape[d];
			}
			// Converte pra índices na ORDEM ORIGINAL
			// novo[d] corresponde a orig[axes[d]]
			for (int d = 0; d < axes.length; d++) {
				idxOrig[axes[d]] = idxNovo[d];
			}
			// Offset no array original
			int off = 0;
			for (int d = 0; d < shape.length; d++) {
				off += idxOrig[d] * strideOrig[d];
			}
			r[i] = dados[off];
		}
		return new NumtuTensor(r, novoShape);
	}

    // Transpõe as 2 últimas dims
    public NumtuTensor transposeUltimas() {
        int nd = shape.length;
        int[] axes = new int[nd];
        for (int i = 0; i < nd; i++) axes[i] = i;
        int tmp = axes[nd - 1];
        axes[nd - 1] = axes[nd - 2];
        axes[nd - 2] = tmp;
        return transpose(axes);
    }

    // ============ BROADCASTING ============
    public NumtuTensor somar(NumtuTensor o) {
        if (Arrays.equals(shape, o.shape)) return elemento(this, o, "+");
        return broadcastBinario(this, o, "+");
    }

    public NumtuTensor subtrair(NumtuTensor o) {
        if (Arrays.equals(shape, o.shape)) return elemento(this, o, "-");
        return broadcastBinario(this, o, "-");
    }

    public NumtuTensor multiplicar(NumtuTensor o) {
        if (Arrays.equals(shape, o.shape)) return elemento(this, o, "*");
        return broadcastBinario(this, o, "*");
    }

    public NumtuTensor multiplicar(double e) {
        double[] r = new double[total];
        for (int i = 0; i < total; i++) r[i] = dados[i] * e;
        return new NumtuTensor(r, shape);
    }

    public NumtuTensor somarEscalar(double e) {
        double[] r = new double[total];
        for (int i = 0; i < total; i++) r[i] = dados[i] + e;
        return new NumtuTensor(r, shape);
    }

    private static NumtuTensor elemento(NumtuTensor a, NumtuTensor b, String op) {
        double[] r = new double[a.total];
        for (int i = 0; i < a.total; i++) {
            if (op.equals("+")) r[i] = a.dados[i] + b.dados[i];
            else if (op.equals("-")) r[i] = a.dados[i] - b.dados[i];
            else r[i] = a.dados[i] * b.dados[i];
        }
        return new NumtuTensor(r, a.shape);
    }

    private static NumtuTensor broadcastBinario(NumtuTensor a, NumtuTensor b, String op) {
        int[] outShape = broadcastShape(a.shape, b.shape);
        NumtuTensor r = new NumtuTensor(outShape);
        int[] idxA = new int[a.shape.length];
        int[] idxB = new int[b.shape.length];
        int[] idxO = new int[outShape.length];
        for (int i = 0; i < r.total; i++) {
            int rem = i;
            for (int d = outShape.length - 1; d >= 0; d--) {
                idxO[d] = rem % outShape[d];
                rem /= outShape[d];
            }
            int offA = 0, strideA = 1;
            for (int d = a.shape.length - 1; d >= 0; d--) {
                int od = outShape.length - a.shape.length + d;
                idxA[d] = a.shape[d] == 1 ? 0 : idxO[od];
                offA += idxA[d] * strideA;
                strideA *= a.shape[d];
            }
            int offB = 0, strideB = 1;
            for (int d = b.shape.length - 1; d >= 0; d--) {
                int od = outShape.length - b.shape.length + d;
                idxB[d] = b.shape[d] == 1 ? 0 : idxO[od];
                offB += idxB[d] * strideB;
                strideB *= b.shape[d];
            }
            double av = a.dados[offA];
            double bv = b.dados[offB];
            if (op.equals("+")) r.dados[i] = av + bv;
            else if (op.equals("-")) r.dados[i] = av - bv;
            else r.dados[i] = av * bv;
        }
        return r;
    }

    private static int[] broadcastShape(int[] a, int[] b) {
        int nd = Math.max(a.length, b.length);
        int[] out = new int[nd];
        for (int i = 0; i < nd; i++) {
            int ai = a.length - nd + i;
            int bi = b.length - nd + i;
            int av = ai >= 0 ? a[ai] : 1;
            int bv = bi >= 0 ? b[bi] : 1;
            if (av != bv && av != 1 && bv != 1)
                throw new RuntimeException("Broadcast impossível: " + Arrays.toString(a) + " vs " + Arrays.toString(b));
            out[i] = Math.max(av, bv);
        }
        return out;
    }

    // ============ MATMUL N-D (últimas 2 dims) ============
    public NumtuTensor matmul(NumtuTensor o) {
		int nd = shape.length;
		int ndO = o.shape.length;
		if (nd < 2 || ndO < 2)
			throw new RuntimeException("matmul: precisa 2D+");

		int M = shape[nd - 2];
		int K = shape[nd - 1];
		int K2 = o.shape[ndO - 2];
		int N = o.shape[ndO - 1];
		if (K != K2)
			throw new RuntimeException("matmul: " + K + " != " + K2);

		int[] batchA = new int[nd - 2];
		for (int i = 0; i < nd - 2; i++) batchA[i] = shape[i];
		int[] batchB = new int[ndO - 2];
		for (int i = 0; i < ndO - 2; i++) batchB[i] = o.shape[i];

		int[] batchOut = new int[Math.max(batchA.length, batchB.length)];
		for (int i = 0; i < batchOut.length; i++) {
			int ai = i - (batchOut.length - batchA.length);
			int bi = i - (batchOut.length - batchB.length);
			int a = ai >= 0 ? batchA[ai] : 1;
			int b = bi >= 0 ? batchB[bi] : 1;
			if (a != b && a != 1 && b != 1)
				throw new RuntimeException("matmul: batch dim " + i + " difere (" + a + " vs " + b + ")");
			batchOut[i] = Math.max(a, b);
		}

		int batch = 1;
		for (int b : batchOut) batch *= b;

		int[] outShape = new int[batchOut.length + 2];
		for (int i = 0; i < batchOut.length; i++) outShape[i] = batchOut[i];
		outShape[batchOut.length] = M;
		outShape[batchOut.length + 1] = N;

		double[] r = new double[batch * M * N];

		int batchA_total = 1;
		for (int b : batchA) batchA_total *= b;
		int batchB_total = 1;
		for (int b : batchB) batchB_total *= b;

		int strideA = M * K;
		int strideB = K * N;
		int strideR = M * N;

		for (int b = 0; b < batch; b++) {
			int aIdx = batchA_total == 1 ? 0 : b % batchA_total;
			int bIdx = batchB_total == 1 ? 0 : b % batchB_total;
			int offA = aIdx * strideA;
			int offB = bIdx * strideB;
			int offR = b * strideR;

			for (int i = 0; i < M; i++) {
				for (int k = 0; k < K; k++) {
					double av = dados[offA + i * K + k];
					if (av == 0.0) continue;
					for (int j = 0; j < N; j++) {
						r[offR + i * N + j] += av * o.dados[offB + k * N + j];
					}
				}
			}
		}
		return new NumtuTensor(r, outShape);
	}

    // ============ REDUÇÕES ============
    public NumtuTensor somaEixo(int eixo) {
        int[] outShape = new int[shape.length - 1];
        int k = 0;
        for (int i = 0; i < shape.length; i++) if (i != eixo) outShape[k++] = shape[i];
        NumtuTensor r = new NumtuTensor(outShape);
        int[] idx = new int[shape.length];
        for (int i = 0; i < total; i++) {
            int rem = i;
            for (int d = shape.length - 1; d >= 0; d--) {
                idx[d] = rem % shape[d];
                rem /= shape[d];
            }
            int[] idxOut = new int[outShape.length];
            int kk = 0;
            for (int d = 0; d < shape.length; d++) if (d != eixo) idxOut[kk++] = idx[d];
            r.dados[r.offset(idxOut)] += dados[i];
        }
        return r;
    }

    public NumtuTensor mediaEixo(int eixo) {
        NumtuTensor s = somaEixo(eixo);
        double f = shape[eixo];
        for (int i = 0; i < s.total; i++) s.dados[i] /= f;
        return s;
    }

    public NumtuTensor maxEixo(int eixo) {
        int[] outShape = new int[shape.length - 1];
        int k = 0;
        for (int i = 0; i < shape.length; i++) if (i != eixo) outShape[k++] = shape[i];
        NumtuTensor r = new NumtuTensor(outShape);
        Arrays.fill(r.dados, Double.NEGATIVE_INFINITY);
        int[] idx = new int[shape.length];
        for (int i = 0; i < total; i++) {
            int rem = i;
            for (int d = shape.length - 1; d >= 0; d--) {
                idx[d] = rem % shape[d];
                rem /= shape[d];
            }
            int[] idxOut = new int[outShape.length];
            int kk = 0;
            for (int d = 0; d < shape.length; d++) if (d != eixo) idxOut[kk++] = idx[d];
            int off = r.offset(idxOut);
            if (dados[i] > r.dados[off]) r.dados[off] = dados[i];
        }
        return r;
    }

    // ============ ELEMENTWISE ============
    public NumtuTensor exp() {
        double[] r = new double[total];
        for (int i = 0; i < total; i++) r[i] = Math.exp(dados[i]);
        return new NumtuTensor(r, shape);
    }

    public NumtuTensor log() {
        double[] r = new double[total];
        for (int i = 0; i < total; i++) r[i] = Math.log(dados[i]);
        return new NumtuTensor(r, shape);
    }

    public double soma() {
        double s = 0;
        for (double d : dados) s += d;
        return s;
    }

    public double media() { return soma() / total; }

    public double maximo() {
        double m = Double.NEGATIVE_INFINITY;
        for (double d : dados) if (d > m) m = d;
        return m;
    }

    // ============ UTIL ============
    public int dimensao() { return shape.length; }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Arrays.toString(shape));
        sb.append(" -> ");
        int mostrar = Math.min(total, 12);
        sb.append("[");
        for (int i = 0; i < mostrar; i++) {
            if (i > 0) sb.append(", ");
            double d = dados[i];
            if (d == Math.floor(d) && !Double.isInfinite(d) && Math.abs(d) < 1e15)
                sb.append((long) d);
            else
                sb.append(String.format(java.util.Locale.US, "%.4f", d));
        }
        if (total > mostrar) sb.append(", ...");
        sb.append("]");
        return sb.toString();
    }
}
