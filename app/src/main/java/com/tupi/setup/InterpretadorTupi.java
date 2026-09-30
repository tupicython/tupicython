package com.tupi.setup;

import com.tupi.setup.ast.Instr;
import com.tupi.setup.interpretador.InterfaceSaida;
import com.tupi.setup.interpretador.InterfaceUsuario;
import com.tupi.setup.interpretador.interpretador;
import com.tupi.setup.lexer.Lexer;
import com.tupi.setup.lexer.Token;
import com.tupi.setup.parser.Parser;

import java.io.File;
import java.util.List;

public class InterpretadorTupi {

    public static android.app.Activity activityAtual = null;

    public static String executar(String codigo, InterfaceUsuario ui, File pastaModulos) {
        interpretador interp = new interpretador();
        if (ui != null) {
            interp.setInterfaceUsuario(ui);
            if (ui instanceof InterfaceSaida) {
                interp.setInterfaceSaida((InterfaceSaida) ui);
            }
        }
        if (pastaModulos != null) interp.setPastaModulos(pastaModulos);
        if (activityAtual != null) {
            com.tupi.setup.motor.TelaMotor.inicia(activityAtual, interp);
        }
        try {
            List<Token> tokens = new Lexer(codigo).analisar();
            List<Instr> ast = new Parser(tokens).programa();
            interp.executar(ast, interp.global());
            String saida = interp.obterSaida();
            return saida.isEmpty() ? "(sem saída)" : saida;
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg == null) msg = e.getClass().getSimpleName();
            return "❌ Erro: " + msg;
        }
    }
}
