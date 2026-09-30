package com.tupi.setup.numtu;

import com.tupi.setup.interpretador.Ambiente;
import com.tupi.setup.interpretador.MeduloTupi;
import com.tupi.setup.interpretador.Nativa;

import java.util.Arrays;
import java.util.List;

public class MeduloNumtu implements MeduloTupi {
	public static IOMonager ioMonager;
    public String nome() { return "numtu"; }

    public void registrar(final Ambiente d) {

        // ============================================================
        // VETOR — criação
        // ============================================================
        d.definir("criar", new N_Criar());
        d.definir("zeros", new N_Zeros());
        d.definir("uns", new N_Uns());
        d.definir("aleatorio", new N_Aleatorio());
        d.definir("aleatorio_normal", new N_AleatorioNormal());
        d.definir("preenche", new N_Preenche());
        d.definir("intervalo", new N_Intervalo());
        d.definir("linspace", new N_Linspace());

        // VETOR — aritmética
        d.definir("somar", new N_Somar());
        d.definir("subtrair", new N_Subtrair());
        d.definir("multiplicar", new N_Multiplicar());
        d.definir("escala", new N_Escala());
        d.definir("dividir", new N_Dividir());
        d.definir("somar_escalar", new N_SomarEscalar());
        d.definir("subtrair_escalar", new N_SubtrairEscalar());

        // VETOR — elementwise
        d.definir("exp", new N_Exp());
        d.definir("log", new N_Log());
        d.definir("seno", new N_Seno());
        d.definir("cosseno", new N_Cosseno());
        d.definir("raiz", new N_Raiz());
        d.definir("absoluto", new N_Absoluto());
        d.definir("potencia", new N_Potencia());
        d.definir("clip", new N_Clip());

        // ATIVAÇÕES
        d.definir("sigmoid", new N_Sigmoid());
        d.definir("relu", new N_Relu());
        d.definir("tanh", new N_Tanh());
        d.definir("silu", new N_Silu());
        d.definir("softmax", new N_Softmax());
		d.definir("softmax_mascarado_backward", new N_SoftmaxMascaradoBackward());

        // NORM
        d.definir("rmsnorm", new N_Rmsnorm());

        // ESTATÍSTICAS
        d.definir("tamanho", new N_Tamanho());
        d.definir("soma", new N_Soma());
        d.definir("media", new N_Media());
        d.definir("maximo", new N_Maximo());
        d.definir("minimo", new N_Minimo());
        d.definir("argmax", new N_Argmax());
        d.definir("produto", new N_Produto());
        d.definir("acumulado", new N_Acumulado());

        // ÁLGEBRA
        d.definir("produto_escalar", new N_ProdutoEscalar());
        d.definir("norma", new N_Norma());
        d.definir("distancia", new N_Distancia());
        d.definir("matmul", new N_Matmul());
        d.definir("transpose", new N_Transpose());
		d.definir("matmul_backward", new N_MatmulBackward());
		d.definir("embedding_backward", new N_EmbeddingBackward());
		d.definir("cross_entropy_backward", new N_CrossEntropyBackward());
		d.definir("attention_backward_simples", new N_AttentionBackwardSimples());
		d.definir("attention_backward", new N_AttentionBackward());
		d.definir("attention_backward_full", new N_AttentionBackwardFull());
		
        // MATRIZ
        d.definir("matriz", new N_Matriz());
        d.definir("matriz_zeros", new N_MatrizZeros());
        d.definir("matriz_uns", new N_MatrizUns());
        d.definir("matriz_aleatorio", new N_MatrizAleatorio());
        d.definir("matriz_aleatorio_normal", new N_MatrizAleatorioNormal());
        d.definir("identidade", new N_Identidade());
        d.definir("linhas", new N_Linhas());
        d.definir("colunas", new N_Colunas());
        d.definir("achatar", new N_Achatar());

        // TENSOR
        d.definir("tensor_zeros", new N_TensorZeros());
        d.definir("tensor_uns", new N_TensorUns());
        d.definir("tensor_aleatorio", new N_TensorAleatorio());
        d.definir("reshape", new N_Reshape());
        d.definir("transpose_ultimas", new N_TransposeUltimas());
        d.definir("mascara_causal", new N_MascaraCausal());
        d.definir("matmul_tensor", new N_MatmulTensor());
        d.definir("soma_eixo", new N_SomaEixo());
        d.definir("media_eixo", new N_MediaEixo());
        d.definir("max_eixo", new N_MaxEixo());
		d.definir("transpose_backward", new N_TransposeBackward());
		d.definir("reshape_backward", new N_ReshapeBackward());
		d.definir("embedding", new N_Embedding());
		d.definir("amostra_batch", new N_AmostraBatch());
        // UTIL
        d.definir("copia", new N_Copia());
        d.definir("igual", new N_Igual());
        d.definir("concatenar", new N_Concatenar());
        d.definir("total_params", new N_TotalParams());
		// ============ I/O ============
		d.definir("le_arquivo", new N_LeArquivo());
		d.definir("escreve_arquivo", new N_EscreveArquivo());
		d.definir("existe_arquivo", new N_ExisteArquivo());
		d.definir("lista_arquivos", new N_ListaArquivos());
		d.definir("cria_lista", new N_CriaLista());
		d.definir("lista_add", new N_ListaAdd());
// ============ BPE ============
		d.definir("cria_bpe", new N_CriaBPE());
		d.definir("bpe_treina", new N_BPETreina());
		d.definir("bpe_codifica", new N_BPECodifica());
		d.definir("bpe_decodifica", new N_BPEDecodifica());
		d.definir("bpe_tamanho_vocab", new N_BPETamanhoVocab());
		d.definir("bpe_salva", new N_BPESalva());
		d.definir("bpe_carrega", new N_BPECarrega());
		
		// OTIMIZAÇÃO
		d.definir("cross_entropy", new N_CrossEntropy());
		d.definir("criar_adamw", new N_CriarAdamW());
		d.definir("adamw_passo", new N_AdamWP());
		d.definir("clip_grad", new N_ClipGrad());
		d.definir("cria_mapa", new N_CriaMapa());
		d.definir("mapa_pega", new N_MapaPega());
		d.definir("mapa_poe", new N_MapaPoe());
		d.definir("mapa_chaves", new N_MapaChaves());
		d.definir("gera_proximo_id", new N_GeraProximoId());
		d.definir("monta_lote", new N_MontaLote());
		// ATIVAÇÕES
		d.definir("sigmoid", new N_Sigmoid());
		d.definir("relu", new N_Relu());
		d.definir("tanh", new N_Tanh());
		d.definir("silu", new N_Silu());
		d.definir("softmax", new N_Softmax());

		d.definir("sigmoid_backward", new N_SigmoidBackward());   // ← TEM ISSO?
		d.definir("relu_backward", new N_ReluBackward());         // ← TEM ISSO?
		d.definir("tanh_backward", new N_TanhBackward());         // ← TEM ISSO?
		d.definir("silu_backward", new N_SiluBackward());         // ← TEM ISSO?
		
		d.definir("softmax_backward", new N_SoftmaxBackward());
		d.definir("rmsnorm_backward", new N_RmsnormBackward());
		d.definir("obter", new N_Obter());
		d.definir("salva_params", new N_SalvaParams());
		d.definir("carrega_params", new N_CarregaParams());
		d.definir("tensor_aleatorio_normal", new N_TensorAleatorioNormal());
		d.definir("le_arquivo_externo", new N_LeArquivoExterno());
		d.definir("copiar_pasta", new N_CopiarPasta());
		d.definir("copiar_arquivo", new N_CopiarArquivo());
    }
	
	static class N_CopiarPasta implements Nativa {
		public Object chamar(List<Object> a) {
			String origem = a.get(0).toString();
			String destino = a.get(1).toString();
			try {
				java.io.File de = new java.io.File(origem);
				java.io.File para = new java.io.File(destino);

				if (!de.exists()) return "Origem não existe: " + origem;

				if (!para.exists()) {
					boolean criou = para.mkdirs();
					if (!criou && !para.exists()) {
						return "Não criou pasta destino: " + destino;
					}
				}

				java.io.File[] filhos = de.listFiles();
				if (filhos == null) return "Sem arquivos em: " + origem;

				int copiados = 0;
				int pulados = 0;
				for (int i = 0; i < filhos.length; i++) {
					java.io.File filho = filhos[i];
					if (filho.isDirectory()) continue;

					// Ignora arquivos maiores que 5 MB (evita travar)
					long tamanho = filho.length();
					if (tamanho > 5 * 1024 * 1024) {
						pulados++;
						continue;
					}

					java.io.File destinoFilho = new java.io.File(para, filho.getName());
					copiarArquivoRec(filho, destinoFilho);
					copiados++;
				}

				return "OK - " + copiados + " copiados, " + pulados + " pulados";
			} catch (Exception e) {
				return "Erro: " + e.getMessage();
			}
		}

