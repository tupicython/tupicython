package com.tupi.setup.interpretador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServidorApp {

    public static class Rota {
        String metodo;
        String caminho;
        Object funcao;
        public Rota(String m, String c, Object f) {
            metodo = m;
            caminho = c;
            funcao = f;
        }
    }

    private final List<Rota> rotas = new ArrayList<Rota>();
    private ServerSocket servidor;
    private Thread thread;
    private volatile boolean rodando = false;
    private int portaAtual = -1;
    private interpretador interp;

    public void adicionarRota(String metodo, String caminho, Object funcao) {
        rotas.add(new Rota(metodo.toUpperCase(), caminho, funcao));
    }

    public int numeroRotas() {
        return rotas.size();
    }

    public int porta() {
        return portaAtual;
    }

    public boolean estaRodando() {
        return rodando;
    }

    public void iniciar(final interpretador interp, final int porta) throws IOException {
        this.interp = interp;
        this.portaAtual = porta;
        this.rodando = true;

        thread = new Thread(new Runnable() {
				public void run() {
					try {
						servidor = new ServerSocket(porta);
						while (rodando) {
							try {
								Socket cliente = servidor.accept();
								tratarConexao(cliente);
							} catch (Exception e) {
								if (rodando) {
									System.out.println("Erro: " + e.getMessage());
								}
							}
						}
					} catch (Exception e) {
						if (rodando) {
							System.out.println("Erro no servidor: " + e.getMessage());
						}
					}
				}
			});
        thread.start();
    }

    public void parar() {
        rodando = false;
        try {
            if (servidor != null) {
                servidor.close();
                servidor = null;
            }
        } catch (Exception ignored) {}
        portaAtual = -1;
    }

    private void tratarConexao(Socket cliente) throws IOException {
        InputStream entrada = cliente.getInputStream();
        OutputStream saida = cliente.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(entrada, "UTF-8"));

        // 1) Lê a primeira linha: "GET /soma?a=5&b=3 HTTP/1.1"
        String primeiraLinha = reader.readLine();
        if (primeiraLinha == null || primeiraLinha.isEmpty()) {
            cliente.close();
            return;
        }

        String[] partes = primeiraLinha.split(" ");
        String metodo = partes.length > 0 ? partes[0].toUpperCase() : "GET";
        String caminhoCompleto = partes.length > 1 ? partes[1] : "/";

        String caminho = caminhoCompleto;
        String query = null;
        int interrogacao = caminhoCompleto.indexOf('?');
        if (interrogacao >= 0) {
            caminho = caminhoCompleto.substring(0, interrogacao);
            query = caminhoCompleto.substring(interrogacao + 1);
        }

        // 2) Lê headers até linha vazia
        int contentLength = 0;
        String linha;
        while ((linha = reader.readLine()) != null) {
            if (linha.isEmpty()) break;
            if (linha.toLowerCase().startsWith("content-length:")) {
                try {
                    contentLength = Integer.parseInt(linha.substring(15).trim());
                } catch (Exception ignored) {}
            }
        }

        // 3) Lê corpo (POST)
        String corpo = "";
        if (contentLength > 0) {
            char[] buffer = new char[contentLength];
            int lidos = 0;
            while (lidos < contentLength) {
                int r = reader.read(buffer, lidos, contentLength - lidos);
                if (r == -1) break;
                lidos += r;
            }
            corpo = new String(buffer, 0, lidos);
        }

        // 4) Acha rota
        Rota encontrada = null;
        for (Rota r : rotas) {
            if (r.metodo.equals(metodo) && r.caminho.equals(caminho)) {
                encontrada = r;
                break;
            }
        }

        if (encontrada == null) {
            responder(saida, 404, "Rota nao encontrada: " + metodo + " " + caminho);
            cliente.close();
            return;
        }

        // 5) Chama função TupiCython
        try {
            Map<String, String> params = new HashMap<String, String>();
            if (query != null && !query.isEmpty()) {
                for (String par : query.split("&")) {
                    String[] kv = par.split("=", 2);
                    String chave = URLDecoder.decode(kv[0], "UTF-8");
                    String valor = kv.length > 1 ? URLDecoder.decode(kv[1], "UTF-8") : "";
                    params.put(chave, valor);
                }
            }

            ServidorPedido pedido = new ServidorPedido(metodo, caminho, query, params, corpo);

            List<Object> args = new ArrayList<Object>();
            args.add(pedido);
            Object resultado = interp.chamarFuncao(encontrada.funcao, args);

            String resposta = interp.formatar(resultado);
            responder(saida, 200, resposta);

        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg == null) msg = e.getClass().getSimpleName();
            responder(saida, 500, "Erro: " + msg);
        }

        cliente.close();
    }

    private void responder(OutputStream saida, int status, String corpo) throws IOException {
        String statusTexto;
        if (status == 200) statusTexto = "200 OK";
        else if (status == 404) statusTexto = "404 Not Found";
        else if (status == 500) statusTexto = "500 Internal Server Error";
        else statusTexto = status + " Status";

        byte[] bytes = corpo.getBytes("UTF-8");

        String cabecalho =
            "HTTP/1.1 " + statusTexto + "\r\n" +
            "Content-Type: text/plain; charset=UTF-8\r\n" +
            "Content-Length: " + bytes.length + "\r\n" +
            "Connection: close\r\n" +
            "\r\n";

        saida.write(cabecalho.getBytes("UTF-8"));
        saida.write(bytes);
        saida.flush();
    }
	// ==================== HTTP CLIENT ====================

