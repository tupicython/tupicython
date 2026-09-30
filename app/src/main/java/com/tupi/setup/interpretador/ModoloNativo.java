package com.tupi.setup.interpretador;

import java.util.HashMap;
import java.util.Map;

public class ModoloNativo {
    public final String nome;
    public final Map<String, Object> membros = new HashMap<String, Object>();

    public ModoloNativo(String nome) { this.nome = nome; }

    public String toString() { return "<modulo " + nome + ">"; }
}