		private void copiarArquivoRec(java.io.File de, java.io.File para) throws Exception {
			java.io.FileInputStream in = new java.io.FileInputStream(de);
			java.io.FileOutputStream out = new java.io.FileOutputStream(para);
			byte[] buffer = new byte[8192];
			int lidos;
			while ((lidos = in.read(buffer)) > 0) {
				out.write(buffer, 0, lidos);
			}
			in.close();
			out.close();
		}
	}
	

	static class N_CopiarArquivo implements Nativa {
		public Object chamar(List<Object> a) {
			String origem = a.get(0).toString();
			String destino = a.get(1).toString();
			try {
				java.io.File de = new java.io.File(origem);
				java.io.File para = new java.io.File(destino);
				java.io.FileInputStream in = new java.io.FileInputStream(de);
				java.io.FileOutputStream out = new java.io.FileOutputStream(para);
				byte[] buffer = new byte[4096];
				int lidos;
				while ((lidos = in.read(buffer)) > 0) {
					out.write(buffer, 0, lidos);
				}
				in.close();
				out.close();
				return "OK";
			} catch (Exception e) {
				return "Erro: " + e.getMessage();
			}
		}
	}
	
	static class N_MontaLote implements Nativa {
		public Object chamar(List<Object> a) {
			NumtuVetor lista = (NumtuVetor) a.get(0);
			int ctx = ((Number) a.get(1)).intValue();
			int n = lista.tamanho();

			System.out.println("=== MONTALOTE ===");
			System.out.println("n=" + n + " ctx=" + ctx);
			if (n > 0) {
				System.out.println("primeiro=" + lista.obter(0));
				System.out.println("ultimo=" + lista.obter(n-1));
			}

			double[] lote = new double[ctx];
			int inicio = 0;
			if (n > ctx) inicio = n - ctx;
			int tam = n - inicio;

			for (int i = 0; i < ctx; i++) {
				if (i < tam) {
					lote[i] = lista.obter(inicio + i);
				} else {
					lote[i] = 0;
				}
			}

			System.out.println("lote[0]=" + lote[0]);
			System.out.println("lote[1]=" + lote[1]);
			System.out.println("=================");

			return new NumtuTensor(lote, new int[]{1, ctx});
		}
	}
	static class N_GeraProximoId implements Nativa {
		public Object chamar(List<Object> a) {
			// a[0] = probs [1, T, vocab]  (saída do forward)
			// a[1] = T  (posição do último token)
			NumtuTensor probs = paraTensor(a.get(0));
			int T = ((Number) a.get(1)).intValue();

			int nd = probs.shape.length;
			int V = probs.shape[nd - 1];

			// Pega o último token da sequência (índice T-1)
			int base = (T - 1) * V;

			// Argmax
			int melhorId = 0;
			double melhorProb = -1.0;
			for (int v = 0; v < V; v++) {
				double p = probs.dados[base + v];
				if (p > melhorProb) {
					melhorProb = p;
					melhorId = v;
				}
			}

			return Double.valueOf(melhorId);
		}
	}
	static class N_LeArquivoExterno implements Nativa {
		public Object chamar(List<Object> a) {
			if (ioMonager == null) throw new RuntimeException("IOManager não inicializado");
			return ioMonager.lerArquivoExterno(a.get(0).toString());
		}
	}
	
	static class N_TensorAleatorioNormal implements Nativa {
		public Object chamar(List<Object> a) {
			double escala = ((Number) a.get(0)).doubleValue();
			int[] shape = new int[a.size() - 1];
			for (int i = 1; i < a.size(); i++) {
				shape[i - 1] = ((Number) a.get(i)).intValue();
			}
			return NumtuTensor.aleatorioNormal(escala, shape);
		}
	}
	
	
	static class N_CriaLista implements Nativa {
		public Object chamar(List<Object> a) {
			return new java.util.ArrayList<Object>();
		}
	}

	static class N_ListaAdd implements Nativa {
		public Object chamar(List<Object> a) {
			java.util.List<Object> lista = (java.util.List<Object>) a.get(0);
			lista.add(a.get(1));
			return null;
		}
	}
	
	static class N_SalvaParams implements Nativa {
		public Object chamar(List<Object> a) {
			@SuppressWarnings("unchecked")
				java.util.Map<String, Object> params = (java.util.Map<String, Object>) a.get(0);
			String nome = a.get(1).toString();
			StringBuilder sb = new StringBuilder();
			for (String k : params.keySet()) {
				double[] v = AdamW.paraArray(params.get(k));
				sb.append(k).append(":");
				for (int i = 0; i < v.length; i++) {
					if (i > 0) sb.append(",");
					sb.append(v[i]);
				}
				sb.append("\n");
			}
			ioMonager.escreverArquivo(nome, sb.toString());
			return null;
		}
	}

	static class N_CarregaParams implements Nativa {
		public Object chamar(List<Object> a) {
			String nome = a.get(0).toString();
			String conteudo = ioMonager.lerArquivo(nome);
			java.util.Map<String, Object> params = new java.util.HashMap<String, Object>();
			String[] linhas = conteudo.split("\n");
			for (String linha : linhas) {
				if (linha.isEmpty()) continue;
				int pos = linha.indexOf(':');
				if (pos < 0) continue;
				String k = linha.substring(0, pos);
				String[] nums = linha.substring(pos + 1).split(",");
				double[] v = new double[nums.length];
				for (int i = 0; i < nums.length; i++) v[i] = Double.parseDouble(nums[i]);

				// Reconstrói o SHAPE baseado no nome
				NumtuTensor t = reconstruirShape(k, v);
				params.put(k, t);
			}
			return params;
		}

		private NumtuTensor reconstruirShape(String nome, double[] v) {
			int EMBED = 32;
			int HIDDEN = 64;

			// W_emb: [vocab, EMBED]
			if (nome.equals("W_emb")) {
				int vocab = v.length / EMBED;
				return new NumtuTensor(v, new int[]{vocab, EMBED});
			}
			// b_out: [vocab]
			if (nome.equals("b_out")) {
				return new NumtuTensor(v, new int[]{v.length});
			}
			// ln_f_g, b_q, b_k, b_v, b_o: [EMBED]
			if (nome.endsWith("ln_f_g") || nome.endsWith("b_q") 
				|| nome.endsWith("b_k") || nome.endsWith("b_v") 
				|| nome.endsWith("b_o") || nome.endsWith("ln1_g") 
				|| nome.endsWith("ln2_g") || nome.endsWith("b2")) {
				return new NumtuTensor(v, new int[]{v.length});
			}
			// W_q, W_k, W_v, W_o: [EMBED, EMBED]
			if (nome.endsWith("W_q") || nome.endsWith("W_k") 
				|| nome.endsWith("W_v") || nome.endsWith("W_o")) {
				return new NumtuTensor(v, new int[]{EMBED, EMBED});
			}
			// W1, W3: [EMBED, HIDDEN]
			if (nome.endsWith("W1") || nome.endsWith("W3")) {
				return new NumtuTensor(v, new int[]{EMBED, HIDDEN});
			}
			// W2: [HIDDEN, EMBED]
			if (nome.endsWith("W2")) {
				return new NumtuTensor(v, new int[]{HIDDEN, EMBED});
			}
			// b1, b3: [HIDDEN]
			if (nome.endsWith("b1") || nome.endsWith("b3")) {
				return new NumtuTensor(v, new int[]{HIDDEN});
			}

			// Padrão: vetor 1D
			return new NumtuTensor(v, new int[]{v.length});
		}
	}
	
	static class N_AmostraBatch implements Nativa {
		public Object chamar(List<Object> a) {
			NumtuTensor ids = paraTensor(a.get(0));
			int batch = ((Number) a.get(1)).intValue();
			int ctx   = ((Number) a.get(2)).intValue();

			int N = ids.total;
			double[] xb = new double[batch * ctx];
			double[] yb = new double[batch * ctx];

			for (int b = 0; b < batch; b++) {
				int start = (int) (Math.random() * (N - ctx - 1));
				for (int t = 0; t < ctx; t++) {
					xb[b * ctx + t] = ids.dados[start + t];
					yb[b * ctx + t] = ids.dados[start + t + 1];
				}
			}

			List<Object> r = new java.util.ArrayList<Object>();
			r.add(new NumtuTensor(xb, new int[]{batch, ctx}));
			r.add(new NumtuTensor(yb, new int[]{batch, ctx}));
			return r;
		}
	}
	
