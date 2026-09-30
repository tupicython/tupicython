package com.tupi.setup.interpretador;

import java.util.List;

public class ModuloServidor implements MeduloTupi {

    public String nome() { return "Servidor"; }

    public void registrar(final Ambiente destino) {
        // Servidor.novo() — cria app HTTP
        destino.definir("novo", new Nativa() {
				public Object chamar(List<Object> a) {
					return new ServidorApp();
				}
			});

        // Servidor.get(url) — requisição GET
        destino.definir("get", new Nativa() {
				public Object chamar(List<Object> a) {
					String url = a.get(0).toString();
					return ServidorApp.httpGet(url, null);
				}
			});

        // Servidor.get_com_headers(url, headers)
        destino.definir("get_com_headers", new Nativa() {
				public Object chamar(List<Object> a) {
					String url = a.get(0).toString();
					java.util.Map<String, String> headers = null;
					if (a.size() > 1 && a.get(1) instanceof java.util.Map) {
						headers = new java.util.HashMap<String, String>();
						java.util.Map<?, ?> m = (java.util.Map<?, ?>) a.get(1);
						for (java.util.Map.Entry<?, ?> e : m.entrySet()) {
							headers.put(e.getKey().toString(), e.getValue().toString());
						}
					}
					return ServidorApp.httpGet(url, headers);
				}
			});

        // Servidor.post(url, corpo, headers)
        destino.definir("post", new Nativa() {
				public Object chamar(List<Object> a) {
					String url = a.get(0).toString();
					String corpo = a.get(1).toString();
					java.util.Map<String, String> headers = null;
					if (a.size() > 2 && a.get(2) instanceof java.util.Map) {
						headers = new java.util.HashMap<String, String>();
						java.util.Map<?, ?> m = (java.util.Map<?, ?>) a.get(2);
						for (java.util.Map.Entry<?, ?> e : m.entrySet()) {
							headers.put(e.getKey().toString(), e.getValue().toString());
						}
					}
					return ServidorApp.httpPost(url, corpo, headers);
				}
			});

        // Servidor.json(texto) — parseia JSON simples
        destino.definir("json", new Nativa() {
				public Object chamar(List<Object> a) {
					return ServidorApp.jsonParse(a.get(0).toString());
				}
			});

        // Servidor.json_para_texto(objeto) — gera JSON
        destino.definir("json_para_texto", new Nativa() {
				public Object chamar(List<Object> a) {
					return ServidorApp.jsonGerar(a.get(0));
				}
			});
		// Servidor.post_simples(url, corpo, auth)
		destino.definir("post_simples", new Nativa() {
				public Object chamar(List<Object> a) {
					String url = a.get(0).toString();
					String corpo = a.get(1).toString();
					String auth = a.size() > 2 ? a.get(2).toString() : "";

					java.util.Map<String, String> headers = new java.util.HashMap<String, String>();
					headers.put("Content-Type", "application/json");
					if (!auth.equals("")) {
						headers.put("Authorization", auth);
					}

					return ServidorApp.httpPost(url, corpo, headers);
				}
			});
			
    }
}
