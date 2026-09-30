package com.tupi.setup.interpretador;

import com.tupi.setup.ast.Instr;
import java.util.List;

public class FuncaoTupi {
    public final String nome;
    public final List<String> params;
    public final Instr.Bloco corpo;
    public final Ambiente closure;

    public FuncaoTupi(String n, List<String> p, Instr.Bloco c, Ambiente cl) {
        nome = n; params = p; corpo = c; closure = cl;
    }

    public String toString() { return "<funcao " + nome + ">"; }
}