	static class N_Embedding implements Nativa {
		public Object chamar(List<Object> a) {
			// a[0] = ids [...]  (qualquer shape)
			// a[1] = W_emb [vocab, dim]
			// Retorna: shape de ids + [dim]
			NumtuTensor ids = paraTensor(a.get(0));
			NumtuTensor W   = paraTensor(a.get(1));
			int n    = ids.total;
			int dim  = W.shape[W.shape.length - 1];
			double[] r = new double[n * dim];
			for (int i = 0; i < n; i++) {
				int id = (int) ids.dados[i];
				for (int j = 0; j < dim; j++) {
					r[i * dim + j] = W.dados[id * dim + j];
				}
			}
			// Preserva o shape original + adiciona dim no final
			int[] novoShape = new int[ids.shape.length + 1];
			for (int i = 0; i < ids.shape.length; i++) novoShape[i] = ids.shape[i];
			novoShape[ids.shape.length] = dim;
			return new NumtuTensor(r, novoShape);
		}
	}
	
	// ============ I/O ============
	static class N_LeArquivo implements Nativa {
		public Object chamar(List<Object> a) {
			if (ioMonager == null) throw new RuntimeException("IOManager não inicializado");
			return ioMonager.lerArquivo(a.get(0).toString());
		}
	}
	static class N_EscreveArquivo implements Nativa {
		public Object chamar(List<Object> a) {
			if (ioMonager == null) throw new RuntimeException("IOManager não inicializado");
			ioMonager.escreverArquivo(a.get(0).toString(), a.get(1).toString());
			return null;
		}
	}
	static class N_ExisteArquivo implements Nativa {
		public Object chamar(List<Object> a) {
			if (ioMonager == null) return Boolean.FALSE;
			return Boolean.valueOf(ioMonager.existe(a.get(0).toString()));
		}
	}
	static class N_ListaArquivos implements Nativa {
		public Object chamar(List<Object> a) {
			if (ioMonager == null) throw new RuntimeException("IOManager não inicializado");
			String[] nomes = ioMonager.listar();
			List<Object> r = new java.util.ArrayList<Object>();
			for (String n : nomes) r.add(n);
			return r;
		}
	}

// ============ BPE ============
	static class N_CriaBPE implements Nativa {
		public Object chamar(List<Object> a) {
			if (a.isEmpty()) return new BPETokenizer();
			int vs = ((Number) a.get(0)).intValue();
			int mf = a.size() > 1 ? ((Number) a.get(1)).intValue() : 2;
			return new BPETokenizer(vs, mf);
		}
	}
	static class N_BPETreina implements Nativa {
		public Object chamar(List<Object> a) {
			BPETokenizer t = (BPETokenizer) a.get(0);
			t.treinar(a.get(1).toString());
			return t;
		}
	}
	static class N_BPECodifica implements Nativa {
		public Object chamar(List<Object> a) {
			BPETokenizer t = (BPETokenizer) a.get(0);
			int[] ids = t.codificar(a.get(1).toString());
			NumtuVetor v = new NumtuVetor(ids.length);
			for (int i = 0; i < ids.length; i++) v.dados[i] = ids[i];
			return v;
		}
	}
	static class N_BPEDecodifica implements Nativa {
		public Object chamar(List<Object> a) {
			BPETokenizer t = (BPETokenizer) a.get(0);
			NumtuVetor v = (NumtuVetor) a.get(1);
			int[] ids = new int[v.dados.length];
			for (int i = 0; i < ids.length; i++) ids[i] = (int) v.dados[i];
			return t.decodificar(ids);
		}
	}
	static class N_BPETamanhoVocab implements Nativa {
		public Object chamar(List<Object> a) {
			BPETokenizer t = (BPETokenizer) a.get(0);
			return Double.valueOf(t.tamanhoVocab());
		}
	}
	static class N_BPESalva implements Nativa {
		public Object chamar(List<Object> a) {
			// TODO: serialização — por enquanto salva só tamanho
			return null;
		}
	}
	static class N_BPECarrega implements Nativa {
		public Object chamar(List<Object> a) {
			return new BPETokenizer();
		}
	}
	
	// ---- ATTENTION BACKWARD COMPLETO (PARTE 3/3) ----
	static class N_AttentionBackwardFull implements Nativa {
		public Object chamar(List<Object> a) {
			// a[0] = x [B,T,E]
			// a[1] = W_q [E,E], a[2] = W_k [E,E], a[3] = W_v [E,E]
			// a[4] = attn [B,H,T,T]
			// a[5] = dCtx [B,H,T,D]
			// a[6] = B, a[7] = H, a[8] = T, a[9] = E, a[10] = D
			// Retorna [dx, dW_q, dW_k, dW_v]

			NumtuTensor x    = paraTensor(a.get(0));
			NumtuTensor W_q  = paraTensor(a.get(1));
			NumtuTensor W_k  = paraTensor(a.get(2));
			NumtuTensor W_v  = paraTensor(a.get(3));
			NumtuTensor attn = paraTensor(a.get(4));
			NumtuTensor dCtx = paraTensor(a.get(5));
			int B = ((Number) a.get(6)).intValue();
			int H = ((Number) a.get(7)).intValue();
			int T = ((Number) a.get(8)).intValue();
			int E = ((Number) a.get(9)).intValue();
			int D = ((Number) a.get(10)).intValue();

			// ============================================
			// 1) FORWARD: reconstruir Q, K, V em [B,H,T,D]
			// ============================================
			NumtuTensor Q_flat = x.matmul(W_q);   // [B,T,E]
			NumtuTensor K_flat = x.matmul(W_k);
			NumtuTensor V_flat = x.matmul(W_v);

			NumtuTensor Qh = Q_flat.reshape(B, T, H, D);
			NumtuTensor Kh = K_flat.reshape(B, T, H, D);
			NumtuTensor Vh = V_flat.reshape(B, T, H, D);

			NumtuTensor Q = Qh.transpose(0, 2, 1, 3);
			NumtuTensor K = Kh.transpose(0, 2, 1, 3);
			NumtuTensor V = Vh.transpose(0, 2, 1, 3);

			// ============================================
			// 2) ATENÇÃO BACKWARD (já temos!)
			// ============================================
			double escala = 1.0 / Math.sqrt(D);

			// dAttn = dCtx @ V^T
			NumtuTensor Vt = V.transposeUltimas();
			NumtuTensor dAttn = dCtx.matmul(Vt);

			// dV = attn^T @ dCtx
			NumtuTensor attnT = attn.transposeUltimas();
			NumtuTensor dV_bhtd = attnT.matmul(dCtx);

			// dScores
			NumtuTensor dScores = softmaxMascaradoBackward(attn, dAttn);
			dScores = dScores.multiplicar(escala);

			// dQ, dK
			NumtuTensor dQ_bhtd = dScores.matmul(K);
			NumtuTensor dScoresT = dScores.transposeUltimas();
			NumtuTensor dK_bhtd = dScoresT.matmul(Q);

			// ============================================
			// 3) TRANSPOSE BACKWARD (desfaz o [B,T,H,D]→[B,H,T,D])
			// ============================================
			NumtuTensor dQh = dQ_bhtd.transpose(0, 2, 1, 3);  // [B,T,H,D]
			NumtuTensor dKh = dK_bhtd.transpose(0, 2, 1, 3);
			NumtuTensor dVh = dV_bhtd.transpose(0, 2, 1, 3);

			// ============================================
			// 4) RESHAPE BACKWARD (volta pra [B,T,E])
			// ============================================
			NumtuTensor dQ_flat = dQh.reshape(B, T, E);
			NumtuTensor dK_flat = dKh.reshape(B, T, E);
			NumtuTensor dV_flat = dVh.reshape(B, T, E);

			// ============================================
			// 5) MATMUL BACKWARD (projeta até W_q, W_k, W_v)
			// ============================================
			// dW_q = x_flat^T @ dQ_flat    onde x_flat é [B*T, E]
			NumtuTensor x_flat  = x.reshape(B * T, E);
			NumtuTensor dQ_2d   = dQ_flat.reshape(B * T, E);
			NumtuTensor dK_2d   = dK_flat.reshape(B * T, E);
			NumtuTensor dV_2d   = dV_flat.reshape(B * T, E);

			NumtuTensor x_flat_T = x_flat.transpose(0, 1);

			NumtuTensor dW_q = x_flat_T.matmul(dQ_2d);   // [E, E]
			NumtuTensor dW_k = x_flat_T.matmul(dK_2d);
			NumtuTensor dW_v = x_flat_T.matmul(dV_2d);

			// dx_q = dQ_flat @ W_q^T     (broadcast do batch)
			NumtuTensor W_q_T = W_q.transpose(0, 1);
			NumtuTensor W_k_T = W_k.transpose(0, 1);
			NumtuTensor W_v_T = W_v.transpose(0, 1);

			NumtuTensor dx_q = dQ_flat.matmul(W_q_T);
			NumtuTensor dx_k = dK_flat.matmul(W_k_T);
			NumtuTensor dx_v = dV_flat.matmul(W_v_T);

			// ============================================
			// 6) SOMA DOS GRADIENTES DE X (x é compartilhado)
			// ============================================
			NumtuTensor dx = dx_q.somar(dx_k).somar(dx_v);

			List<Object> r = new java.util.ArrayList<Object>();
			r.add(dx);
			r.add(dW_q);
			r.add(dW_k);
			r.add(dW_v);
			return r;
		}
	}

