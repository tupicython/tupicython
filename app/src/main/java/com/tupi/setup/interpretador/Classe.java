package com.tupi.setup.interpretador;

import com.tupi.setup.ast.Instr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Classe {

    public final String nome;
    public final Classe pai;
    public final Map<String, Instr.MetodoClasse> metodos =
	new HashMap<String, Instr.MetodoClasse>();
    public final Instr.MetodoClasse construtor;
    public final Ambiente ambienteDefinicao;

    public Classe(String nome, Classe pai,
                  List<Instr.MetodoClasse> metodos,
                  Ambiente ambienteDefinicao) {
        this.nome = nome;
        this.pai = pai;
        this.ambienteDefinicao = ambienteDefinicao;
        Instr.MetodoClasse ctor = null;
        for (int i = 0; i < metodos.size(); i++) {
            Instr.MetodoClasse m = metodos.get(i);
            if (m.ehConstrutor) ctor = m;
            else this.metodos.put(m.nome, m);
        }
        // ⬅️ MUDANÇA: se não tem construtor próprio, herda do pai
        if (ctor == null && pai != null) {
            ctor = pai.construtor;
        }
        this.construtor = ctor;
    }

    public Instr.MetodoClasse buscarMetodo(String nome) {
        if (metodos.containsKey(nome)) return metodos.get(nome);
        if (pai != null) return pai.buscarMetodo(nome);
        return null;
    }

    public String toString() { return "<classe " + nome + ">"; }
}
