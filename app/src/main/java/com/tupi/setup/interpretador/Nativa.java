package com.tupi.setup.interpretador;

import java.util.List;

public interface Nativa {
    Object chamar(List<Object> args);
}
