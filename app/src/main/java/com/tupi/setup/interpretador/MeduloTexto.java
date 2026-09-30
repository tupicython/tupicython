package com.tupi.setup.interpretador;

import java.util.List;

public class MeduloTexto implements MeduloTupi {

    public String nome() { return "texto"; }

    public void registrar(final Ambiente destino) {
        destino.definir("maiusculo", new Nativa() {
				public Object chamar(List<Object> a) {
					return a.get(0).toString().toUpperCase();
				}
			});
        destino.definir("minusculo", new Nativa() {
				public Object chamar(List<Object> a) {
					return a.get(0).toString().toLowerCase();
				}
			});
        destino.definir("tamanho", new Nativa() {
				public Object chamar(List<Object> a) {
					return Double.valueOf(a.get(0).toString().length());
				}
			});
        destino.definir("contem", new Nativa() {
				public Object chamar(List<Object> a) {
					return Boolean.valueOf(
						a.get(0).toString().contains(a.get(1).toString()));
				}
			});
        destino.definir("substituir", new Nativa() {
				public Object chamar(List<Object> a) {
					return a.get(0).toString().replace(
						a.get(1).toString(), a.get(2).toString());
				}
			});
        destino.definir("dividir", new Nativa() {
				public Object chamar(List<Object> a) {
					String[] partes = a.get(0).toString().split(
						java.util.regex.Pattern.quote(a.get(1).toString()));
					List<Object> lista = new java.util.ArrayList<Object>();
					for (String s : partes) lista.add(s);
					return lista;
				}
			});
        destino.definir("aparar", new Nativa() {
				public Object chamar(List<Object> a) {
					return a.get(0).toString().trim();
				}
			});
        destino.definir("repete", new Nativa() {
				public Object chamar(List<Object> a) {
					int n = (int) ((Double) a.get(1)).doubleValue();
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < n; i++) sb.append(a.get(0).toString());
					return sb.toString();
				}
			});

        // ==================== NOVAS FUNÇÕES (FIX) ====================

        // texto.caractere(s, i) — pega caractere por índice
        destino.definir("caractere", new Nativa() {
				public Object chamar(List<Object> a) {
					String s = a.get(0).toString();
					int i = (int) ((Double) a.get(1)).doubleValue();
					if (i < 0 || i >= s.length()) {
						throw new RuntimeException(
							"Índice " + i + " fora do texto (tamanho=" + s.length() + ")"
						);
					}
					return String.valueOf(s.charAt(i));
				}
			});

        // texto.termina_com(s, sufixo)
        destino.definir("termina_com", new Nativa() {
				public Object chamar(List<Object> a) {
					return Boolean.valueOf(
						a.get(0).toString().endsWith(a.get(1).toString()));
				}
			});

        // texto.comeca_com(s, prefixo)
        destino.definir("comeca_com", new Nativa() {
				public Object chamar(List<Object> a) {
					return Boolean.valueOf(
						a.get(0).toString().startsWith(a.get(1).toString()));
				}
			});

        // texto.indice(s, sub) — retorna posição ou -1
        destino.definir("indice", new Nativa() {
				public Object chamar(List<Object> a) {
					return Double.valueOf(
						a.get(0).toString().indexOf(a.get(1).toString()));
				}
			});

        // texto.fatia(s, ini, fim) — pedaço do texto
        destino.definir("fatia", new Nativa() {
				public Object chamar(List<Object> a) {
					String s = a.get(0).toString();
					int ini = (int) ((Double) a.get(1)).doubleValue();
					int fim = (int) ((Double) a.get(2)).doubleValue();
					if (ini < 0) ini = 0;
					if (fim > s.length()) fim = s.length();
					if (ini >= fim) return "";
					return s.substring(ini, fim);
				}
			});

        // texto.juntar(lista, separador) — oposto de dividir
        destino.definir("juntar", new Nativa() {
				public Object chamar(List<Object> a) {
					List<?> lista = (List<?>) a.get(0);
					String sep = a.get(1).toString();
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < lista.size(); i++) {
						if (i > 0) sb.append(sep);
						sb.append(lista.get(i).toString());
					}
					return sb.toString();
				}
			});
    }
}
