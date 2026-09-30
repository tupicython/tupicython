package com.tupi.setup;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxHighLighter {

    // ============ CORES ============
    public static final int COR_FUNDO = Color.parseColor("#1E1E1E");

    public static final int COR_PALAVRA_CHAVE = Color.parseColor("#C586C0");  // rosa (var, funcao, se...)
    public static final int COR_LITERAL      = Color.parseColor("#4EC9B0");  // verde (verdadeiro, falso, nulo)
    public static final int COR_TEXTO        = Color.parseColor("#CE9178");  // laranja-claro ("...")
    public static final int COR_NUMERO       = Color.parseColor("#B5CEA8");  // verde-claro (42, 3.14)
    public static final int COR_COMENTARIO   = Color.parseColor("#6A9955");  // verde-escuro (# ...)
    public static final int COR_OPERADOR     = Color.parseColor("#D4D4D4");  // branco
    public static final int COR_IDENTIFICADOR = Color.parseColor("#9CDCFE"); // azul-claro
    public static final int COR_NORMAL       = Color.parseColor("#D4D4D4");  // branco

    // ============ PADRÕES ============
    private static final Pattern P_COMENTARIO = Pattern.compile("//.*|#.*");
    private static final Pattern P_TEXTO      = Pattern.compile("\"[^\"]*\"");
    private static final Pattern P_NUMERO     = Pattern.compile("\\b\\d+(\\.\\d+)?\\b");
    private static final Pattern P_PALAVRA    = Pattern.compile(
        "\\b(var|funcao|retorna|se|senao|entao|enquanto|para|ate|faca|fim|" +
        "e|ou|nao|classe|eu|super|herda|de|metodo|" +
        "tenta|pega|finalmente|lanca|importe|como|quebra|continua)\\b"
    );
    private static final Pattern P_LITERAL    = Pattern.compile("\\b(verdadeiro|falso|nulo)\\b");
    private static final Pattern P_NATIVA     = Pattern.compile(
        "\\b(escreva|leia|tamanho|texto|numero|tipo)\\b"
    );

    public static void aplicar(Editable texto) {
        // Remove formatações antigas
        ForegroundColorSpan[] spans = texto.getSpans(0, texto.length(), ForegroundColorSpan.class);
        for (ForegroundColorSpan s : spans) {
            texto.removeSpan(s);
        }

        // Cor normal em tudo
        texto.setSpan(new ForegroundColorSpan(COR_NORMAL),
                      0, texto.length(),
                      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Aplica as regras
        aplicarRegex(texto, P_COMENTARIO, COR_COMENTARIO);
        aplicarRegex(texto, P_TEXTO,      COR_TEXTO);
        aplicarRegex(texto, P_NUMERO,     COR_NUMERO);
        aplicarRegex(texto, P_LITERAL,    COR_LITERAL);
        aplicarRegex(texto, P_PALAVRA,    COR_PALAVRA_CHAVE);
        aplicarRegex(texto, P_NATIVA,     COR_IDENTIFICADOR);
    }

    private static void aplicarRegex(Editable texto, Pattern padrao, int cor) {
        Matcher m = padrao.matcher(texto);
        while (m.find()) {
            texto.setSpan(new ForegroundColorSpan(cor),
                          m.start(), m.end(),
                          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}