	// ---- ATTENTION BACKWARD COMPLETO (PARTE 2/3) ----
	static class N_AttentionBackward implements Nativa {
		public Object chamar(List<Object> a) {
			// a[0] = Q [B, H, T, D]
			// a[1] = K [B, H, T, D]
			// a[2] = V [B, H, T, D]
			// a[3] = attn [B, H, T, T]
			// a[4] = dCtx [B, H, T, D]
			// a[5] = head_dim (D, número)
			// Retorna [dQ, dK, dV] cada um [B, H, T, D]

			NumtuTensor Q    = paraTensor(a.get(0));
			NumtuTensor K    = paraTensor(a.get(1));
			NumtuTensor V    = paraTensor(a.get(2));
			NumtuTensor attn = paraTensor(a.get(3));
			NumtuTensor dCtx = paraTensor(a.get(4));
			double D         = ((Number) a.get(5)).doubleValue();
			double escala    = 1.0 / Math.sqrt(D);

			// 1) dAttn = dCtx @ Vᵀ
			NumtuTensor Vt = V.transposeUltimas();
			NumtuTensor dAttn = dCtx.matmul(Vt);

			// 2) dV = attnᵀ @ dCtx
			NumtuTensor attnT = attn.transposeUltimas();
			NumtuTensor dV = attnT.matmul(dCtx);

			// 3) dScores = softmax_mascarado_backward(attn, dAttn)
			NumtuTensor dScores = softmaxMascaradoBackward(attn, dAttn);

			// 4) Aplica escala
			dScores = dScores.multiplicar(escala);

			// 5) dQ = dScores @ K
			NumtuTensor dQ = dScores.matmul(K);

			// 6) dK = dScoresᵀ @ Q
			NumtuTensor dScoresT = dScores.transposeUltimas();
			NumtuTensor dK = dScoresT.matmul(Q);

			List<Object> r = new java.util.ArrayList<Object>();
			r.add(dQ);
			r.add(dK);
			r.add(dV);
			return r;
		}
	}
	
	// ---- ATTENTION BACKWARD (PARTE 1/3) ----
	static class N_AttentionBackwardSimples implements Nativa {
		public Object chamar(List<Object> a) {
			// a[0] = Q [T, D]
			// a[1] = K [T, D]
			// a[2] = V [T, D]
			// a[3] = attn [T, T]    (output do softmax)
			// a[4] = dCtx [T, D]    (gradiente do output)
			// a[5] = head_dim       (D, pra dividir pela escala)
			// Retorna [dQ, dK, dV]  cada um [T, D]

			NumtuTensor Q    = paraTensor(a.get(0));
			NumtuTensor K    = paraTensor(a.get(1));
			NumtuTensor V    = paraTensor(a.get(2));
			NumtuTensor attn = paraTensor(a.get(3));
			NumtuTensor dCtx = paraTensor(a.get(4));
			double D         = ((Number) a.get(5)).doubleValue();
			double escala    = 1.0 / Math.sqrt(D);

			// 1) dAttn = dCtx @ Vᵀ
			NumtuTensor Vt = V.transposeUltimas();
			NumtuTensor dAttn = dCtx.matmul(Vt);

			// 2) dV = attnᵀ @ dCtx
			NumtuTensor attnT = attn.transposeUltimas();
			NumtuTensor dV = attnT.matmul(dCtx);

			// 3) dScores = softmax_mascarado_backward(attn, dAttn)
			NumtuTensor dScores = softmaxMascaradoBackward(attn, dAttn);

			// 4) dScores = dScores * escala
			dScores = dScores.multiplicar(escala);

			// 5) dQ = dScores @ K
			NumtuTensor dQ = dScores.matmul(K);

			// 6) dK = dScoresᵀ @ Q
			NumtuTensor dScoresT = dScores.transposeUltimas();
			NumtuTensor dK = dScoresT.matmul(Q);

			List<Object> r = new java.util.ArrayList<Object>();
			r.add(dQ);
			r.add(dK);
			r.add(dV);
			return r;
		}
	}

	static NumtuTensor softmaxMascaradoBackward(NumtuTensor y, NumtuTensor dy) {
		int nd = y.shape.length;
		int T  = y.shape[nd - 1];
		int T2 = y.shape[nd - 2];
		int blocos = y.total / (T2 * T);
		double[] r = new double[y.total];
		for (int b = 0; b < blocos; b++) {
			int base = b * T2 * T;
			for (int i = 0; i < T2; i++) {
				int linhaOff = base + i * T;
				double soma = 0;
				for (int j = 0; j < T; j++) {
					soma += dy.dados[linhaOff + j] * y.dados[linhaOff + j];
				}
				for (int j = 0; j < T; j++) {
					if (j > i) {
						r[linhaOff + j] = 0.0;
					} else {
						r[linhaOff + j] = y.dados[linhaOff + j] *
							(dy.dados[linhaOff + j] - soma);
					}
				}
			}
		}
		return new NumtuTensor(r, y.shape);
	}
	
	// ---- BACKWARD DE FORMA ----

	static class N_TransposeBackward implements Nativa {
		public Object chamar(List<Object> a) {
			// a[0] = dy (gradiente do output do transpose)
			// a[1..] = eixos (a mesma permutação usada no forward)
			// Retorna dx = transpose(dy, eixos)
			// (transpose é sua própria inversa quando aplicada 2x)
			NumtuTensor dy = paraTensor(a.get(0));

			int nArgs = a.size();
			if (nArgs == 1) {
				// Transpose simples das últimas 2 dims
				return dy.transposeUltimas();
			}
			int[] eixos = new int[nArgs - 1];
			for (int i = 1; i < nArgs; i++) {
				eixos[i - 1] = ((Number) a.get(i)).intValue();
			}
			return dy.transpose(eixos);
		}
	}

	static class N_ReshapeBackward implements Nativa {
		public Object chamar(List<Object> a) {
			// a[0] = dy (gradiente do output do reshape)
			// a[1..] = shape original
			NumtuTensor dy = paraTensor(a.get(0));

			int[] shapeOriginal = new int[a.size() - 1];
			for (int i = 1; i < a.size(); i++) {
				shapeOriginal[i - 1] = ((Number) a.get(i)).intValue();
			}
			return dy.reshape(shapeOriginal);
		}
	}
    // ============================================================
    // HELPERS INTERNOS
    // ============================================================
    static NumtuTensor paraTensor(Object o) {
        if (o instanceof NumtuTensor) return (NumtuTensor) o;
        if (o instanceof NumtuVetor) {
            NumtuVetor v = (NumtuVetor) o;
            return new NumtuTensor(v.dados.clone(), new int[]{v.tamanho()});
        }
        NumtuMatriz m = (NumtuMatriz) o;
        double[] flat = new double[m.linhas * m.colunas];
        int k = 0;
        for (int i = 0; i < m.linhas; i++)
            for (int j = 0; j < m.colunas; j++)
                flat[k++] = m.dados[i][j];
        return new NumtuTensor(flat, new int[]{m.linhas, m.colunas});
    }

    // ============================================================
    // CLASSES NATIVAS — cada uma é uma operação
    // ============================================================

