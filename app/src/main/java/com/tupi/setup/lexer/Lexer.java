package com.tupi.setup.lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.tupi.setup.lexer.TipoToken.*;

public class Lexer {

    private final String fonte;
    private final List<Token> tokens = new ArrayList<Token>();
    private int inicio = 0;
    private int atual = 0;
    private int linha = 1;

    private static final Map<String, TipoToken> PALAVRAS =
	new HashMap<String, TipoToken>();
    static {
        PALAVRAS.put("var", VAR);
        PALAVRAS.put("funcao", FUNCAO);
        PALAVRAS.put("retorna", RETORNA);
        PALAVRAS.put("se", SE);
        PALAVRAS.put("senao", SENAO);
        PALAVRAS.put("entao", ENTAO);
        PALAVRAS.put("enquanto", ENQUANTO);
        PALAVRAS.put("para", PARA);
        PALAVRAS.put("ate", ATE);
        PALAVRAS.put("faca", FACA);
        PALAVRAS.put("fim", FIM);
        PALAVRAS.put("e", E);
        PALAVRAS.put("ou", OU);
        PALAVRAS.put("nao", NAO);
        PALAVRAS.put("verdadeiro", VERDADEIRO);
        PALAVRAS.put("falso", FALSO);
        PALAVRAS.put("nulo", NULO);
        PALAVRAS.put("escreva", ESCREVA);
        PALAVRAS.put("quebra", QUEBRA);
        PALAVRAS.put("continua", CONTINUA);
        PALAVRAS.put("classe", CLASSE);
        PALAVRAS.put("eu", EU);
        PALAVRAS.put("super", SUPER);
        PALAVRAS.put("herda", HERDA);
        PALAVRAS.put("metodo", METODO);
        PALAVRAS.put("tenta", TENTA);
        PALAVRAS.put("pega", PEGA);
        PALAVRAS.put("finalmente", FINALMENTE);
        PALAVRAS.put("lanca", LANCA);
        PALAVRAS.put("importe", IMPORTE);
        PALAVRAS.put("como", COMO);
		PALAVRAS.put("de", DE);
    }

    public Lexer(String fonte) { this.fonte = fonte; }

    public List<Token> analisar() {
        while (!acabou()) {
            inicio = atual;
            char c = avancar();
            switch (c) {
                case '(': add(PAREN_ESQ); break;
                case ')': add(PAREN_DIR); break;
                case '[': add(COLCH_ESQ); break;
                case ']': add(COLCH_DIR); break;
                case '{': add(CHAVE_ESQ); break;
                case '}': add(CHAVE_DIR); break;
                case ',': add(VIRGULA); break;
                case ':': add(DOIS_PONTOS); break;
                case '+': add(MAIS); break;
                case '-': add(MENOS); break;
                case '*':
                    if (espiar() == '*') { avancar(); add(POTENCIA); }
                    else add(MULT);
                    break;
                case '/':
                    if (espiar() == '/') {
                        while (espiar() != '\n' && !acabou()) avancar();
                    } else add(DIV);
                    break;
                case '%': add(MOD); break;
                case '#':
                    while (espiar() != '\n' && !acabou()) avancar();
                    break;
                case '=':
                    if (espiar() == '=') { avancar(); add(IGUAL_IGUAL); }
                    else add(ATRIBUICAO);
                    break;
                case '!':
                    if (espiar() == '=') { avancar(); add(DIFERENTE); }
                    else throw erro("Esperava '!='");
                    break;
                case '~':
                    if (espiar() == '=') { avancar(); add(DIFERENTE); }
                    else throw erro("Esperava '~='");
                    break;
                case '<':
                    if (espiar() == '=') { avancar(); add(MENOR_IGUAL); }
                    else add(MENOR);
                    break;
                case '>':
                    if (espiar() == '=') { avancar(); add(MAIOR_IGUAL); }
                    else add(MAIOR);
                    break;
                case '.':
                    if (espiar() == '.') { avancar(); add(CONCAT); }
                    else add(PONTO);
                    break;
                case '"': texto(); break;
                case '\n': linha++; add(NOVA_LINHA); break;
                case ' ': case '\r': case '\t': break;
                default:
                    if (Character.isDigit(c)) numero();
                    else if (Character.isLetter(c) || c == '_') identificador();
                    else throw erro("Caractere inesperado: '" + c + "'");
            }
        }
        tokens.add(new Token(EOF, "", null, linha));
        return tokens;
    }

    private void texto() {
        StringBuilder sb = new StringBuilder();
        while (espiar() != '"' && !acabou()) {
            if (espiar() == '\n') linha++;
            if (espiar() == '\\') {
                avancar();
                char esc = avancar();
                switch (esc) {
                    case 'n': sb.append('\n'); break;
                    case 't': sb.append('\t'); break;
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    default: sb.append(esc);
                }
            } else {
                sb.append(avancar());
            }
        }
        if (acabou()) throw erro("Texto não fechado");
        avancar();
        addLiteral(TEXTO, sb.toString());
    }

    private void numero() {
        while (Character.isDigit(espiar())) avancar();
        if (espiar() == '.' && Character.isDigit(espiarProx())) {
            avancar();
            while (Character.isDigit(espiar())) avancar();
        }
        String num = fonte.substring(inicio, atual);
        addLiteral(NUMERO, Double.valueOf(Double.parseDouble(num)));
    }

    private void identificador() {
        while (Character.isLetterOrDigit(espiar()) || espiar() == '_') avancar();
        String palavra = fonte.substring(inicio, atual);
        TipoToken tipo = PALAVRAS.get(palavra);
        if (tipo != null) add(tipo);
        else addLiteral(IDENTIFICADOR, palavra);
    }

    private boolean acabou() { return atual >= fonte.length(); }
    private char avancar() { return fonte.charAt(atual++); }
    private char espiar() { return acabou() ? '\0' : fonte.charAt(atual); }
    private char espiarProx() {
        return (atual + 1 >= fonte.length()) ? '\0' : fonte.charAt(atual + 1);
    }

    private void add(TipoToken tipo) {
        tokens.add(new Token(tipo, fonte.substring(inicio, atual), null, linha));
    }
    private void addLiteral(TipoToken tipo, Object val) {
        tokens.add(new Token(tipo, fonte.substring(inicio, atual), val, linha));
    }
    private RuntimeException erro(String msg) {
        return new RuntimeException("[Linha " + linha + "] " + msg);
    }
}
