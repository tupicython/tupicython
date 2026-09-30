package com.tupi.setup.ast;

import java.util.List;

public abstract class Instr {

    public static class ExprInstr extends Instr {
        public final Expr expr;
        public ExprInstr(Expr e) { expr = e; }
    }

    public static class Var extends Instr {
        public final String nome;
        public final Expr valor;
        public Var(String n, Expr v) { nome = n; valor = v; }
    }

    public static class Bloco extends Instr {
        public final List<Instr> instrucoes;
        public Bloco(List<Instr> i) { instrucoes = i; }
    }

    public static class Se extends Instr {
        public final Expr cond;
        public final Bloco entao;
        public final Bloco senao;
        public Se(Expr c, Bloco e, Bloco s) { cond = c; entao = e; senao = s; }
    }

    public static class Enquanto extends Instr {
        public final Expr cond;
        public final Bloco corpo;
        public Enquanto(Expr c, Bloco co) { cond = c; corpo = co; }
    }

    public static class Para extends Instr {
        public final String var;
        public final Expr inicio;
        public final Expr fim;
        public final Bloco corpo;
        public Para(String v, Expr i, Expr f, Bloco c) {
            var = v; inicio = i; fim = f; corpo = c;
        }
    }

    public static class Funcao extends Instr {
        public final String nome;
        public final List<String> params;
        public final Bloco corpo;
        public Funcao(String n, List<String> p, Bloco c) {
            nome = n; params = p; corpo = c;
        }
    }

    public static class Retorna extends Instr {
        public final Expr valor;
        public Retorna(Expr v) { valor = v; }
    }

    public static class Escreva extends Instr {
        public final Expr valor;
        public Escreva(Expr v) { valor = v; }
    }

    public static class Quebra extends Instr { }

    public static class Continua extends Instr { }

    public static class Classe extends Instr {
        public final String nome;
        public final String nomePai;
        public final List<MetodoClasse> metodos;
        public Classe(String n, String pai, List<MetodoClasse> m) {
            nome = n; nomePai = pai; metodos = m;
        }
    }

    public static class MetodoClasse {
        public final String nome;
        public final List<String> params;
        public final Bloco corpo;
        public final boolean ehConstrutor;
        public MetodoClasse(String n, List<String> p, Bloco c, boolean ctor) {
            nome = n; params = p; corpo = c; ehConstrutor = ctor;
        }
    }

    public static class Tenta extends Instr {
        public final Bloco corpoTenta;
        public final String nomeErro;
        public final Bloco corpoPega;
        public final Bloco corpoFinal;
        public Tenta(Bloco t, String n, Bloco p, Bloco f) {
            corpoTenta = t; nomeErro = n; corpoPega = p; corpoFinal = f;
        }
    }

    public static class Lanca extends Instr {
        public final Expr valor;
        public Lanca(Expr v) { valor = v; }
    }
	public static class Importe extends Instr {
		public final String caminho;      // pode ser "matematica" ou "utils.tupi"
		public final String alias;        // null se não tem
		public final List<String> nomes;  // null se é importe normal; lista se é "de X importe a, b"
		public Importe(String c, String a, List<String> n) {
			caminho = c; alias = a; nomes = n;
		}
	}
}
