package com.tupi.setup.erro;

public class ErroLexer extends ErroTupi {

    public ErroLexer(String mensagem, int linha, int coluna, String dica) {
        super("LEX001", mensagem, linha, coluna, null, dica);
    }

    public ErroLexer(String mensagem, int linha, int coluna) {
        this(mensagem, linha, coluna, null);
    }
}
