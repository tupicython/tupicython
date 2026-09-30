package com.tupi.setup.motor;

import android.app.Activity;
import android.widget.*;
import com.tupi.setup.interpretador.interpretador;
import com.tupi.setup.interpretador.InterfaceSaida;

import java.util.*;

public class TelaMotor {

    private static Activity activity = null;
    private static LinearLayout layout = null;
    private static interpretador interp = null;
    private static final List<Object> componentes = new ArrayList<Object>();
    private static TextView saidaTexto = null;

    public static synchronized void inicia(Activity a, interpretador i) {
        activity = a;
        if (i != null) interp = i;
        if (com.tupi.setup.TelaActivity.instancia == null) {
            saidaTexto = null;
            layout = null;
            componentes.clear();
        }
    }

    private static Activity resolverActivity() {
        if (com.tupi.setup.TelaActivity.instancia != null) {
            activity = com.tupi.setup.TelaActivity.instancia;
        }
        return activity;
    }

    private static boolean layoutValido(Activity act) {
        return layout != null && layout.getContext() == act;
    }

    private static void garantirLayout(final Activity act) {
        if (layoutValido(act)) return;
        act.runOnUiThread(new Runnable() {
                public void run() {
                    layout = new LinearLayout(act);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(32, 32, 32, 32);

                    saidaTexto = new TextView(act);
                    saidaTexto.setTextSize(16);
                    saidaTexto.setPadding(16, 16, 16, 16);
                    layout.addView(saidaTexto);

                    act.setContentView(layout);
                    componentes.clear();
                }
            });
        long limite = System.currentTimeMillis() + 1500;
        while (!layoutValido(act) && System.currentTimeMillis() < limite) {
            try { Thread.sleep(20); } catch (Exception e) { }
        }
    }

    public static String valor(Object view) {
        if (view instanceof EditText) {
            return ((EditText) view).getText().toString();
        }
        if (view instanceof CheckBox) {
            return ((CheckBox) view).isChecked() ? "verdadeiro" : "falso";
        }
        if (view instanceof SeekBar) {
            return String.valueOf(((SeekBar) view).getProgress());
        }
        return "";
    }

    public static void escreve(final String texto) {
        if (interp != null && interp.getSaidaUI() != null) {
            interp.getSaidaUI().escrever(texto);
        }
    }

    // ---------------------------------------------------------------
    // janela()
    // ---------------------------------------------------------------
    public static Object janela() {
        final Activity base = activity;
        if (base == null) return "erro";

        if (com.tupi.setup.TelaActivity.instancia != null) {
            activity = com.tupi.setup.TelaActivity.instancia;
            garantirLayout(activity);
            if (base != activity && base instanceof com.tupi.setup.SaidaActivity) {
                base.finish();
            }
            return "janela";
        }

        base.runOnUiThread(new Runnable() {
                public void run() {
                    android.content.Intent intent = new android.content.Intent(
                        base, com.tupi.setup.TelaActivity.class);
                    base.startActivity(intent);
                }
            });

        long limite = System.currentTimeMillis() + 3000;
        while (com.tupi.setup.TelaActivity.instancia == null
               && System.currentTimeMillis() < limite) {
            try { Thread.sleep(50); } catch (Exception e) { }
        }

        if (com.tupi.setup.TelaActivity.instancia != null) {
            activity = com.tupi.setup.TelaActivity.instancia;
        } else {
            activity = base;
        }

        garantirLayout(activity);

        if (base != activity && base instanceof com.tupi.setup.SaidaActivity) {
            base.runOnUiThread(new Runnable() {
                    public void run() { base.finish(); }
                });
        }

        return "janela";
    }

    // ---------------------------------------------------------------
    // adiciona_texto
    // ---------------------------------------------------------------
    public static Object adicionaTexto(final String texto) {
        final Activity act = resolverActivity();
        if (act == null) return "erro: activity nula";
        garantirLayout(act);

        act.runOnUiThread(new Runnable() {
                public void run() {
                    if (layout == null || layout.getContext() != act) return;
                    TextView t = new TextView(act);
                    t.setText(texto);
                    t.setTextSize(16);
                    layout.addView(t);
                    componentes.add(t);
                }
            });
        return "texto";
    }

    // ---------------------------------------------------------------
    // adiciona_entrada
    // ---------------------------------------------------------------
    public static Object adicionaEntrada(final String placeholder) {
        final Activity act = resolverActivity();
        if (act == null) return "erro: activity nula";
        garantirLayout(act);

        final EditText e = new EditText(act);
        act.runOnUiThread(new Runnable() {
                public void run() {
                    if (layout == null) {
                        garantirLayout(act);
                        return;
                    }
                    e.setHint(placeholder);
                    e.setTextSize(16);
                    layout.addView(e);
                    componentes.add(e);
                }
            });
        return e;
    }

