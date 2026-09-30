package com.tupi.setup.interpretador;

import android.app.Activity;
import android.widget.TextView;

public class SaidaTela implements InterfaceSaida {
    private final Activity activity;
    private final TextView texto;

    public SaidaTela(Activity activity, TextView texto) {
        this.activity = activity;
        this.texto = texto;
    }

    public void escrever(final String linha) {
        activity.runOnUiThread(new Runnable() {
				public void run() {
					texto.append(linha + "\n");
				}
			});
    }
}
