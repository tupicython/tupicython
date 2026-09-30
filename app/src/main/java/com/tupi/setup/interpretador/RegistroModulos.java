package com.tupi.setup.interpretador;

import java.util.HashMap;
import java.util.Map;

public class RegistroModulos {

    private static final Map<String, MeduloTupi> modulos =
	new HashMap<String, MeduloTupi>();

    public static void registrar(MeduloTupi m) {
        modulos.put(m.nome(), m);
    }

    public static boolean existe(String nome) {
        return modulos.containsKey(nome);
    }

    public static ModoloNativo carregar(String nome) {
        MeduloTupi m = modulos.get(nome);
        if (m == null) return null;
        ModoloNativo modulo = new ModoloNativo(nome);
        Ambiente temp = new Ambiente();
        m.registrar(temp);
        modulo.membros.putAll(temp.variaveis);
        return modulo;
    }
}
