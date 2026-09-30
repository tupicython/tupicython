package com.tupi.setup.interpretador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ModuloTupim implements MeduloTupi {

    private static final String REPO_OFICIAL = 
    "https://raw.githubusercontent.com/tupicython/tupicython/main/";
	
    private final File pastaModulos;
    private final File pastaRepositorio;

    public ModuloTupim(File pastaModulos, File pastaRepositorio) {
        this.pastaModulos = pastaModulos;
        this.pastaRepositorio = pastaRepositorio;
    }

    public String nome() { return "tupim"; }

    public void registrar(final Ambiente destino) {

        // tupim.instalar("nome") — local ou oficial
        destino.definir("instalar", new Nativa() {
				public Object chamar(List<Object> a) {
					return instalar(a.get(0).toString());
				}
			});

        // tupim.instalar_url("url")
        destino.definir("instalar_url", new Nativa() {
				public Object chamar(List<Object> a) {
					return instalarURL(a.get(0).toString());
					
				}
			});

        // tupim.instalar_arquivo("/caminho")
        destino.definir("instalar_arquivo", new Nativa() {
				public Object chamar(List<Object> a) {
					return instalarArquivo(a.get(0).toString());
				}
			});

        // tupim.remover("nome")
        destino.definir("remover", new Nativa() {
				public Object chamar(List<Object> a) {
					return remover(a.get(0).toString());
				}
			});

        // tupim.listar()
        destino.definir("listar", new Nativa() {
				public Object chamar(List<Object> a) {
					return listar();
				}
			});

        // tupim.disponiveis() — só do repositorio/ local
        destino.definir("disponiveis", new Nativa() {
				public Object chamar(List<Object> a) {
					return disponiveis();
				}
			});
    }

    // ============ INSTALAR (local + oficial) ============
    private String instalar(String nome) {
        // 1) Tenta local primeiro
        File local = new File(pastaRepositorio, nome + ".tupi");
        if (local.exists()) {
            return copiarLocal(local, nome);
        }

        // 2) Se não tem, baixa do repositório oficial
        return baixarDeURL(REPO_OFICIAL + nome + ".tupi", nome);
    }

    // ============ INSTALAR DE URL ============
    private String instalarURL(String url) {
		
        // Extrai nome do arquivo
        String nome = url.substring(url.lastIndexOf('/') + 1);
        if (nome.endsWith(".tupi")) {
            nome = nome.substring(0, nome.length() - 5);
        }
        return baixarDeURL(url, nome);
		
    }

    // ============ INSTALAR DE ARQUIVO LOCAL ============
    private String instalarArquivo(String caminho) {
        File origem = new File(caminho);
        if (!origem.exists()) {
            return "❌ Arquivo nao existe: " + caminho;
        }
        String nome = origem.getName();
        if (nome.endsWith(".tupi")) {
            nome = nome.substring(0, nome.length() - 5);
        }
        return copiarLocal(origem, nome);
    }

    // ============ AUXILIAR: copia local ============
    private String copiarLocal(File origem, String nome) {
        File destino = new File(pastaModulos, nome + ".tupi");
        if (destino.exists()) {
            return "⚠️ Modulo '" + nome + "' ja instalado";
        }
        try {
            InputStream in = new FileInputStream(origem);
            OutputStream out = new FileOutputStream(destino);
            byte[] buffer = new byte[4096];
            int lidos;
            while ((lidos = in.read(buffer)) > 0) {
                out.write(buffer, 0, lidos);
            }
            in.close();
            out.close();
            return "✅ Modulo '" + nome + "' instalado!";
        } catch (Exception e) {
            return "❌ Erro: " + e.getMessage();
        }
    }

    // ============ AUXILIAR: baixa da URL ============
	private String baixarDeURL(String url, String nome) {
		File destino = new File(pastaModulos, nome + ".tupi");
		if (destino.exists()) {
			return "⚠️ Modulo '" + nome + "' ja instalado";
		}

		try {
			// Aceita qualquer certificado SSL (Android antigo)
			javax.net.ssl.TrustManager[] trustAll = new javax.net.ssl.TrustManager[] {
				new javax.net.ssl.X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
					public void checkClientTrusted(java.security.cert.X509Certificate[] c, String a) {}
					public void checkServerTrusted(java.security.cert.X509Certificate[] c, String a) {}
				}
			};
			try {
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

			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setRequestMethod("GET");
			conn.setInstanceFollowRedirects(true);
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);

			int codigo = conn.getResponseCode();
			if (codigo != 200) {
				return "❌ Erro HTTP " + codigo + ": modulo '" + nome + "' nao encontrado";
			}

			InputStream in = conn.getInputStream();
			FileOutputStream out = new FileOutputStream(destino);
			byte[] buffer = new byte[4096];
			int lidos;
			while ((lidos = in.read(buffer)) > 0) {
				out.write(buffer, 0, lidos);
			}
			in.close();
			out.close();
			conn.disconnect();

			return "✅ Modulo '" + nome + "' baixado e instalado!";
		} catch (Exception e) {
			return "❌ Erro: " + e.getMessage();
		}
	}

    // ============ REMOVER ============
    private String remover(String nome) {
        File arquivo = new File(pastaModulos, nome + ".tupi");
        if (!arquivo.exists()) {
            return "❌ Modulo '" + nome + "' nao instalado";
        }
        if (arquivo.delete()) {
            return "✅ Modulo '" + nome + "' removido";
        }
        return "❌ Erro ao remover";
    }

    // ============ LISTAR INSTALADOS ============
    private String listar() {
        File[] arquivos = pastaModulos.listFiles();
        if (arquivos == null || arquivos.length == 0) {
            return "Nenhum modulo instalado.";
        }
        StringBuilder sb = new StringBuilder("📦 Instalados:\n");
        for (File f : arquivos) {
            if (f.getName().endsWith(".tupi")) {
                sb.append("  • ").append(f.getName()).append("\n");
            }
        }
        return sb.toString();
    }

    // ============ DISPONIVEIS (repositorio local) ============
    private String disponiveis() {
        File[] arquivos = pastaRepositorio.listFiles();
        if (arquivos == null || arquivos.length == 0) {
            return "Nenhum modulo disponivel localmente.\n" +
				"Use tupim.instalar(\"nome\") pra baixar do oficial.";
        }
        StringBuilder sb = new StringBuilder("📚 Disponiveis (local):\n");
        for (File f : arquivos) {
            if (f.getName().endsWith(".tupi")) {
                sb.append("  • ").append(f.getName()).append("\n");
            }
        }
        return sb.toString();
    }
}
