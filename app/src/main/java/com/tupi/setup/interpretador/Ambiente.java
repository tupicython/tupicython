package com.tupi.setup.interpretador;

import java.util.HashMap;
import java.util.Map;

public class Ambiente {
    final Ambiente pai;
    final Map<String, Object> variaveis = new HashMap<String, Object>();

    public Ambiente() { this(null); }
    public Ambiente(Ambiente p) { this.pai = p; }

    public void definir(String nome, Object valor) {
        variaveis.put(nome, valor);
    }

    public Object obter(String nome) {
        if (variaveis.containsKey(nome)) return variaveis.get(nome);
        if (pai != null) return pai.obter(nome);
        throw new RuntimeException("Variável '" + nome + "' não definida");
    }

    public void atribuir(String nome, Object valor) {
        if (variaveis.containsKey(nome)) { variaveis.put(nome, valor); return; }
        if (pai != null) { pai.atribuir(nome, valor); return; }
        throw new RuntimeException("Variável '" + nome + "' não definida");
    }
}