    // ---------------------------------------------------------------
    // adiciona_botao
    // ---------------------------------------------------------------
    public static Object adicionaBotao(final String texto, final Object funcao) {
        final Activity act = resolverActivity();
        if (act == null) return "erro: activity nula";
        garantirLayout(act);

        final Button b = new Button(act);
        act.runOnUiThread(new Runnable() {
                public void run() {
                    if (layout == null || layout.getContext() != act) return;
                    b.setText(texto);
                    b.setOnClickListener(new android.view.View.OnClickListener() {
                            public void onClick(android.view.View v) {
                                new Thread(new Runnable() {
                                        public void run() {
                                            final InterfaceSaida saidaAntiga =
                                                (interp != null) ? interp.getSaidaUI() : null;

                                            if (interp != null) {
                                                interp.setSaidaUI(new InterfaceSaida() {
                                                        public void escrever(final String linha) {
                                                            act.runOnUiThread(new Runnable() {
                                                                    public void run() {
                                                                        if (saidaTexto != null) {
                                                                            saidaTexto.append(linha + "\n");
                                                                        }
                                                                    }
                                                                });
                                                        }
                                                    });
                                            }

                                            try {
                                                List<Object> args = new ArrayList<Object>();
                                                if (interp != null) {
                                                    interp.chamarFuncao(funcao, args);
                                                }
                                            } catch (Exception ex) {
                                                // silencioso
                                            } finally {
                                                if (interp != null) {
                                                    interp.setSaidaUI(saidaAntiga);
                                                }
                                            }
                                        }
                                    }).start();
                            }
                        });
                    layout.addView(b);
                    componentes.add(b);
                }
            });
        return b;
    }

    // ---------------------------------------------------------------
    // adiciona_imagem
    // ---------------------------------------------------------------
    public static Object adicionaImagem(final String caminho) {
        final Activity act = resolverActivity();
        if (act == null) return "erro: activity nula";
        garantirLayout(act);

        final ImageView img = new ImageView(act);
        act.runOnUiThread(new Runnable() {
                public void run() {
                    if (layout == null) return;
                    try {
                        java.io.File f = new java.io.File(caminho);
                        if (f.exists()) {
                            img.setImageURI(android.net.Uri.fromFile(f));
                        } else {
                            img.setImageResource(android.R.drawable.ic_menu_gallery);
                        }
                    } catch (Exception e) {
                        img.setImageResource(android.R.drawable.ic_menu_gallery);
                    }
                    img.setAdjustViewBounds(true);
                    img.setMaxHeight(600);
                    layout.addView(img);
                    componentes.add(img);
                }
            });
        return img;
    }

    // ---------------------------------------------------------------
    // adiciona_checkbox
    // ---------------------------------------------------------------
    public static Object adicionaCheckbox(final String texto) {
        final Activity act = resolverActivity();
        if (act == null) return "erro: activity nula";
        garantirLayout(act);

        final CheckBox cb = new CheckBox(act);
        act.runOnUiThread(new Runnable() {
                public void run() {
                    if (layout == null) return;
                    cb.setText(texto);
                    cb.setTextSize(16);
                    layout.addView(cb);
                    componentes.add(cb);
                }
            });
        return cb;
    }

    // ---------------------------------------------------------------
    // adiciona_slider
    // ---------------------------------------------------------------
    public static Object adicionaSlider(final double min, final double max) {
        final Activity act = resolverActivity();
        if (act == null) return "erro: activity nula";
        garantirLayout(act);

        final SeekBar sb = new SeekBar(act);
        act.runOnUiThread(new Runnable() {
                public void run() {
                    if (layout == null) return;
                    sb.setMax((int)(max - min));
                    sb.setProgress(0);
                    layout.addView(sb);
                    componentes.add(sb);
                }
            });
        return sb;
    }

    // ---------------------------------------------------------------
    // adiciona_espaco
    // ---------------------------------------------------------------
    public static Object adicionaEspaco(final double altura) {
        final Activity act = resolverActivity();
        if (act == null) return "erro: activity nula";
        garantirLayout(act);

        act.runOnUiThread(new Runnable() {
                public void run() {
                    if (layout == null) return;
                    android.view.View espaco = new android.view.View(act);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        (int) altura
                    );
                    espaco.setLayoutParams(params);
                    layout.addView(espaco);
                    componentes.add(espaco);
                }
            });
        return "espaco";
    }

    // ---------------------------------------------------------------
    // limpar()
    // ---------------------------------------------------------------
    public static void limpar() {
        final Activity act = resolverActivity();
        if (act == null) return;
        act.runOnUiThread(new Runnable() {
                public void run() {
                    if (saidaTexto != null) {
                        saidaTexto.setText("");
                    }
                }
            });
    }

    // ---------------------------------------------------------------
    // remove_componente(view)
    // ---------------------------------------------------------------
    public static void remover(final Object view) {
        final Activity act = resolverActivity();
        if (act == null || !(view instanceof android.view.View)) return;
        final android.view.View v = (android.view.View) view;
        act.runOnUiThread(new Runnable() {
                public void run() {
                    if (layout != null) {
                        layout.removeView(v);
                        componentes.remove(v);
                    }
                }
            });
    }

    // ---------------------------------------------------------------
    // mostra()
    // ---------------------------------------------------------------
    public static void mostra() {
        final Activity act = resolverActivity();
        if (act == null) return;
        act.runOnUiThread(new Runnable() {
                public void run() {
                    if (layout != null && layout.getContext() == act) {
                        act.setContentView(layout);
                    }
                }
            });
    }
}
