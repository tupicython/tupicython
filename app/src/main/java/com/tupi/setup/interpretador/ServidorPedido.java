package com.tupi.setup.interpretador;

import java.util.HashMap;
import java.util.Map;

public class ServidorPedido {

    public String metodo;
    public String caminho;
    public String queryBruta;
    public Map<String, String> parametros = new HashMap<String, String>();
    public String corpo = "";

    public ServidorPedido(String metodo, String caminho, String query,
                          Map<String, String> params, String corpo) {
        this.metodo = metodo;
        this.caminho = caminho;
        this.queryBruta = query;
        this.parametros = params;
        this.corpo = corpo;
    }
}