    // ---- CRIAÇÃO ----
    static class N_Criar implements Nativa {
        public Object chamar(List<Object> a) {
            return NumtuVetor.criarDeLista((List<Object>) a.get(0));
        }
    }
    static class N_Zeros implements Nativa {
        public Object chamar(List<Object> a) {
            return NumtuVetor.zeros(((Number) a.get(0)).intValue());
        }
    }
    static class N_Uns implements Nativa {
        public Object chamar(List<Object> a) {
            return NumtuVetor.uns(((Number) a.get(0)).intValue());
        }
    }
    static class N_Aleatorio implements Nativa {
        public Object chamar(List<Object> a) {
            int n = ((Number) a.get(0)).intValue();
            double e = a.size() > 1 ? ((Number) a.get(1)).doubleValue() : 1.0;
            return NumtuVetor.aleatorio(n, e);
        }
    }
    static class N_AleatorioNormal implements Nativa {
        public Object chamar(List<Object> a) {
            int n = ((Number) a.get(0)).intValue();
            double e = a.size() > 1 ? ((Number) a.get(1)).doubleValue() : 1.0;
            return NumtuVetor.aleatorioNormal(n, e);
        }
    }
    static class N_Preenche implements Nativa {
        public Object chamar(List<Object> a) {
            return NumtuVetor.preenche(((Number) a.get(0)).intValue(),
                                       ((Number) a.get(1)).doubleValue());
        }
    }
    static class N_Intervalo implements Nativa {
        public Object chamar(List<Object> a) {
            double i = ((Number) a.get(0)).doubleValue();
            double f = ((Number) a.get(1)).doubleValue();
            double p = a.size() > 2 ? ((Number) a.get(2)).doubleValue() : 1.0;
            return NumtuVetor.intervalo(i, f, p);
        }
    }
    static class N_Linspace implements Nativa {
        public Object chamar(List<Object> a) {
            return NumtuVetor.linspace(
                ((Number) a.get(0)).doubleValue(),
                ((Number) a.get(1)).doubleValue(),
                ((Number) a.get(2)).intValue());
        }
    }

    // ---- ARITMÉTICA ----
    static class N_Somar implements Nativa {
        public Object chamar(List<Object> a) {
            Object x = a.get(0), y = a.get(1);
            if (x instanceof NumtuVetor) {
                if (y instanceof NumtuVetor) return ((NumtuVetor) x).somar((NumtuVetor) y);
                return ((NumtuVetor) x).somarEscalar(((Number) y).doubleValue());
            }
            if (x instanceof NumtuMatriz) {
                if (y instanceof NumtuMatriz) return ((NumtuMatriz) x).somar((NumtuMatriz) y);
                if (y instanceof NumtuVetor) return ((NumtuMatriz) x).somarVetor((NumtuVetor) y);
            }
            return ((NumtuTensor) x).somar((NumtuTensor) y);
        }
    }
    static class N_Subtrair implements Nativa {
        public Object chamar(List<Object> a) {
            Object x = a.get(0), y = a.get(1);
            if (x instanceof NumtuVetor) return ((NumtuVetor) x).subtrair((NumtuVetor) y);
            if (x instanceof NumtuMatriz) return ((NumtuMatriz) x).subtrair((NumtuMatriz) y);
            return ((NumtuTensor) x).subtrair((NumtuTensor) y);
        }
    }
    static class N_Multiplicar implements Nativa {
        public Object chamar(List<Object> a) {
            Object x = a.get(0), y = a.get(1);
            if (x instanceof NumtuVetor) {
                if (y instanceof NumtuVetor) return ((NumtuVetor) x).multiplicarElemento((NumtuVetor) y);
                return ((NumtuVetor) x).multiplicar(((Number) y).doubleValue());
            }
            if (x instanceof NumtuMatriz) {
                if (y instanceof NumtuMatriz) return ((NumtuMatriz) x).multiplicarElemento((NumtuMatriz) y);
                return ((NumtuMatriz) x).multiplicar(((Number) y).doubleValue());
            }
            if (y instanceof NumtuTensor) return ((NumtuTensor) x).multiplicar((NumtuTensor) y);
            return ((NumtuTensor) x).multiplicar(((Number) y).doubleValue());
        }
    }
    static class N_Escala implements Nativa {
        public Object chamar(List<Object> a) {
            Object x = a.get(0);
            double e = ((Number) a.get(1)).doubleValue();
            if (x instanceof NumtuVetor) return ((NumtuVetor) x).multiplicar(e);
            if (x instanceof NumtuMatriz) return ((NumtuMatriz) x).multiplicar(e);
            return ((NumtuTensor) x).multiplicar(e);
        }
    }
    static class N_Dividir implements Nativa {
        public Object chamar(List<Object> a) {
            return ((NumtuVetor) a.get(0)).dividir(((Number) a.get(1)).doubleValue());
        }
    }
    static class N_SomarEscalar implements Nativa {
        public Object chamar(List<Object> a) {
            Object x = a.get(0);
            double e = ((Number) a.get(1)).doubleValue();
            if (x instanceof NumtuVetor) return ((NumtuVetor) x).somarEscalar(e);
            return ((NumtuTensor) x).somarEscalar(e);
        }
    }
    static class N_SubtrairEscalar implements Nativa {
        public Object chamar(List<Object> a) {
            return ((NumtuVetor) a.get(0)).subtrairEscalar(((Number) a.get(1)).doubleValue());
        }
    }

    // ---- ELEMENTWISE ----
    static class N_Exp implements Nativa {
        public Object chamar(List<Object> a) {
            Object o = a.get(0);
            if (o instanceof NumtuVetor) return ((NumtuVetor) o).exp();
            if (o instanceof NumtuMatriz) return ((NumtuMatriz) o).exp();
            return ((NumtuTensor) o).exp();
        }
    }
    static class N_Log implements Nativa {
        public Object chamar(List<Object> a) {
            Object o = a.get(0);
            if (o instanceof NumtuVetor) return ((NumtuVetor) o).log();
            if (o instanceof NumtuMatriz) return ((NumtuMatriz) o).log();
            return ((NumtuTensor) o).log();
        }
    }
    static class N_Seno implements Nativa {
        public Object chamar(List<Object> a) { return ((NumtuVetor) a.get(0)).seno(); }
    }
    static class N_Cosseno implements Nativa {
        public Object chamar(List<Object> a) { return ((NumtuVetor) a.get(0)).cosseno(); }
    }
    static class N_Raiz implements Nativa {
        public Object chamar(List<Object> a) { return ((NumtuVetor) a.get(0)).raiz(); }
    }
    static class N_Absoluto implements Nativa {
        public Object chamar(List<Object> a) { return ((NumtuVetor) a.get(0)).absoluto(); }
    }
    static class N_Potencia implements Nativa {
        public Object chamar(List<Object> a) {
            return ((NumtuVetor) a.get(0)).potencia(((Number) a.get(1)).doubleValue());
        }
    }
    static class N_Clip implements Nativa {
        public Object chamar(List<Object> a) {
            return ((NumtuVetor) a.get(0)).clip(
                ((Number) a.get(1)).doubleValue(),
                ((Number) a.get(2)).doubleValue());
        }
    }

    // ---- ATIVAÇÕES ----
    static class N_Sigmoid implements Nativa {
		public Object chamar(List<Object> a) { return aplicar(a.get(0), 0); }
	}
	static class N_Relu implements Nativa {
		public Object chamar(List<Object> a) { return aplicar(a.get(0), 1); }
	}
	static class N_Tanh implements Nativa {
		public Object chamar(List<Object> a) { return aplicar(a.get(0), 2); }
	}
	static class N_Silu implements Nativa {
		public Object chamar(List<Object> a) { return aplicar(a.get(0), 3); }
	}
    static class N_Softmax implements Nativa {
        public Object chamar(List<Object> a) { return softmaxUltimo(a.get(0)); }
    }
    static class N_Rmsnorm implements Nativa {
        public Object chamar(List<Object> a) { return rmsnorm(a.get(0), a.get(1)); }
    }

    static Object aplicar(Object o, int tipo) {
		// tipo: 0=sigmoid, 1=relu, 2=tanh, 3=silu
		if (o instanceof NumtuVetor) {
			NumtuVetor v = (NumtuVetor) o;
			double[] r = new double[v.dados.length];
			for (int i = 0; i < r.length; i++) r[i] = aplicaUm(v.dados[i], tipo);
			return new NumtuVetor(r);
		}
		if (o instanceof NumtuMatriz) {
			NumtuMatriz m = (NumtuMatriz) o;
			NumtuMatriz res = new NumtuMatriz(m.linhas, m.colunas);
			for (int i = 0; i < m.linhas; i++)
				for (int j = 0; j < m.colunas; j++)
					res.dados[i][j] = aplicaUm(m.dados[i][j], tipo);
			return res;
		}
		NumtuTensor t = (NumtuTensor) o;
		double[] r = new double[t.total];
		for (int i = 0; i < t.total; i++) r[i] = aplicaUm(t.dados[i], tipo);
		return new NumtuTensor(r, t.shape);
	}

