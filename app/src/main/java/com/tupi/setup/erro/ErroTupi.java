package com.tupi.setup.erro;

public class ErroTupi extends RuntimeException {

    public final String codigo;   // ex: "TUPI001"
    public final int linha;
    public final int coluna;
    public final String arquivo;
    public final String dica;     // sugestão pra corrigir

    public ErroTupi(String codigo, String mensagem, int linha, int coluna,
                    String arquivo, String dica) {
        super(mensagem);
        this.codigo = codigo;
        this.linha = linha;
        this.coluna = coluna;
        this.arquivo = arquivo == null ? "<programa>" : arquivo;
        this.dica = dica;
    }

    public ErroTupi(String codigo, String mensagem, int linha, int coluna) {
        this(codigo, mensagem, linha, coluna, null, null);
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(codigo).append("] ");
        sb.append(super.getMessage());
        if (linha > 0) {
            sb.append("\n  → ").append(arquivo)
				.append(":").append(linha);
            if (coluna > 0) sb.append(":").append(coluna);
        }
        if (dica != null && !dica.isEmpty()) {
            sb.append("\n  💡 Dica: ").append(dica);
        }
        return sb.toString();
    }
}
