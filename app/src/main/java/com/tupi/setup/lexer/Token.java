package com.tupi.setup.lexer;

public class Token {
    public final TipoToken tipo;
    public final String lexema;
    public final Object literal;
    public final int linha;

    public Token(TipoToken tipo, String lexema, Object literal, int linha) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
        this.linha = linha;
    }

    public String toString() {
        return "[" + tipo + " '" + lexema + "' linha " + linha + "]";
    }
}
