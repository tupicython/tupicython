package com.tupi.setup.parser;

import com.tupi.setup.ast.Expr;
import com.tupi.setup.ast.Instr;
import com.tupi.setup.lexer.Token;
import com.tupi.setup.lexer.TipoToken;

import java.util.ArrayList;
import java.util.List;

import static com.tupi.setup.lexer.TipoToken.*;

public class Parser {

    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) { this.tokens = tokens; }

    public List<Instr> programa() {
        List<Instr> lista = new ArrayList<Instr>();
        pularLinhas();
        while (!noFim()) {
            lista.add(instrucao());
            pularLinhas();
        }
        return lista;
    }

    private Instr instrucao() {
		if (verifica(IMPORTE))   return importeInstr();
        if (verifica(VAR))       return varInstr();
        if (verifica(FUNCAO))    return funcaoInstr();
        if (verifica(CLASSE))    return classeInstr();
        if (verifica(RETORNA))   return retornaInstr();
        if (verifica(SE))        return seInstr();
        if (verifica(ENQUANTO))  return enquantoInstr();
        if (verifica(PARA))      return paraInstr();
        if (verifica(ESCREVA))   return escrevaInstr();
        if (verifica(TENTA))     return tentaInstr();
        if (verifica(LANCA))     return lancaInstr();
        if (verifica(QUEBRA))    { avancar(); return new Instr.Quebra(); }
        if (verifica(CONTINUA))  { avancar(); return new Instr.Continua(); }
        if (verifica(CHAVE_ESQ)) return blocoInstr();
        return new Instr.ExprInstr(expressao());
    }
	
	private Instr importeInstr() {
		consumir(IMPORTE);
		// Formato 1: importe "arquivo.tupi"
		// Formato 2: importe matematica
		// Formato 3: importe matematica como mat
		String caminho;
		if (verifica(TEXTO)) {
			caminho = avancar().literal.toString();
		} else {
			caminho = consumir(IDENTIFICADOR).lexema;
		}
		String alias = null;
		if (casou(COMO)) {
			alias = consumir(IDENTIFICADOR).lexema;
		}
		return new Instr.Importe(caminho, alias, null);
	}

    private Instr classeInstr() {
		consumir(CLASSE);
		String nome = consumir(IDENTIFICADOR).lexema;
		String nomePai = null;
		if (casou(HERDA)) {
			consumir(DE);                              // ← agora usa o DE do enum
			nomePai = consumir(IDENTIFICADOR).lexema;
		}
		List<Instr.MetodoClasse> metodos = new ArrayList<Instr.MetodoClasse>();
		pularLinhas();
		while (!verifica(FIM) && !noFim()) {
			pularLinhas();
			if (verifica(FIM) || noFim()) break;
			casou(METODO);
			consumir(FUNCAO);
			String nomeMetodo = consumir(IDENTIFICADOR).lexema;
			boolean ehCtor = nomeMetodo.equals("construtor");
			consumir(PAREN_ESQ);
			List<String> params = new ArrayList<String>();
			if (!verifica(PAREN_DIR)) {
				do {
					params.add(consumir(IDENTIFICADOR).lexema);
				} while (casou(VIRGULA));
			}
			consumir(PAREN_DIR);
			Instr.Bloco corpo = blocoInstr();
			metodos.add(new Instr.MetodoClasse(nomeMetodo, params, corpo, ehCtor));
		}
		consumir(FIM);
		return new Instr.Classe(nome, nomePai, metodos);
	}

    private Instr tentaInstr() {
        consumir(TENTA);
        pularLinhas();
        List<Instr> listaTenta = new ArrayList<Instr>();
        while (!verifica(PEGA) && !verifica(FINALMENTE) && !verifica(FIM) && !noFim()) {
            listaTenta.add(instrucao());
            pularLinhas();
        }
        Instr.Bloco corpoTenta = new Instr.Bloco(listaTenta);

        String nomeErro = null;
        Instr.Bloco corpoPega = null;
        Instr.Bloco corpoFinal = null;

        if (casou(PEGA)) {
            if (verifica(IDENTIFICADOR)) {
                nomeErro = avancar().lexema;
            }
            pularLinhas();
            List<Instr> listaPega = new ArrayList<Instr>();
            while (!verifica(FINALMENTE) && !verifica(FIM) && !noFim()) {
                listaPega.add(instrucao());
                pularLinhas();
            }
            corpoPega = new Instr.Bloco(listaPega);
        }

        if (casou(FINALMENTE)) {
            pularLinhas();
            List<Instr> listaFinal = new ArrayList<Instr>();
            while (!verifica(FIM) && !noFim()) {
                listaFinal.add(instrucao());
                pularLinhas();
            }
            corpoFinal = new Instr.Bloco(listaFinal);
        }

        consumir(FIM);
        return new Instr.Tenta(corpoTenta, nomeErro, corpoPega, corpoFinal);
    }

    private Instr lancaInstr() {
        consumir(LANCA);
        Expr valor = expressao();
        return new Instr.Lanca(valor);
    }

    private Instr varInstr() {
        consumir(VAR);
        String nome = consumir(IDENTIFICADOR).lexema;
        Expr valor = null;
        if (verifica(ATRIBUICAO)) {
            avancar();
            valor = expressao();
        }
        return new Instr.Var(nome, valor);
    }

    private Instr funcaoInstr() {
        consumir(FUNCAO);
        String nome = consumir(IDENTIFICADOR).lexema;
        consumir(PAREN_ESQ);
        List<String> params = new ArrayList<String>();
        if (!verifica(PAREN_DIR)) {
            do {
                params.add(consumir(IDENTIFICADOR).lexema);
            } while (casou(VIRGULA));
        }
        consumir(PAREN_DIR);
        Instr.Bloco corpo = blocoInstr();
        return new Instr.Funcao(nome, params, corpo);
    }

    private Instr retornaInstr() {
        consumir(RETORNA);
        Expr valor = (verifica(NOVA_LINHA) || verifica(FIM)) ? null : expressao();
        return new Instr.Retorna(valor);
    }

    private Instr seInstr() {
        consumir(SE);
        Expr cond = expressao();
        casou(ENTAO);
        Instr.Bloco entao = blocoInstr();
        Instr.Bloco senao = null;
        if (casou(SENAO)) {
            if (verifica(SE)) {
                List<Instr> lista = new ArrayList<Instr>();
                lista.add(seInstr());
                senao = new Instr.Bloco(lista);
            } else {
                senao = blocoInstr();
            }
        }
        return new Instr.Se(cond, entao, senao);
    }

    private Instr enquantoInstr() {
        consumir(ENQUANTO);
        Expr cond = expressao();
        casou(FACA);
        Instr.Bloco corpo = blocoInstr();
        return new Instr.Enquanto(cond, corpo);
    }

    private Instr paraInstr() {
        consumir(PARA);
        String var = consumir(IDENTIFICADOR).lexema;
        consumir(ATRIBUICAO);
        Expr inicio = expressao();
        consumir(ATE);
        Expr fim = expressao();
        casou(FACA);
        Instr.Bloco corpo = blocoInstr();
        return new Instr.Para(var, inicio, fim, corpo);
    }

    private Instr escrevaInstr() {
        consumir(ESCREVA);
        consumir(PAREN_ESQ);
        Expr valor = expressao();
        consumir(PAREN_DIR);
        return new Instr.Escreva(valor);
    }

    private Instr.Bloco blocoInstr() {
        pularLinhas();
        List<Instr> lista = new ArrayList<Instr>();
        if (casou(CHAVE_ESQ)) {
            pularLinhas();
            while (!verifica(CHAVE_DIR) && !noFim()) {
                lista.add(instrucao());
                pularLinhas();
            }
            consumir(CHAVE_DIR);
        } else {
            while (!verifica(FIM) && !verifica(SENAO)
                   && !verifica(PEGA) && !verifica(FINALMENTE) && !noFim()) {
                pularLinhas();
                if (verifica(FIM) || verifica(SENAO)
                    || verifica(PEGA) || verifica(FINALMENTE) || noFim()) break;
                lista.add(instrucao());
                pularLinhas();
            }
            if (verifica(FIM)) consumir(FIM);
        }
        return new Instr.Bloco(lista);
    }

    private Expr expressao() { return atribuicao(); }

    private Expr atribuicao() {
        Expr esq = ou();
        if (verifica(ATRIBUICAO)) {
            avancar();
            Expr dir = atribuicao();
            return new Expr.Binaria(esq, "=", dir);
        }
        return esq;
    }
    private Expr ou() {
        Expr e = eLogico();
        while (casou(OU)) e = new Expr.Binaria(e, "ou", eLogico());
        return e;
    }
    private Expr eLogico() {
        Expr e = igualdade();
        while (casou(E)) e = new Expr.Binaria(e, "e", igualdade());
        return e;
    }
    private Expr igualdade() {
        Expr e = comparacao();
        while (verifica(IGUAL_IGUAL) || verifica(DIFERENTE)) {
            String op = avancar().lexema;
            e = new Expr.Binaria(e, op, comparacao());
        }
        return e;
    }
    private Expr comparacao() {
        Expr e = concat();
        while (verifica(MENOR) || verifica(MENOR_IGUAL)
               || verifica(MAIOR) || verifica(MAIOR_IGUAL)) {
            String op = avancar().lexema;
            e = new Expr.Binaria(e, op, concat());
        }
        return e;
    }
    private Expr concat() {
        Expr e = soma();
        while (casou(CONCAT)) e = new Expr.Binaria(e, "..", soma());
        return e;
    }
    private Expr soma() {
        Expr e = termo();
        while (verifica(MAIS) || verifica(MENOS)) {
            String op = avancar().lexema;
            e = new Expr.Binaria(e, op, termo());
        }
        return e;
    }
    private Expr termo() {
        Expr e = potencia();
        while (verifica(MULT) || verifica(DIV) || verifica(MOD)) {
            String op = avancar().lexema;
            e = new Expr.Binaria(e, op, potencia());
        }
        return e;
    }
    private Expr potencia() {
        Expr e = unaria();
        if (casou(POTENCIA)) e = new Expr.Binaria(e, "**", unaria());
        return e;
    }
    private Expr unaria() {
        if (verifica(NAO) || verifica(MENOS)) {
            String op = avancar().lexema;
            return new Expr.Unaria(op, unaria());
        }
        return chamada();
    }
    private Expr chamada() {
        Expr e = primario();
        while (true) {
            if (casou(PAREN_ESQ)) {
                List<Expr> args = new ArrayList<Expr>();
                if (!verifica(PAREN_DIR)) {
                    do { args.add(expressao()); } while (casou(VIRGULA));
                }
                consumir(PAREN_DIR);
                e = new Expr.Chamada(e, args);
            } else if (casou(PONTO)) {
                String nome = consumir(IDENTIFICADOR).lexema;
                e = new Expr.Acesso(e, nome);
            } else if (casou(COLCH_ESQ)) {
                Expr chave = expressao();
                consumir(COLCH_DIR);
                e = new Expr.Index(e, chave);
            } else break;
        }
        return e;
    }
    private Expr primario() {
		if (casou(FUNCAO)) {
			consumir(PAREN_ESQ);
			List<String> params = new ArrayList<String>();
			if (!verifica(PAREN_DIR)) {
				do { params.add(consumir(IDENTIFICADOR).lexema); } while (casou(VIRGULA));
			}
			consumir(PAREN_DIR);
			Instr.Bloco corpo = blocoInstr();
			return new Expr.FuncaoAnonima(params, corpo);
		}
        if (casou(NUMERO))     return new Expr.Literal(tokens.get(pos - 1).literal);
        if (casou(TEXTO))      return new Expr.Literal(tokens.get(pos - 1).literal);
        if (casou(VERDADEIRO)) return new Expr.Literal(Boolean.TRUE);
        if (casou(FALSO))      return new Expr.Literal(Boolean.FALSE);
        if (casou(NULO))       return new Expr.Literal(null);
        if (casou(EU)) {
            if (casou(PONTO)) {
                String nome = consumir(IDENTIFICADOR).lexema;
                return new Expr.AcessoEu(nome);
            }
            return new Expr.Eu();
        }
        if (casou(SUPER)) {
            return new Expr.Super();
        }
        if (casou(IDENTIFICADOR)) return new Expr.Variavel(tokens.get(pos - 1).lexema);
        if (casou(PAREN_ESQ)) {
            Expr e = expressao();
            consumir(PAREN_DIR);
            return e;
        }
        if (casou(COLCH_ESQ)) {
            List<Expr> itens = new ArrayList<Expr>();
            if (!verifica(COLCH_DIR)) {
                do { itens.add(expressao()); } while (casou(VIRGULA));
            }
            consumir(COLCH_DIR);
            return new Expr.Lista(itens);
        }
        if (casou(CHAVE_ESQ)) {
            List<Expr> chaves = new ArrayList<Expr>();
            List<Expr> valores = new ArrayList<Expr>();
            if (!verifica(CHAVE_DIR)) {
                do {
                    chaves.add(expressao());
                    consumir(DOIS_PONTOS);
                    valores.add(expressao());
                } while (casou(VIRGULA));
            }
            consumir(CHAVE_DIR);
            return new Expr.Mapa(chaves, valores);
        }
        Token t = atual();
        throw new ErrorSintaxe("Expressão inesperada '" + t.lexema + "' (linha " + t.linha + ")");
    }

    private Token atual() { return tokens.get(pos); }
    private boolean noFim() { return atual().tipo == EOF; }
    private boolean verifica(TipoToken... tipos) {
        for (TipoToken t : tipos) if (atual().tipo == t) return true;
        return false;
    }
    private Token avancar() { return tokens.get(pos++); }
    private boolean casou(TipoToken tipo) {
        if (verifica(tipo)) { avancar(); return true; }
        return false;
    }
    private Token consumir(TipoToken tipo) {
        if (verifica(tipo)) return avancar();
        Token t = atual();
        throw new ErrorSintaxe("Esperava " + tipo + " mas veio '" + t.lexema + "' (linha " + t.linha + ")");
    }
    private void pularLinhas() {
        while (verifica(NOVA_LINHA)) avancar();
    }
}
