package com.tupi.setup.interpretador;

public class RetornoException extends RuntimeException {
    public final Object valor;
    public RetornoException(Object v) {
        super(null, null, false, false);
        this.valor = v;
    }
}