	static double aplicaUm(double x, int tipo) {
		if (tipo == 0) { // sigmoid
			double c = Math.max(-50, Math.min(50, x));
			return 1.0 / (1.0 + Math.exp(-c));
		}
		if (tipo == 1) { // relu
			return Math.max(0, x);
		}
		if (tipo == 2) { // tanh
			return Math.tanh(x);
		}
		// silu
		double c = Math.max(-50, Math.min(50, x));
		return x / (1.0 + Math.exp(-c));
	}
    static NumtuTensor softmaxUltimo(Object o) {
        NumtuTensor t = paraTensor(o);
        int nd = t.shape.length;
        int ultimo = t.shape[nd - 1];
        int blocos = t.total / ultimo;
        double[] r = new double[t.total];
        for (int b = 0; b < blocos; b++) {
            int base = b * ultimo;
            double max = Double.NEGATIVE_INFINITY;
            for (int j = 0; j < ultimo; j++) if (t.dados[base + j] > max) max = t.dados[base + j];
            double soma = 0;
            for (int j = 0; j < ultimo; j++) {
                r[base + j] = Math.exp(t.dados[base + j] - max);
                soma += r[base + j];
            }
            for (int j = 0; j < ultimo; j++) r[base + j] /= soma;
        }
        return new NumtuTensor(r, t.shape);
    }

    static NumtuTensor rmsnorm(Object x, Object g) {
        NumtuTensor xt = paraTensor(x);
        NumtuTensor gt = paraTensor(g);
        int nd = xt.shape.length;
        int ultimo = xt.shape[nd - 1];
        int blocos = xt.total / ultimo;
        double[] r = new double[xt.total];
        double eps = 1e-6;
        for (int b = 0; b < blocos; b++) {
            int base = b * ultimo;
            double ms = 0;
            for (int j = 0; j < ultimo; j++) ms += xt.dados[base + j] * xt.dados[base + j];
            ms /= ultimo;
            double inv = 1.0 / Math.sqrt(ms + eps);
            for (int j = 0; j < ultimo; j++)
                r[base + j] = xt.dados[base + j] * inv * gt.dados[j];
        }
        return new NumtuTensor(r, xt.shape);
    }
	// ---- BACKWARD DAS ATIVAÇÕES ----
	static class N_SigmoidBackward implements Nativa {
		public Object chamar(List<Object> a) {
			// a[0] = x (entrada original)
			// a[1] = dy (gradiente do output)
			// dx = dy * sigmoid(x) * (1 - sigmoid(x))
			return backwardAtivacao(a.get(0), a.get(1), 0);
		}
	}

	static class N_ReluBackward implements Nativa {
		public Object chamar(List<Object> a) {
			// dx = dy * (x > 0 ? 1 : 0)
			return backwardAtivacao(a.get(0), a.get(1), 1);
		}
	}

	static class N_TanhBackward implements Nativa {
		public Object chamar(List<Object> a) {
			// dx = dy * (1 - tanh(x)^2)
			return backwardAtivacao(a.get(0), a.get(1), 2);
		}
	}

	static class N_SiluBackward implements Nativa {
		public Object chamar(List<Object> a) {
			// dx = dy * (silu(x) + sigmoid(x)*(1-silu(x)))
			return backwardAtivacao(a.get(0), a.get(1), 3);
		}
	}

	static class N_SoftmaxMascaradoBackward implements Nativa {
		public Object chamar(List<Object> a) {
			// a[0] = y (output do softmax mascarado)
			// a[1] = dy (gradiente do output)
			// Retorna dx com zeros onde j > i (máscara causal)
			NumtuTensor y = paraTensor(a.get(0));
			NumtuTensor dy = paraTensor(a.get(1));

			int nd = y.shape.length;
			int T = y.shape[nd - 1];        // último eixo
			int T2 = y.shape[nd - 2];       // penúltimo eixo (linhas)
			int blocos = y.total / (T2 * T);

			double[] r = new double[y.total];

			for (int b = 0; b < blocos; b++) {
				int base = b * T2 * T;

				// Pra cada linha i da matriz T2 x T
				for (int i = 0; i < T2; i++) {
					int linhaOff = base + i * T;

					// Soma = Σ_j(dy_j · y_j)
					double soma = 0;
					for (int j = 0; j < T; j++) {
						soma += dy.dados[linhaOff + j] * y.dados[linhaOff + j];
					}

					// dx_j = y_j · (dy_j - soma)
					// MAS zera se j > i (máscara causal)
					for (int j = 0; j < T; j++) {
						if (j > i) {
							r[linhaOff + j] = 0.0;   // máscara atua
						} else {
							r[linhaOff + j] = y.dados[linhaOff + j] *
								(dy.dados[linhaOff + j] - soma);
						}
					}
				}
			}
			return new NumtuTensor(r, y.shape);
		}
	}
	
// ============================================================
// FUNÇÃO CENTRAL DE BACKWARD DAS ATIVAÇÕES
// ============================================================
	static Object backwardAtivacao(Object xObj, Object dyObj, int tipo) {
		NumtuTensor x = paraTensor(xObj);
		NumtuTensor dy = paraTensor(dyObj);
		double[] r = new double[x.total];

		for (int i = 0; i < x.total; i++) {
			double xi = x.dados[i];
			double dyi = dy.dados[i];

			if (tipo == 0) {
				// sigmoid: dx = dy * s * (1-s)   onde s = sigmoid(x)
				double s = 1.0 / (1.0 + Math.exp(-Math.max(-50, Math.min(50, xi))));
				r[i] = dyi * s * (1.0 - s);
			} else if (tipo == 1) {
				// relu: dx = dy * (x > 0 ? 1 : 0)
				r[i] = xi > 0 ? dyi : 0.0;
			} else if (tipo == 2) {
				// tanh: dx = dy * (1 - tanh(x)^2)
				double t = Math.tanh(xi);
				r[i] = dyi * (1.0 - t * t);
			} else {
				// silu: dx = dy * (silu(x) + sigmoid(x)*(1-silu(x)))
				double s = 1.0 / (1.0 + Math.exp(-Math.max(-50, Math.min(50, xi))));
				double silu = xi * s;
				r[i] = dyi * (silu + s * (1.0 - silu));
			}
		}
		return new NumtuTensor(r, x.shape);
	}
// ---- BACKWARD COMPLEXO ----
	static class N_SoftmaxBackward implements Nativa {
		public Object chamar(List<Object> a) {
			// a[0] = y (output do softmax)
			// a[1] = dy (gradiente do output)
			// dx_i = y_i * (dy_i - Σ_j(dy_j * y_j))
			NumtuTensor y = paraTensor(a.get(0));
			NumtuTensor dy = paraTensor(a.get(1));

			int nd = y.shape.length;
			int ultimo = y.shape[nd - 1];
			int blocos = y.total / ultimo;
			double[] r = new double[y.total];

			for (int b = 0; b < blocos; b++) {
				int base = b * ultimo;
				double soma = 0;
				for (int j = 0; j < ultimo; j++) {
					soma += dy.dados[base + j] * y.dados[base + j];
				}
				for (int j = 0; j < ultimo; j++) {
					r[base + j] = y.dados[base + j] * (dy.dados[base + j] - soma);
				}
			}
			return new NumtuTensor(r, y.shape);
		}
	}
	static class N_RmsnormBackward implements Nativa {
		public Object chamar(List<Object> a) {
			NumtuTensor x = paraTensor(a.get(0));
			NumtuTensor g = paraTensor(a.get(1));
			NumtuTensor dout = paraTensor(a.get(2));

			double eps = 1e-6;
			int nd = x.shape.length;
			int ultimo = x.shape[nd - 1];
			int blocos = x.total / ultimo;

			double[] dx = new double[x.total];
			double[] dg = new double[ultimo];

			for (int b = 0; b < blocos; b++) {
				int base = b * ultimo;
				double ms = 0;
				for (int j = 0; j < ultimo; j++) ms += x.dados[base + j] * x.dados[base + j];
				ms /= ultimo;
				double inv = 1.0 / Math.sqrt(ms + eps);
				double dot = 0;
				for (int j = 0; j < ultimo; j++) {
					double xn = x.dados[base + j] * inv;
					dg[j] += dout.dados[base + j] * xn;
					double dxn = dout.dados[base + j] * g.dados[j];
					dot += dxn * x.dados[base + j];
				}
				dot /= ultimo;
				for (int j = 0; j < ultimo; j++) {
					double dxn = dout.dados[base + j] * g.dados[j];
					dx[base + j] = dxn * inv - x.dados[base + j] * (inv * inv * inv) * dot;
				}
			}

			// ** USA O SHAPE DO DOUT em vez do x **
			java.util.List<Object> r = new java.util.ArrayList<Object>();
			r.add(new NumtuTensor(dx, dout.shape));   // ← dout.shape em vez de x.shape
			r.add(new NumtuTensor(dg, new int[]{ultimo}));
			return r;
		}
	}
	static class N_Obter implements Nativa {
		public Object chamar(List<Object> a) {
			Object o = a.get(0);
			int idx = ((Number) a.get(1)).intValue();
			if (o instanceof NumtuVetor) {
				return Double.valueOf(((NumtuVetor) o).obter(idx));
			}
			if (o instanceof NumtuMatriz) {
				NumtuMatriz m = (NumtuMatriz) o;
				int i = ((Number) a.get(1)).intValue();
				int j = ((Number) a.get(2)).intValue();
				return Double.valueOf(m.obter(i, j));
			}
			// NumtuTensor — acessa pelo flat index
			NumtuTensor t = (NumtuTensor) o;
			return Double.valueOf(t.dados[idx]);
		}
	}
	
