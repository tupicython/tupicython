package com.tupi.setup.interpretador;

import java.util.HashMap;
import java.util.Map;

public class Instancia {

    public final Classe classe;
    public final Map<String, Object> campos = new HashMap<String, Object>();

    public Instancia(Classe classe) {
        this.classe = classe;
    }

    public String toString() {
        return "<" + classe.nome + ">";
    }
}
