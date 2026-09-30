package com.tupi.setup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tupi.setup.interpretador.InterfaceSaida;
import com.tupi.setup.interpretador.InterfaceUsuario;

import java.io.File;

public class SaidaActivity extends Activity implements InterfaceUsuario, InterfaceSaida {

    private TextView texto;
    private ScrollView scroll;
    private final Object travaLeia = new Object();
    private volatile String respostaLeia = null;
    private File pastaModulos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saida);

        texto = (TextView) findViewById(R.id.texto_saida);
        scroll = (ScrollView) findViewById(R.id.scroll_saida);
        Button botaoLimpar = (Button) findViewById(R.id.botao_limpar_saida);
        Button botaoFechar = (Button) findViewById(R.id.botao_fechar);

        final String codigo = getIntent().getStringExtra("codigo");
        String pasta = getIntent().getStringExtra("pasta");

        pastaModulos = pasta != null ? new File(pasta) : null;

        botaoLimpar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					texto.setText("");
				}
			});

        botaoFechar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			});

        // Roda o código em thread separada
        // Roda o código em thread separada
		new Thread(new Runnable() {
				public void run() {
					try {
						com.tupi.setup.InterpretadorTupi.activityAtual = SaidaActivity.this;
						com.tupi.setup.motor.TelaMotor.inicia(SaidaActivity.this, null);
						InterpretadorTupi.executar(codigo, SaidaActivity.this, pastaModulos);
					} catch (Throwable t) {
						escrever("❌ Erro: " + t.getMessage());
					}
				}
			}).start();
         }
    @Override
    public void escrever(final String linha) {
        runOnUiThread(new Runnable() {
				public void run() {
					texto.append(linha + "\n");
					scroll.post(new Runnable() {
							public void run() {
								scroll.fullScroll(View.FOCUS_DOWN);
							}
						});
				}
			});
    }

    @Override
    public String pedirTexto(final String mensagem) {
        respostaLeia = null;
        runOnUiThread(new Runnable() {
				public void run() {
					final android.widget.EditText entrada = new android.widget.EditText(SaidaActivity.this);
					entrada.setHint("Digite aqui...");
					entrada.setTextColor(0xFF000000);

					new android.app.AlertDialog.Builder(SaidaActivity.this)
						.setTitle("📥 Entrada do Tupi")
						.setMessage(mensagem.isEmpty() ? "Digite um valor:" : mensagem)
						.setView(entrada)
						.setCancelable(false)
						.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
							public void onClick(android.content.DialogInterface d, int w) {
								synchronized (travaLeia) {
									respostaLeia = entrada.getText().toString();
									travaLeia.notifyAll();
								}
							}
						})
						.show();
				}
			});

        synchronized (travaLeia) {
            while (respostaLeia == null) {
                try {
                    travaLeia.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "";
                }
            }
        }
        return respostaLeia;
    }
}
