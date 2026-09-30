package com.tupi.setup.interpretador;

import com.tupi.setup.ast.Instr;
import com.tupi.setup.lexer.Lexer;
import com.tupi.setup.lexer.Token;
import com.tupi.setup.parser.Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class CarregadorModulos {

    private final File pastaBase;
    private final interpretador interpretador;
    private final java.util.Set<String> jaCarregados =
	new java.util.HashSet<String>();

    public CarregadorModulos(File pastaBase, interpretador interpretador) {
        this.pastaBase = pastaBase;
        this.interpretador = interpretador;
    }

    public ModoloNativo carregar(String nome) {
        if (jaCarregados.contains(nome)) {
            throw new RuntimeException("Módulo '" + nome + "' já foi importado");
        }
        jaCarregados.add(nome);

        File arquivo = new File(pastaBase, nome);
        if (!arquivo.exists()) {
            // tenta com .tupi
            arquivo = new File(pastaBase, nome + ".tupi");
        }
        if (!arquivo.exists()) {
            throw new RuntimeException("Módulo '" + nome + "' não encontrado em " +
                                       pastaBase.getAbsolutePath());
        }

        String codigo;
        try {
            codigo = lerArquivo(arquivo);
        } catch (Exception e) {
            throw new RuntimeException("Erro lendo módulo '" + nome + "': " + e.getMessage());
        }

        List<Token> tokens = new Lexer(codigo).analisar();
        List<Instr> ast = new Parser(tokens).programa();

        ModoloNativo modulo = new ModoloNativo(removerExtensao(nome));
        Ambiente envModulo = new Ambiente(interpretador.global());
        interpretador.executar(ast, envModulo);

        // Tudo que foi definido no módulo vira membro
        modulo.membros.putAll(envModulo.variaveis);

        return modulo;
    }

    private String lerArquivo(File f) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new FileReader(f));
        String linha;
        while ((linha = r.readLine()) != null) {
            sb.append(linha).append('\n');
        }
        r.close();
        return sb.toString();
    }

    private String removerExtensao(String nome) {
        if (nome.endsWith(".tupi")) {
            return nome.substring(0, nome.length() - 5);
        }
        return nome;
    }
}
