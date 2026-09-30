package com.tupi.setup.erro;

public class ErroParser extends ErroTupi {

    public ErroParser(String mensagem, int linha, int coluna, String dica) {
        super("PAR001", mensagem, linha, coluna, null, dica);
    }

    public ErroParser(String mensagem, int linha, int coluna) {
        this(mensagem, linha, coluna, null);
    }
}
