package com.tupi.setup.interpretador;

import com.tupi.setup.ast.Instr;

public class MetadoLigado {
    public final Instancia instancia;
    public final Instr.MetodoClasse metodo;

    public MetadoLigado(Instancia inst, Instr.MetodoClasse m) {
        this.instancia = inst;
        this.metodo = m;
    }

    public String toString() {
        return "<metodo " + metodo.nome + " de " + instancia.classe.nome + ">";
    }
}
