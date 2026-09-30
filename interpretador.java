package com.tupi.setup.interpretador;

import com.tupi.setup.ast.Expr;
import com.tupi.setup.ast.Instr;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class interpretador {

    private final Ambiente global = new Ambiente();
    private final StringBuilder saida = new StringBuilder();
    private InterfaceUsuario ui;
    private CarregadorModulos carregador;
    private File pastaModulos;
    private InterfaceSaida saidaUI;

    public interpretador() {
        registrarNativas();
        registrarModulosNativos();
    }

    public String obterSaida() { return saida.toString(); }
    public Ambiente global() { return global; }

    public void setInterfaceUsuario(InterfaceUsuario ui) {
        this.ui = ui;
    }
    public void setInterfaceSaida(InterfaceSaida ui) {
        this.saidaUI = ui;
    }

    public void setPastaModulos(File pasta) {
		this.pastaModulos = pasta;
		if (pasta != null) {
			this.carregador = new CarregadorModulos(pasta, this);

			// NOVO: registra o tupim automaticamente
			File pastaRepositorio = new File(pasta.getParentFile(), "repositorio");
			if (!pastaRepositorio.exists()) pastaRepositorio.mkdirs();
			RegistroModulos.registrar(new ModuloTupim(pasta, pastaRepositorio));
		}
	}
    private void registrarModulosNativos() {
        RegistroModulos.registrar(new ModuloMatematica());
        RegistroModulos.registrar(new MeduloTexto());
        RegistroModulos.registrar(new ModuloLista());
        RegistroModulos.registrar(new com.tupi.setup.numtu.MeduloNumtu());
        RegistroModulos.registrar(new ModuloServidor());
    }

    public void executar(List<Instr> programa, Ambiente env) {
        for (int i = 0; i < programa.size(); i++) {
            try {
                executarInstr(programa.get(i), env);
            } catch (RetornoException | QuebraException | ContinuaException e) {
                throw e;
            } catch (RuntimeException e) {
                String msg = e.getMessage();
                if (msg == null || msg.isEmpty()) {
                    msg = e.getClass().getSimpleName();
                }
                String linhaErro = "❌ Erro: " + msg;
                saida.append(linhaErro).append("\n");
                if (saidaUI != null) {
                    saidaUI.escrever(linhaErro);
                }
                return;
            }
        }
    }

    // ========== MÉTODO NOVO — chamar função de fora (servidor HTTP usa) ==========
    public Object chamarFuncao(Object funcao, List<Object> args) {
        if (funcao instanceof FuncaoTupi) {
            FuncaoTupi f = (FuncaoTupi) funcao;
            Ambiente local = new Ambiente(f.closure);
            for (int k = 0; k < f.params.size(); k++) {
                local.definir(f.params.get(k), k < args.size() ? args.get(k) : null);
            }
            try {
                executar(f.corpo.instrucoes, new Ambiente(local));
            } catch (RetornoException r) {
                return r.valor;
            }
            return null;
        }
        if (funcao instanceof Nativa) {
            return ((Nativa) funcao).chamar(args);
        }
        throw new RuntimeException("Não é função: " + funcao);
    }

    private void executarInstr(Instr i, Ambiente env) {
        if (i instanceof Instr.ExprInstr) {
            avaliar(((Instr.ExprInstr) i).expr, env);
        } else if (i instanceof Instr.Var) {
            Instr.Var v = (Instr.Var) i;
            Object val = (v.valor == null) ? null : avaliar(v.valor, env);
            env.definir(v.nome, val);
        } else if (i instanceof Instr.Bloco) {
            executar(((Instr.Bloco) i).instrucoes, new Ambiente(env));
        } else if (i instanceof Instr.Escreva) {
            Object val = avaliar(((Instr.Escreva) i).valor, env);
            String texto = formatar(val);
            saida.append(texto).append("\n");
            if (saidaUI != null) {
                saidaUI.escrever(texto);
            }
        } else if (i instanceof Instr.Se) {
            Instr.Se s = (Instr.Se) i;
            if (verdadeiro(avaliar(s.cond, env))) {
                executarBloco(s.entao, env);
            } else if (s.senao != null) {
                executarBloco(s.senao, env);
            }
        } else if (i instanceof Instr.Enquanto) {
            Instr.Enquanto w = (Instr.Enquanto) i;
            while (verdadeiro(avaliar(w.cond, env))) {
                try { executarBloco(w.corpo, env); }
                catch (QuebraException q) { break; }
                catch (ContinuaException c) { }
            }
        } else if (i instanceof Instr.Para) {
            Instr.Para p = (Instr.Para) i;
            double ini = numero(avaliar(p.inicio, env));
            double fim = numero(avaliar(p.fim, env));
            for (double k = ini; k <= fim; k++) {
                env.definir(p.var, Double.valueOf(k));
                try { executarBloco(p.corpo, env); }
                catch (QuebraException q) { break; }
                catch (ContinuaException c) { }
            }
        } else if (i instanceof Instr.Classe) {
            Instr.Classe c = (Instr.Classe) i;
            Classe pai = null;
            if (c.nomePai != null) {
                Object p = env.obter(c.nomePai);
                if (!(p instanceof Classe))
                    throw new RuntimeException("'" + c.nomePai + "' não é uma classe");
                pai = (Classe) p;
            }
            Classe nova = new Classe(c.nome, pai, c.metodos, env);
            env.definir(c.nome, nova);
        } else if (i instanceof Instr.Tenta) {
            Instr.Tenta t = (Instr.Tenta) i;
            try {
                executar(t.corpoTenta.instrucoes, new Ambiente(env));
            } catch (RetornoException e) { throw e;
            } catch (QuebraException e) { throw e;
            } catch (ContinuaException e) { throw e;
            } catch (RuntimeException e) {
                if (t.corpoPega != null) {
                    Ambiente local = new Ambiente(env);
                    if (t.nomeErro != null) {
                        String msg = e.getMessage();
                        if (msg == null) msg = e.getClass().getSimpleName();
                        local.definir(t.nomeErro, msg);
                    }
                    executar(t.corpoPega.instrucoes, local);
                } else {
                    throw e;
                }
            } finally {
                if (t.corpoFinal != null) {
                    executar(t.corpoFinal.instrucoes, new Ambiente(env));
                }
            }
        } else if (i instanceof Instr.Lanca) {
            Instr.Lanca l = (Instr.Lanca) i;
            Object val = avaliar(l.valor, env);
            throw new RuntimeException(formatar(val));
        } else if (i instanceof Instr.Importe) {
            Instr.Importe imp = (Instr.Importe) i;
            carregarModulo(imp, env);
        } else if (i instanceof Instr.Funcao) {
            Instr.Funcao f = (Instr.Funcao) i;
            env.definir(f.nome, new FuncaoTupi(f.nome, f.params, f.corpo, env));
        } else if (i instanceof Instr.Retorna) {
            Instr.Retorna r = (Instr.Retorna) i;
            Object val = (r.valor == null) ? null : avaliar(r.valor, env);
            throw new RetornoException(val);
        } else if (i instanceof Instr.Quebra) {
            throw new QuebraException();
        } else if (i instanceof Instr.Continua) {
            throw new ContinuaException();
        }
    }

    private void carregarModulo(Instr.Importe imp, Ambiente env) {
        if (RegistroModulos.existe(imp.caminho)) {
            ModoloNativo m = RegistroModulos.carregar(imp.caminho);
            String nomeFinal = imp.alias != null ? imp.alias : m.nome;
            env.definir(nomeFinal, m);
            return;
        }
        if (carregador != null) {
            ModoloNativo m = carregador.carregar(imp.caminho);
            String nomeFinal = imp.alias != null ? imp.alias : m.nome;
            env.definir(nomeFinal, m);
            return;
        }
        throw new RuntimeException("Módulo '" + imp.caminho + "' não encontrado");
    }

    private void executarBloco(Instr.Bloco b, Ambiente env) {
        executar(b.instrucoes, new Ambiente(env));
    }

    public Object avaliar(Expr e, Ambiente env) {
        if (e instanceof Expr.Literal)   return ((Expr.Literal) e).valor;
        if (e instanceof Expr.Variavel)  return env.obter(((Expr.Variavel) e).nome);

        if (e instanceof Expr.Unaria) {
            Expr.Unaria u = (Expr.Unaria) e;
            Object v = avaliar(u.expr, env);
            if (u.op.equals("nao")) return Boolean.valueOf(!verdadeiro(v));
            if (u.op.equals("-"))   return Double.valueOf(-numero(v));
            throw new RuntimeException("Operador unário: " + u.op);
        }

        if (e instanceof Expr.Binaria)  return binaria((Expr.Binaria) e, env);
        if (e instanceof Expr.Chamada)  return chamar((Expr.Chamada) e, env);

        if (e instanceof Expr.Acesso) {
            Expr.Acesso a = (Expr.Acesso) e;
            if (a.obj instanceof Expr.Super) {
                Object eu = obterEu(env);
                if (!(eu instanceof Instancia))
                    throw new RuntimeException("'super' usado fora de método");
                Instancia inst = (Instancia) eu;
                if (inst.classe.pai == null)
                    throw new RuntimeException("Classe '" + inst.classe.nome + "' não herda");
                Instr.MetodoClasse m = inst.classe.pai.buscarMetodo(a.nome);
                if (m == null)
                    throw new RuntimeException("Classe pai não tem '" + a.nome + "'");
                return new MetadoLigado(inst, m);
            }
            return acessar(avaliar(a.obj, env), a.nome);
        }
        if (e instanceof Expr.Index) {
            Expr.Index ix = (Expr.Index) e;
            return indexar(avaliar(ix.obj, env), avaliar(ix.chave, env));
        }
        if (e instanceof Expr.Lista) {
            List<Object> lista = new ArrayList<Object>();
            Expr.Lista l = (Expr.Lista) e;
            for (int i = 0; i < l.itens.size(); i++)
                lista.add(avaliar(l.itens.get(i), env));
            return lista;
        }
        if (e instanceof Expr.Mapa) {
            Map<Object, Object> mapa = new HashMap<Object, Object>();
            Expr.Mapa m = (Expr.Mapa) e;
            for (int i = 0; i < m.chaves.size(); i++) {
                mapa.put(avaliar(m.chaves.get(i), env),
                         avaliar(m.valores.get(i), env));
            }
            return mapa;
        }
        if (e instanceof Expr.Eu) return obterEu(env);
        if (e instanceof Expr.AcessoEu) {
            Object eu = obterEu(env);
            if (eu instanceof Instancia) {
                Instancia inst = (Instancia) eu;
                String nome = ((Expr.AcessoEu) e).nome;
                if (inst.campos.containsKey(nome)) return inst.campos.get(nome);
                Instr.MetodoClasse m = inst.classe.buscarMetodo(nome);
                if (m != null) return new MetadoLigado(inst, m);
                throw new RuntimeException("Classe '" + inst.classe.nome +
                                           "' não tem atributo/método '" + nome + "'");
            }
            throw new RuntimeException("'eu' usado fora de método");
        }
        if (e instanceof Expr.Super) {
            Object eu = obterEu(env);
            if (eu instanceof Instancia) return new SuperProxy((Instancia) eu);
            throw new RuntimeException("'super' usado fora de método");
        }
        throw new RuntimeException("Expressão desconhecida");
    }

    private Object obterEu(Ambiente env) {
        if (env.variaveis.containsKey("eu")) return env.variaveis.get("eu");
        if (env.pai != null) return obterEu(env.pai);
        throw new RuntimeException("'eu' usado fora de método");
    }

    private Object binaria(Expr.Binaria b, Ambiente env) {
        if (b.op.equals("=")) {
            Object dir = avaliar(b.dir, env);
            atribuir(b.esq, dir, env);
            return dir;
        }
        if (b.op.equals("e")) {
            if (!verdadeiro(avaliar(b.esq, env))) return Boolean.FALSE;
            return Boolean.valueOf(verdadeiro(avaliar(b.dir, env)));
        }
        if (b.op.equals("ou")) {
            if (verdadeiro(avaliar(b.esq, env))) return Boolean.TRUE;
            return Boolean.valueOf(verdadeiro(avaliar(b.dir, env)));
        }

        Object esq = avaliar(b.esq, env);
        Object dir = avaliar(b.dir, env);

        if (b.op.equals("+"))  return soma(esq, dir);
        if (b.op.equals("-"))  return Double.valueOf(numero(esq) - numero(dir));
        if (b.op.equals("*"))  return Double.valueOf(numero(esq) * numero(dir));
        if (b.op.equals("/")) {
            double d = numero(dir);
            if (d == 0) throw new RuntimeException("Divisão por zero!");
            return Double.valueOf(numero(esq) / d);
        }
        if (b.op.equals("%")) {
            double d = numero(dir);
            if (d == 0) throw new RuntimeException("Módulo por zero!");
            return Double.valueOf(numero(esq) % d);
        }
        if (b.op.equals("**")) return Double.valueOf(Math.pow(numero(esq), numero(dir)));
        if (b.op.equals("..")) return formatar(esq) + formatar(dir);
        if (b.op.equals("==")) return Boolean.valueOf(iguais(esq, dir));
        if (b.op.equals("!=")) return Boolean.valueOf(!iguais(esq, dir));
        if (b.op.equals("<"))  return Boolean.valueOf(comparar(esq, dir) < 0);
        if (b.op.equals("<=")) return Boolean.valueOf(comparar(esq, dir) <= 0);
        if (b.op.equals(">"))  return Boolean.valueOf(comparar(esq, dir) > 0);
        if (b.op.equals(">=")) return Boolean.valueOf(comparar(esq, dir) >= 0);
        throw new RuntimeException("Operador: " + b.op);
    }

    private void atribuir(Expr alvo, Object valor, Ambiente env) {
        if (alvo instanceof Expr.Variavel) {
            env.atribuir(((Expr.Variavel) alvo).nome, valor);
            return;
        }
        if (alvo instanceof Expr.Index) {
            Expr.Index ix = (Expr.Index) alvo;
            Object obj = avaliar(ix.obj, env);
            Object chave = avaliar(ix.chave, env);
            if (obj instanceof List) {
                List<Object> lista = castLista(obj);
                int idx = (int) numero(chave);
                while (lista.size() <= idx) lista.add(null);
                lista.set(idx, valor);
                return;
            }
            if (obj instanceof Map) {
                Map<Object, Object> mapa = castMapa(obj);
                mapa.put(chave, valor);
                return;
            }
            throw new RuntimeException("Não é possível indexar este valor");
        }
        if (alvo instanceof Expr.Acesso) {
            Expr.Acesso a = (Expr.Acesso) alvo;
            Object obj = avaliar(a.obj, env);
            if (obj instanceof Instancia) {
                ((Instancia) obj).campos.put(a.nome, valor);
                return;
            }
            if (obj instanceof Map) {
                castMapa(obj).put(a.nome, valor);
                return;
            }
            throw new RuntimeException("Não é possível atribuir em " + obj);
        }
        if (alvo instanceof Expr.AcessoEu) {
            Expr.AcessoEu a = (Expr.AcessoEu) alvo;
            Object eu = obterEu(env);
            if (eu instanceof Instancia) {
                ((Instancia) eu).campos.put(a.nome, valor);
                return;
            }
            throw new RuntimeException("'eu' usado fora de método");
        }
        throw new RuntimeException("Alvo de atribuição inválido");
    }

    private Object chamar(Expr.Chamada c, Ambiente env) {
        if (c.alvo instanceof Expr.Acesso) {
            Expr.Acesso a = (Expr.Acesso) c.alvo;
            if (a.obj instanceof Expr.Super && a.nome.equals("construtor")) {
                Object eu = obterEu(env);
                if (eu instanceof Instancia) {
                    Instancia inst = (Instancia) eu;
                    Classe pai = inst.classe.pai;
                    if (pai == null || pai.construtor == null) return null;
                    List<Object> args = new ArrayList<Object>();
                    for (int i = 0; i < c.args.size(); i++)
                        args.add(avaliar(c.args.get(i), env));
                    Ambiente local = new Ambiente(pai.ambienteDefinicao);
                    local.definir("eu", inst);
                    for (int k = 0; k < pai.construtor.params.size(); k++) {
                        local.definir(pai.construtor.params.get(k),
                                      k < args.size() ? args.get(k) : null);
                    }
                    try {
                        executar(pai.construtor.corpo.instrucoes, new Ambiente(local));
                    } catch (RetornoException r) { }
                    return null;
                }
            }
        }

        Object alvo = avaliar(c.alvo, env);
        List<Object> args = new ArrayList<Object>();
        for (int i = 0; i < c.args.size(); i++)
            args.add(avaliar(c.args.get(i), env));

        if (alvo instanceof Classe) {
            Classe cls = (Classe) alvo;
            Instancia inst = new Instancia(cls);
            if (cls.construtor != null) {
                Ambiente local = new Ambiente(cls.ambienteDefinicao);
                local.definir("eu", inst);
                for (int k = 0; k < cls.construtor.params.size(); k++) {
                    local.definir(cls.construtor.params.get(k),
                                  k < args.size() ? args.get(k) : null);
                }
                try {
                    executar(cls.construtor.corpo.instrucoes, new Ambiente(local));
                } catch (RetornoException r) { }
            }
            return inst;
        }

        if (alvo instanceof MetadoLigado) {
            MetadoLigado ml = (MetadoLigado) alvo;
            Ambiente local = new Ambiente(ml.instancia.classe.ambienteDefinicao);
            local.definir("eu", ml.instancia);
            for (int k = 0; k < ml.metodo.params.size(); k++) {
                local.definir(ml.metodo.params.get(k),
                              k < args.size() ? args.get(k) : null);
            }
            try {
                executar(ml.metodo.corpo.instrucoes, new Ambiente(local));
            } catch (RetornoException r) {
                return r.valor;
            }
            return null;
        }

        if (alvo instanceof FuncaoTupi) {
            FuncaoTupi f = (FuncaoTupi) alvo;
            Ambiente local = new Ambiente(f.closure);
            for (int k = 0; k < f.params.size(); k++) {
                local.definir(f.params.get(k), k < args.size() ? args.get(k) : null);
            }
            try {
                executar(f.corpo.instrucoes, new Ambiente(local));
            } catch (RetornoException r) {
                return r.valor;
            }
            return null;
        }
        if (alvo instanceof Nativa) {
            return ((Nativa) alvo).chamar(args);
        }
        throw new RuntimeException("Não é uma função: " + alvo);
    }

    private void registrarNativas() {
        global.definir("tamanho", new Nativa() {
				public Object chamar(List<Object> a) {
					Object v = a.get(0);
					if (v instanceof String) return Double.valueOf(((String) v).length());
					if (v instanceof List)   return Double.valueOf(((List) v).size());
					if (v instanceof Map)    return Double.valueOf(((Map) v).size());
					return Double.valueOf(0);
				}
			});
        global.definir("texto", new Nativa() {
				public Object chamar(List<Object> a) { return formatar(a.get(0)); }
			});
        global.definir("numero", new Nativa() {
				public Object chamar(List<Object> a) { return Double.valueOf(numero(a.get(0))); }
			});
        global.definir("tipo", new Nativa() {
				public Object chamar(List<Object> a) { return tipo(a.get(0)); }
			});
        global.definir("leia", new Nativa() {
				public Object chamar(List<Object> a) {
					String pergunta = a.isEmpty() ? "" : formatar(a.get(0));
					if (ui == null) {
						throw new RuntimeException("leia() requer interface gráfica");
					}
					return ui.pedirTexto(pergunta);
				}
			});
    }

    private Object soma(Object a, Object b) {
        if (a instanceof String || b instanceof String)
            return formatar(a) + formatar(b);
        return Double.valueOf(numero(a) + numero(b));
    }

    private boolean verdadeiro(Object v) {
        if (v == null) return false;
        if (v instanceof Boolean) return ((Boolean) v).booleanValue();
        return true;
    }

    private double numero(Object v) {
        if (v instanceof Double)  return ((Double) v).doubleValue();
        if (v instanceof Integer) return ((Integer) v).doubleValue();
        if (v instanceof String)  return Double.parseDouble((String) v);
        throw new RuntimeException("Esperava número, veio: " + v);
    }

    private boolean iguais(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        if (a instanceof Number && b instanceof Number)
            return numero(a) == numero(b);
        return a.equals(b);
    }

    @SuppressWarnings("unchecked")
    private int comparar(Object a, Object b) {
        if (a instanceof Number && b instanceof Number)
            return Double.compare(numero(a), numero(b));
        if (a instanceof String && b instanceof String)
            return ((String) a).compareTo((String) b);
        if (a instanceof Comparable && a.getClass() == b.getClass())
            return ((Comparable<Object>) a).compareTo(b);
        throw new RuntimeException("Não é possível comparar: " + a + " e " + b);
    }

    public String formatar(Object v) {
        if (v == null) return "nulo";
        if (v instanceof Double) {
            double d = ((Double) v).doubleValue();
            if (d == Math.floor(d) && !Double.isInfinite(d))
                return String.valueOf((long) d);
            return String.valueOf(d);
        }
        return String.valueOf(v);
    }

    private String tipo(Object v) {
        if (v == null) return "nulo";
        if (v instanceof Double)  return "numero";
        if (v instanceof String)  return "texto";
        if (v instanceof Boolean) return "booleano";
        if (v instanceof List)    return "lista";
        if (v instanceof Map)     return "mapa";
        if (v instanceof FuncaoTupi) return "funcao";
        if (v instanceof Classe)  return "classe";
        if (v instanceof Instancia) return ((Instancia) v).classe.nome;
        if (v instanceof ModoloNativo) return "modulo";
        if (v instanceof ServidorApp) return "servidor";
        if (v instanceof ServidorPedido) return "pedido";
        return v.getClass().getSimpleName();
    }

    // ========== ACESSAR — agora com suporte a Servidor ==========
    private Object acessar(Object obj, String nome) {

        // ===== SERVIDOR APP =====
        if (obj instanceof ServidorApp) {
			final ServidorApp app = (ServidorApp) obj;
			final interpretador self = this;

			if (nome.equals("rota")) {
				return new Nativa() {
					public Object chamar(List<Object> a) {
						String metodo = formatar(a.get(0));
						String caminho = formatar(a.get(1));
						Object funcao = a.get(2);
						app.adicionarRota(metodo, caminho, funcao);
						return null;
					}
				};
			}
			if (nome.equals("inicia")) {
				return new Nativa() {
					public Object chamar(List<Object> a) {
						int porta = (int) numero(a.get(0));
						try {
							app.iniciar(self, porta);
						} catch (Exception e) {
							throw new RuntimeException("Erro ao iniciar: " + e.getMessage());
						}
						return null;
					}
				};
			}
			if (nome.equals("para")) {
				return new Nativa() {
					public Object chamar(List<Object> a) {
						app.parar();
						return null;
					}
				};
			}
			if (nome.equals("porta")) {
				return Double.valueOf(app.porta());
			}
			if (nome.equals("rodando")) {
				return Boolean.valueOf(app.estaRodando());
			}
			if (nome.equals("rotas")) {
				return Double.valueOf(app.numeroRotas());
			}
		}
        // ===== SERVIDOR PEDIDO =====
        if (obj instanceof ServidorPedido) {
			final ServidorPedido p = (ServidorPedido) obj;

			if (nome.equals("parametro")) {
				return new Nativa() {
					public Object chamar(List<Object> a) {
						String chave = formatar(a.get(0));
						String valor = p.parametros.get(chave);
						return valor != null ? valor : "";
					}
				};
			}
			if (nome.equals("corpo")) return p.corpo;
			if (nome.equals("metodo")) return p.metodo;
			if (nome.equals("caminho")) return p.caminho;
		}

        // ===== MÓDULO NATIVO =====
        if (obj instanceof ModoloNativo) {
            ModoloNativo m = (ModoloNativo) obj;
            if (m.membros.containsKey(nome)) return m.membros.get(nome);
            throw new RuntimeException("Módulo '" + m.nome + "' não tem '" + nome + "'");
        }
        if (obj instanceof Instancia) {
            Instancia inst = (Instancia) obj;
            if (inst.campos.containsKey(nome)) return inst.campos.get(nome);
            Instr.MetodoClasse m = inst.classe.buscarMetodo(nome);
            if (m != null) return new MetadoLigado(inst, m);
            throw new RuntimeException("'" + nome + "' não existe em " +
                                       inst.classe.nome);
        }
        if (obj instanceof Map) {
            Map<?, ?> m = (Map<?, ?>) obj;
            if (m.containsKey(nome)) return m.get(nome);
        }
        throw new RuntimeException("Sem propriedade '" + nome + "' em " + obj);
    }

    private Object indexar(Object obj, Object chave) {
        if (obj instanceof List) return ((List<?>) obj).get((int) numero(chave));
        if (obj instanceof Map)  return ((Map<?, ?>) obj).get(chave);
        if (obj instanceof String)
            return String.valueOf(((String) obj).charAt((int) numero(chave)));
        throw new RuntimeException("Não indexável: " + obj);
    }

    @SuppressWarnings("unchecked")
    private List<Object> castLista(Object o) { return (List<Object>) o; }

    @SuppressWarnings("unchecked")
    private Map<Object, Object> castMapa(Object o) { return (Map<Object, Object>) o; }
}