	// ---- BACKWARD MATRICIAL ----

	static class N_MatmulBackward implements Nativa {
		public Object chamar(List<Object> a) {
			// a[0] = A (matriz ou tensor)
			// a[1] = B
			// a[2] = dC (gradiente do output de A@B)
			// retorna [dA, dB]
			NumtuTensor A = paraTensor(a.get(0));
			NumtuTensor B = paraTensor(a.get(1));
			NumtuTensor dC = paraTensor(a.get(2));

			// dA = dC @ B^T
			NumtuTensor Bt = B.transposeUltimas();
			NumtuTensor dA = dC.matmul(Bt);

			// dB = A^T @ dC
			NumtuTensor At = A.transposeUltimas();
			NumtuTensor dB = At.matmul(dC);

			List<Object> r = new java.util.ArrayList<Object>();
			r.add(dA);
			r.add(dB);
			return r;
		}
	}

	static class N_EmbeddingBackward implements Nativa {
		public Object chamar(List<Object> a) {
			// a[0] = ids (tensor 1D de índices inteiros)
			// a[1] = vocab_size (int)
			// a[2] = dim (int)
			// a[3] = dOut (gradiente do output do embedding)
			// Retorna dW_emb [vocab, dim] com scatter-add
			NumtuTensor ids = paraTensor(a.get(0));
			int vocab = ((Number) a.get(1)).intValue();
			int dim = ((Number) a.get(2)).intValue();
			NumtuTensor dOut = paraTensor(a.get(3));

			// dOut tem shape [..., dim] e ids tem shape [...]
			// dW_emb [vocab, dim]
			double[] dW = new double[vocab * dim];

			int n = ids.total;
			int stride = dOut.total / n;
			for (int i = 0; i < n; i++) {
				int id = (int) ids.dados[i];
				if (id < 0 || id >= vocab) continue;
				for (int j = 0; j < dim; j++) {
					dW[id * dim + j] += dOut.dados[i * stride + j];
				}
			}
			return new NumtuTensor(dW, new int[]{vocab, dim});
		}
	}

	static class N_CrossEntropyBackward implements Nativa {
		public Object chamar(List<Object> a) {
			// a[0] = probs [..., vocab]
			// a[1] = targets [...] (int)
			// Retorna dlogits = probs - one_hot(target)
			NumtuTensor probs = paraTensor(a.get(0));
			NumtuTensor yb = paraTensor(a.get(1));

			NumtuTensor dlogits = probs.copia();
			int nd = probs.shape.length;
			int V = probs.shape[nd - 1];
			int blocos = probs.total / V;

			for (int b = 0; b < blocos; b++) {
				int alvo = (int) yb.dados[b];
				dlogits.dados[b * V + alvo] -= 1.0;
			}
			return dlogits;
		}
	}
	
    // ---- ESTATÍSTICAS ----
    static class N_Tamanho implements Nativa {
        public Object chamar(List<Object> a) {
            Object o = a.get(0);
            if (o instanceof NumtuVetor) return Double.valueOf(((NumtuVetor) o).tamanho());
            if (o instanceof NumtuMatriz) return Double.valueOf(((NumtuMatriz) o).linhas);
            return Double.valueOf(((NumtuTensor) o).total);
        }
    }
    static class N_Soma implements Nativa {
        public Object chamar(List<Object> a) {
            Object o = a.get(0);
            if (o instanceof NumtuVetor) return Double.valueOf(((NumtuVetor) o).soma());
            return Double.valueOf(((NumtuTensor) o).soma());
        }
    }
    static class N_Media implements Nativa {
        public Object chamar(List<Object> a) {
            Object o = a.get(0);
            if (o instanceof NumtuVetor) return Double.valueOf(((NumtuVetor) o).media());
            return Double.valueOf(((NumtuTensor) o).media());
        }
    }
    static class N_Maximo implements Nativa {
        public Object chamar(List<Object> a) {
            Object o = a.get(0);
            if (o instanceof NumtuVetor) return Double.valueOf(((NumtuVetor) o).maximo());
            return Double.valueOf(((NumtuTensor) o).maximo());
        }
    }
    static class N_Minimo implements Nativa {
        public Object chamar(List<Object> a) {
            return Double.valueOf(((NumtuVetor) a.get(0)).minimo());
        }
    }
    static class N_Argmax implements Nativa {
        public Object chamar(List<Object> a) {
            return Double.valueOf(((NumtuVetor) a.get(0)).argmax());
        }
    }
    static class N_Produto implements Nativa {
        public Object chamar(List<Object> a) {
            return Double.valueOf(((NumtuVetor) a.get(0)).produto());
        }
    }
    static class N_Acumulado implements Nativa {
        public Object chamar(List<Object> a) {
            return ((NumtuVetor) a.get(0)).acumulado();
        }
    }

    // ---- ÁLGEBRA ----
    static class N_ProdutoEscalar implements Nativa {
        public Object chamar(List<Object> a) {
            return Double.valueOf(((NumtuVetor) a.get(0)).produtoEscalar((NumtuVetor) a.get(1)));
        }
    }
    static class N_Norma implements Nativa {
        public Object chamar(List<Object> a) {
            return Double.valueOf(((NumtuVetor) a.get(0)).norma());
        }
    }
    static class N_Distancia implements Nativa {
        public Object chamar(List<Object> a) {
            return Double.valueOf(((NumtuVetor) a.get(0)).distancia((NumtuVetor) a.get(1)));
        }
    }
    static class N_Matmul implements Nativa {
        public Object chamar(List<Object> a) {
            Object x = a.get(0), y = a.get(1);
            if (x instanceof NumtuMatriz && y instanceof NumtuMatriz)
                return ((NumtuMatriz) x).matmul((NumtuMatriz) y);
            if (x instanceof NumtuMatriz && y instanceof NumtuVetor)
                return ((NumtuMatriz) x).matmulVetor((NumtuVetor) y);
            throw new RuntimeException("matmul: tipos incompatíveis");
        }
    }
    static class N_Transpose implements Nativa {
		public Object chamar(List<Object> a) {
			Object o = a.get(0);
			// Se for só 1 argumento e for Matriz, usa transpose de matriz
			if (a.size() == 1) {
				if (o instanceof NumtuMatriz) {
					return ((NumtuMatriz) o).transpose();
				}
				if (o instanceof NumtuTensor) {
					return ((NumtuTensor) o).transposeUltimas();
				}
			}
			// Se tem eixos, é Tensor
			NumtuTensor t = (NumtuTensor) o;
			int[] eixos = new int[a.size() - 1];
			for (int i = 1; i < a.size(); i++) {
				eixos[i - 1] = ((Number) a.get(i)).intValue();
			}
			return t.transpose(eixos);
		}
	}

    // ---- MATRIZ ----
    static class N_Matriz implements Nativa {
        public Object chamar(List<Object> a) {
            return NumtuMatriz.criarDeLista((List<Object>) a.get(0));
        }
    }
    static class N_MatrizZeros implements Nativa {
        public Object chamar(List<Object> a) {
            return NumtuMatriz.zeros(((Number) a.get(0)).intValue(),
                                     ((Number) a.get(1)).intValue());
        }
    }
    static class N_MatrizUns implements Nativa {
        public Object chamar(List<Object> a) {
            return NumtuMatriz.uns(((Number) a.get(0)).intValue(),
                                   ((Number) a.get(1)).intValue());
        }
    }
    static class N_MatrizAleatorio implements Nativa {
        public Object chamar(List<Object> a) {
            return NumtuMatriz.aleatorio(
                ((Number) a.get(0)).intValue(),
                ((Number) a.get(1)).intValue(),
                ((Number) a.get(2)).doubleValue());
        }
    }
    static class N_MatrizAleatorioNormal implements Nativa {
        public Object chamar(List<Object> a) {
            return NumtuMatriz.aleatorioNormal(
                ((Number) a.get(0)).intValue(),
                ((Number) a.get(1)).intValue(),
                ((Number) a.get(2)).doubleValue());
        }
    }
    static class N_Identidade implements Nativa {
        public Object chamar(List<Object> a) {
            return NumtuMatriz.identidade(((Number) a.get(0)).intValue());
        }
    }
    static class N_Linhas implements Nativa {
		public Object chamar(List<Object> a) {
			Object o = a.get(0);
			if (o instanceof NumtuMatriz) {
				return Double.valueOf(((NumtuMatriz) o).linhas);
			}
			NumtuTensor t = (NumtuTensor) o;
			return Double.valueOf(t.shape[0]);
		}
	}