// Aceita SSL (Android antigo)
	private static void aceitarSSL() {
		try {
			javax.net.ssl.TrustManager[] trustAll = new javax.net.ssl.TrustManager[] {
				new javax.net.ssl.X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
					public void checkClientTrusted(java.security.cert.X509Certificate[] c, String a) {}
					public void checkServerTrusted(java.security.cert.X509Certificate[] c, String a) {}
				}
			};
			javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("TLS");
			sc.init(null, trustAll, new java.security.SecureRandom());
			javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
				new javax.net.ssl.HostnameVerifier() {
					public boolean verify(String hostname, javax.net.ssl.SSLSession session) {
						return true;
					}
				}
			);
		} catch (Exception ignored) {}
	}

// Requisição GET
	public static String httpGet(String url, java.util.Map<String, String> headers) {
		try {
			aceitarSSL();
			java.net.URL u = new java.net.URL(url);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) u.openConnection();
			conn.setRequestMethod("GET");
			conn.setInstanceFollowRedirects(true);
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(15000);

			if (headers != null) {
				for (java.util.Map.Entry<String, String> h : headers.entrySet()) {
					conn.setRequestProperty(h.getKey(), h.getValue());
				}
			}

			int codigo = conn.getResponseCode();
			InputStream in = (codigo >= 200 && codigo < 300)
				? conn.getInputStream()
				: conn.getErrorStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String linha;
			while ((linha = reader.readLine()) != null) {
				sb.append(linha).append("\n");
			}
			reader.close();
			conn.disconnect();
			return sb.toString();
		} catch (Exception e) {
			return "❌ Erro: " + e.getMessage();
		}
	}

// Requisição POST
	public static String httpPost(String url, String corpo, java.util.Map<String, String> headers) {
		try {
			aceitarSSL();
			java.net.URL u = new java.net.URL(url);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) u.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(true);
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);

			if (headers != null) {
				for (java.util.Map.Entry<String, String> h : headers.entrySet()) {
					conn.setRequestProperty(h.getKey(), h.getValue());
				}
			}

			byte[] bytes = corpo.getBytes("UTF-8");
			conn.setRequestProperty("Content-Length", String.valueOf(bytes.length));
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.flush();
			out.close();

			int codigo = conn.getResponseCode();
			InputStream in = (codigo >= 200 && codigo < 300)
				? conn.getInputStream()
				: conn.getErrorStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String linha;
			while ((linha = reader.readLine()) != null) {
				sb.append(linha).append("\n");
			}
			reader.close();
			conn.disconnect();
			return sb.toString();
		} catch (Exception e) {
			return "❌ Erro: " + e.getMessage();
		}
	}

// Parseia JSON simples (retorna Map ou List)
	public static Object jsonParse(String texto) {
		try {
			String s = texto.trim();
			if (s.startsWith("{")) {
				java.util.Map<String, Object> mapa = new java.util.HashMap<String, Object>();
				s = s.substring(1, s.length() - 1).trim();
				// Parse simples (não funciona com nested)
				for (String par : s.split(",")) {
					String[] kv = par.split(":", 2);
					if (kv.length == 2) {
						String chave = kv[0].trim().replace("\"", "");
						String valor = kv[1].trim().replace("\"", "");
						mapa.put(chave, valor);
					}
				}
				return mapa;
			} else if (s.startsWith("[")) {
				java.util.List<Object> lista = new java.util.ArrayList<Object>();
				s = s.substring(1, s.length() - 1).trim();
				for (String item : s.split(",")) {
					lista.add(item.trim().replace("\"", ""));
				}
				return lista;
			}
			return texto;
		} catch (Exception e) {
			return texto;
		}
	}

// Gera JSON de um Map/List
	public static String jsonGerar(Object obj) {
		if (obj == null) return "null";
		if (obj instanceof java.util.Map) {
			StringBuilder sb = new StringBuilder("{");
			java.util.Map<?, ?> m = (java.util.Map<?, ?>) obj;
			boolean primeiro = true;
			for (java.util.Map.Entry<?, ?> e : m.entrySet()) {
				if (!primeiro) sb.append(",");
				sb.append("\"").append(e.getKey()).append("\":");
				sb.append(jsonGerar(e.getValue()));
				primeiro = false;
			}
			sb.append("}");
			return sb.toString();
		}
		if (obj instanceof java.util.List) {
			StringBuilder sb = new StringBuilder("[");
			java.util.List<?> l = (java.util.List<?>) obj;
			boolean primeiro = true;
			for (Object item : l) {
				if (!primeiro) sb.append(",");
				sb.append(jsonGerar(item));
				primeiro = false;
			}
			sb.append("]");
			return sb.toString();
		}
		if (obj instanceof String) return "\"" + obj + "\"";
		return obj.toString();
	}
	
}
