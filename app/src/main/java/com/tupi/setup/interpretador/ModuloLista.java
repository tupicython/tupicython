package com.tupi.setup.interpretador;

import java.util.Collections;
import java.util.List;

public class ModuloLista implements MeduloTupi {

    public String nome() { return "lista"; }

    @SuppressWarnings("unchecked")
    public void registrar(final Ambiente destino) {
        destino.definir("tamanho", new Nativa() {
				public Object chamar(List<Object> a) {
					return Double.valueOf(((List<Object>) a.get(0)).size());
				}
			});
        destino.definir("adicionar", new Nativa() {
				public Object chamar(List<Object> a) {
					((List<Object>) a.get(0)).add(a.get(1));
					return null;
				}
			});
        destino.definir("inserir", new Nativa() {
				public Object chamar(List<Object> a) {
					int idx = (int) ((Double) a.get(1)).doubleValue();
					((List<Object>) a.get(0)).add(idx, a.get(2));
					return null;
				}
			});
        destino.definir("remover", new Nativa() {
				public Object chamar(List<Object> a) {
					int idx = (int) ((Double) a.get(1)).doubleValue();
					return ((List<Object>) a.get(0)).remove(idx);
				}
			});
        destino.definir("contem", new Nativa() {
				public Object chamar(List<Object> a) {
					return Boolean.valueOf(((List<Object>) a.get(0)).contains(a.get(1)));
				}
			});
        destino.definir("indice", new Nativa() {
				public Object chamar(List<Object> a) {
					return Double.valueOf(((List<Object>) a.get(0)).indexOf(a.get(1)));
				}
			});
        destino.definir("inverter", new Nativa() {
				public Object chamar(List<Object> a) {
					Collections.reverse((List<Object>) a.get(0));
					return null;
				}
			});
        destino.definir("ordenar", new Nativa() {
				public Object chamar(List<Object> a) {
					Collections.sort((List<Object>) a.get(0), new java.util.Comparator<Object>() {
							public int compare(Object x, Object y) {
								if (x instanceof Number && y instanceof Number) {
									return Double.compare(((Number) x).doubleValue(),
														  ((Number) y).doubleValue());
								}
								return x.toString().compareTo(y.toString());
							}
						});
					return null;
				}
			});
        destino.definir("limpar", new Nativa() {
				public Object chamar(List<Object> a) {
					((List<Object>) a.get(0)).clear();
					return null;
				}
			});
    }
}