	static class N_Colunas implements Nativa {
		public Object chamar(List<Object> a) {
			Object o = a.get(0);
			if (o instanceof NumtuMatriz) {
				return Double.valueOf(((NumtuMatriz) o).colunas);
			}
			NumtuTensor t = (NumtuTensor) o;
			return Double.valueOf(t.shape[t.shape.length - 1]);
		}
	}
    static class N_Achatar implements Nativa {
        public Object chamar(List<Object> a) {
            return ((NumtuMatriz) a.get(0)).achatar();
        }
    }

    // ---- TENSOR ----
    static class N_TensorZeros implements Nativa {
        public Object chamar(List<Object> a) {
            int[] shape = new int[a.size()];
            for (int i = 0; i < a.size(); i++) shape[i] = ((Number) a.get(i)).intValue();
            return NumtuTensor.zeros(shape);
        }
    }
    static class N_TensorUns implements Nativa {
        public Object chamar(List<Object> a) {
            int[] shape = new int[a.size()];
            for (int i = 0; i < a.size(); i++) shape[i] = ((Number) a.get(i)).intValue();
            return NumtuTensor.uns(shape);
        }
    }
    static class N_TensorAleatorio implements Nativa {
        public Object chamar(List<Object> a) {
            double e = ((Number) a.get(0)).doubleValue();
            int[] shape = new int[a.size() - 1];
            for (int i = 1; i < a.size(); i++) shape[i - 1] = ((Number) a.get(i)).intValue();
            return NumtuTensor.aleatorio(e, shape);
        }
    }
    static class N_Reshape implements Nativa {
        public Object chamar(List<Object> a) {
            NumtuTensor t = (NumtuTensor) a.get(0);
            int[] novo = new int[a.size() - 1];
            for (int i = 1; i < a.size(); i++) novo[i - 1] = ((Number) a.get(i)).intValue();
            return t.reshape(novo);
        }
    }
    static class N_TransposeUltimas implements Nativa {
        public Object chamar(List<Object> a) {
            return ((NumtuTensor) a.get(0)).transposeUltimas();
        }
    }
    static class N_MascaraCausal implements Nativa {
        public Object chamar(List<Object> a) {
            return mascaraCausal((NumtuTensor) a.get(0));
        }
    }
    static class N_MatmulTensor implements Nativa {
        public Object chamar(List<Object> a) {
            return ((NumtuTensor) a.get(0)).matmul((NumtuTensor) a.get(1));
        }
    }
    static class N_SomaEixo implements Nativa {
        public Object chamar(List<Object> a) {
            return ((NumtuTensor) a.get(0)).somaEixo(((Number) a.get(1)).intValue());
        }
    }
    static class N_MediaEixo implements Nativa {
        public Object chamar(List<Object> a) {
            return ((NumtuTensor) a.get(0)).mediaEixo(((Number) a.get(1)).intValue());
        }
    }
    static class N_MaxEixo implements Nativa {
        public Object chamar(List<Object> a) {
            return ((NumtuTensor) a.get(0)).maxEixo(((Number) a.get(1)).intValue());
        }
    }

    static NumtuTensor mascaraCausal(NumtuTensor scores) {
        int nd = scores.shape.length;
        if (nd < 2) throw new RuntimeException("mascara_causal precisa 2D+");
        int T = scores.shape[nd - 1];
        int T2 = scores.shape[nd - 2];
        NumtuTensor r = scores.copia();
        int tamanhoBloco = T2 * T;
        int blocos = r.total / tamanhoBloco;
        for (int b = 0; b < blocos; b++) {
            int base = b * tamanhoBloco;
            for (int i = 0; i < T2; i++)
                for (int j = 0; j < T; j++)
                    if (j > i) r.dados[base + i * T + j] = -1e9;
        }
        return r;
    }

    // ---- UTIL ----
    static class N_Copia implements Nativa {
        public Object chamar(List<Object> a) {
            Object o = a.get(0);
            if (o instanceof NumtuVetor) return ((NumtuVetor) o).copia();
            if (o instanceof NumtuMatriz) return ((NumtuMatriz) o).copia();
            return ((NumtuTensor) o).copia();
        }
    }
    static class N_Igual implements Nativa {
        public Object chamar(List<Object> a) {
            return Boolean.valueOf(((NumtuVetor) a.get(0)).igual((NumtuVetor) a.get(1)));
        }
    }
    static class N_Concatenar implements Nativa {
        public Object chamar(List<Object> a) {
            return NumtuVetor.concatenar((NumtuVetor) a.get(0), (NumtuVetor) a.get(1));
        }
    }
    static class N_TotalParams implements Nativa {
        public Object chamar(List<Object> a) {
            return Double.valueOf(((NumtuVetor) a.get(0)).tamanho());
        }
    }
// ============ OTIMIZAÇÃO / AUTOGRAD ============

	static class N_CrossEntropy implements Nativa {
		public Object chamar(List<Object> a) {
			// probs: [..., vocab]  targets: [...]
			NumtuTensor probs = paraTensor(a.get(0));
			NumtuTensor yb = paraTensor(a.get(1));

			int nd = probs.shape.length;
			int V = probs.shape[nd - 1];
			int blocos = probs.total / V;
			double loss = 0;

			for (int b = 0; b < blocos; b++) {
				int alvo = (int) yb.dados[b];
				double p = probs.dados[b * V + alvo];
				if (p < 1e-30) p = 1e-30;
				loss -= Math.log(p);
			}
			return Double.valueOf(loss / blocos);
		}
	}

	static class N_CriarAdamW implements Nativa {
		public Object chamar(List<Object> a) {
			return new AdamW();
		}
	}

	static class N_AdamWP implements Nativa {
		public Object chamar(List<Object> a) {
			AdamW ot = (AdamW) a.get(0);
			java.util.Map<String, Object> params = castMapa(a.get(1));
			java.util.Map<String, Object> grads = castMapa(a.get(2));
			double lr = ((Number) a.get(3)).doubleValue();
			double wd = a.size() > 4 ? ((Number) a.get(4)).doubleValue() : 0.0;
			ot.passo(params, grads, lr, wd);
			return null;
		}
	}

	static class N_ClipGrad implements Nativa {
		public Object chamar(List<Object> a) {
			java.util.Map<String, Object> grads = castMapa(a.get(0));
			double maxNorm = ((Number) a.get(1)).doubleValue();
			double soma = 0;
			for (Object g : grads.values()) {
				if (g == null) continue;
				double[] arr = AdamW.paraArray(g);
				for (double x : arr) soma += x * x;
			}
			double norma = Math.sqrt(soma);
			if (norma > maxNorm) {
				double f = maxNorm / (norma + 1e-6);
				for (String k : grads.keySet()) {
					Object g = grads.get(k);
					if (g == null) continue;
					double[] arr = AdamW.paraArray(g);
					for (int i = 0; i < arr.length; i++) arr[i] *= f;
					AdamW.escreveArray(g, arr);
				}
			}
			return Double.valueOf(norma);
		}
	}

	static class N_CriaMapa implements Nativa {
		public Object chamar(List<Object> a) {
			return new java.util.HashMap<String, Object>();
		}
	}

	static class N_MapaPega implements Nativa {
		public Object chamar(List<Object> a) {
			return castMapa(a.get(0)).get(a.get(1).toString());
		}
	}

	static class N_MapaPoe implements Nativa {
		public Object chamar(List<Object> a) {
			castMapa(a.get(0)).put(a.get(1).toString(), a.get(2));
			return null;
		}
	}

	static class N_MapaChaves implements Nativa {
		public Object chamar(List<Object> a) {
			List<Object> chaves = new java.util.ArrayList<Object>();
			for (Object k : castMapa(a.get(0)).keySet()) chaves.add(k);
			return chaves;
		}
	}

	@SuppressWarnings("unchecked")
	static java.util.Map<String, Object> castMapa(Object o) {
		return (java.util.Map<String, Object>) o;
	}
}
