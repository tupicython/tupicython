package com.tupi.setup.interpretador;

import java.util.List;

public class ModuloMatematica implements MeduloTupi {

    public String nome() { return "matematica"; }

    public void registrar(final Ambiente destino) {
        destino.definir("pi", Double.valueOf(Math.PI));
        destino.definir("numero_e", Double.valueOf(Math.E));
        destino.definir("raiz", new Nativa() {
				public Object chamar(List<Object> a) {
					double x = ((Double) a.get(0)).doubleValue();
					if (x < 0) throw new RuntimeException("Raiz de número negativo!");
					return Double.valueOf(Math.sqrt(x));
				}
			});
        destino.definir("potencia", new Nativa() {
				public Object chamar(List<Object> a) {
					double b = ((Double) a.get(0)).doubleValue();
					double e = ((Double) a.get(1)).doubleValue();
					return Double.valueOf(Math.pow(b, e));
				}
			});
        destino.definir("seno", new Nativa() {
				public Object chamar(List<Object> a) {
					return Double.valueOf(Math.sin(((Double) a.get(0)).doubleValue()));
				}
			});
        destino.definir("cosseno", new Nativa() {
				public Object chamar(List<Object> a) {
					return Double.valueOf(Math.cos(((Double) a.get(0)).doubleValue()));
				}
			});
        destino.definir("tangente", new Nativa() {
				public Object chamar(List<Object> a) {
					return Double.valueOf(Math.tan(((Double) a.get(0)).doubleValue()));
				}
			});
        destino.definir("absoluto", new Nativa() {
				public Object chamar(List<Object> a) {
					return Double.valueOf(Math.abs(((Double) a.get(0)).doubleValue()));
				}
			});
        destino.definir("minimo", new Nativa() {
				public Object chamar(List<Object> a) {
					double x = ((Double) a.get(0)).doubleValue();
					double y = ((Double) a.get(1)).doubleValue();
					return Double.valueOf(Math.min(x, y));
				}
			});
        destino.definir("maximo", new Nativa() {
				public Object chamar(List<Object> a) {
					double x = ((Double) a.get(0)).doubleValue();
					double y = ((Double) a.get(1)).doubleValue();
					return Double.valueOf(Math.max(x, y));
				}
			});
        destino.definir("arredondar", new Nativa() {
				public Object chamar(List<Object> a) {
					return Double.valueOf(Math.round(((Double) a.get(0)).doubleValue()));
				}
			});
        destino.definir("piso", new Nativa() {
				public Object chamar(List<Object> a) {
					return Double.valueOf(Math.floor(((Double) a.get(0)).doubleValue()));
				}
			});
        destino.definir("teto", new Nativa() {
				public Object chamar(List<Object> a) {
					return Double.valueOf(Math.ceil(((Double) a.get(0)).doubleValue()));
				}
			});
        destino.definir("log", new Nativa() {
				public Object chamar(List<Object> a) {
					return Double.valueOf(Math.log(((Double) a.get(0)).doubleValue()));
				}
			});
		// ============ NOVAS FUNÇÕES ============

// matematica.aleatorio() — número entre 0 e 1
		destino.definir("aleatorio", new Nativa() {
				public Object chamar(List<Object> a) {
					return Double.valueOf(Math.random());
				}
			});

// matematica.aleatorio_entre(min, max) — inteiro entre min e max
		destino.definir("aleatorio_entre", new Nativa() {
				public Object chamar(List<Object> a) {
					int min = (int) ((Double) a.get(0)).doubleValue();
					int max = (int) ((Double) a.get(1)).doubleValue();
					int faixa = max - min + 1;
					return Double.valueOf(Math.floor(Math.random() * faixa) + min);
				}
			});

// matematica.semente(n) — fixa o random (útil pra teste)
// (opcional — precisa trocar Math.random por Random)
    }
	
}
