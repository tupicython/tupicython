package com.tupi.setup.ast;

import java.util.List;
import com.tupi.setup.ast.Instr;

public abstract class Expr {

    public static class Literal extends Expr {
        public final Object valor;
        public Literal(Object v) { this.valor = v; }
    }

    public static class Variavel extends Expr {
        public final String nome;
        public Variavel(String n) { this.nome = n; }
    }

    public static class Binaria extends Expr {
        public final Expr esq;
        public final String op;
        public final Expr dir;
        public Binaria(Expr e, String o, Expr d) { esq = e; op = o; dir = d; }
    }

    public static class Unaria extends Expr {
        public final String op;
        public final Expr expr;
        public Unaria(String o, Expr e) { op = o; expr = e; }
    }

    public static class Chamada extends Expr {
        public final Expr alvo;
        public final List<Expr> args;
        public Chamada(Expr a, List<Expr> ar) { alvo = a; args = ar; }
    }

    public static class Acesso extends Expr {
        public final Expr obj;
        public final String nome;
        public Acesso(Expr o, String n) { obj = o; nome = n; }
    }

    public static class Index extends Expr {
        public final Expr obj;
        public final Expr chave;
        public Index(Expr o, Expr c) { obj = o; chave = c; }
    }

    public static class Lista extends Expr {
        public final List<Expr> itens;
        public Lista(List<Expr> i) { itens = i; }
    }

    public static class Mapa extends Expr {
        public final List<Expr> chaves;
        public final List<Expr> valores;
        public Mapa(List<Expr> c, List<Expr> v) { chaves = c; valores = v; }
    }

    public static class Eu extends Expr { }

    public static class Super extends Expr { }

    public static class AcessoEu extends Expr {
        public final String nome;
        public AcessoEu(String n) { nome = n; }
    }
	
	public static class FuncaoAnonima extends Expr {
		public final List<String> params;
		public final Instr.Bloco corpo;
		public FuncaoAnonima(List<String> p, Instr.Bloco c) {
			params = p; corpo = c;
		}
	}
}
