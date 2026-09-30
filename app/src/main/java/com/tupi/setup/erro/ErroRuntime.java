package com.tupi.setup.erro;

public class ErroRuntime extends ErroTupi {

    public ErroRuntime(String mensagem, int linha, String dica) {
        super("RUN001", mensagem, linha, 0, null, dica);
    }

    public ErroRuntime(String mensagem, int linha) {
        this(mensagem, linha, null);
    }

    public ErroRuntime(String mensagem) {
        this(mensagem, 0, null);
    }
}
