package com.tupi.setup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.File;

public class MainActivity extends Activity {

    private EditText editor;
    private File pastaModulos;

    private static final String CODIGO_PADRAO =
	"// Bem-vindo ao TupiCython!\n" +
	"importe matematica\n" +
	"escreva(\"Pi = \" .. matematica.pi)\n" +
	"escreva(\"Raiz de 25 = \" .. matematica.raiz(25))\n" +
	"escreva(\"Seno de 0 = \" .. matematica.seno(0))\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        java.io.File pastaDados = new java.io.File(getExternalFilesDir(null), "dados");
        if (!pastaDados.exists()) pastaDados.mkdirs();
        com.tupi.setup.numtu.MeduloNumtu.ioMonager =
            new com.tupi.setup.numtu.IOMonager(pastaDados);

        editor = (EditText) findViewById(R.id.editor);
        Button botaoExecutar = (Button) findViewById(R.id.botao_executar);
        Button botaoSalvar = (Button) findViewById(R.id.botao_salvar);
        Button botaoCarregar = (Button) findViewById(R.id.botao_carregar);
        Button botaoLimpar = (Button) findViewById(R.id.botao_limpar);
        Button botaoExemplos = (Button) findViewById(R.id.botao_exemplos);

        editor.setText(CODIGO_PADRAO);
        SyntaxHighLighter.aplicar(editor.getText());

        editor.addTextChangedListener(new android.text.TextWatcher() {
				public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
				public void onTextChanged(CharSequence s, int a, int b, int c) {}
				public void afterTextChanged(android.text.Editable s) {
					int posCursor = editor.getSelectionStart();
					SyntaxHighLighter.aplicar(s);
					editor.setSelection(posCursor);
				}
			});

        pastaModulos = new File(getExternalFilesDir(null), "modulos");
        if (!pastaModulos.exists()) pastaModulos.mkdirs();
		
		File pastaRepositorio = new File(getExternalFilesDir(null), "repositorio");
		if (!pastaRepositorio.exists()) pastaRepositorio.mkdirs();
		
        botaoExecutar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					String codigo = editor.getText().toString();
					android.content.Intent intent = new android.content.Intent(
						MainActivity.this, SaidaActivity.class);
					intent.putExtra("codigo", codigo);
					intent.putExtra("pasta", pastaModulos.getAbsolutePath());
					startActivity(intent);
				}
			});

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) { salvarCodigo(); }
			});

        botaoCarregar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) { carregarCodigo(); }
			});

        botaoLimpar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					editor.setText("");
				}
			});

        botaoExemplos.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) { mostrarExemplos(); }
			});
    }

    private void mostrarExemplos() {
        new AlertDialog.Builder(MainActivity.this)
            .setTitle("📚 Exemplos")
            .setItems(ExemplosTupi.NOMES, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface d, int which) {
                    editor.setText(ExemplosTupi.pegar(which));            
                }
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void salvarCodigo() {
        final EditText entrada = new EditText(MainActivity.this);
        entrada.setHint("Nome do arquivo");
        entrada.setTextColor(0xFF000000);

        new AlertDialog.Builder(MainActivity.this)
            .setTitle("💾 Salvar código")
            .setView(entrada)
            .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface d, int w) {
                    String nome = entrada.getText().toString().trim();
                    if (nome.isEmpty()) {
                        android.widget.Toast.makeText(MainActivity.this,
													  "Nome vazio!", android.widget.Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!nome.endsWith(".tupi")) nome = nome + ".tupi";
                    try {
                        pastaModulos.mkdirs();
                        java.io.File f = new java.io.File(pastaModulos, nome);
                        java.io.FileWriter w2 = new java.io.FileWriter(f);
                        w2.write(editor.getText().toString());
                        w2.close();
                        android.widget.Toast.makeText(MainActivity.this,
													  "✓ Salvo: " + nome,
													  android.widget.Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        android.widget.Toast.makeText(MainActivity.this,
													  "Erro: " + e.getMessage(),
													  android.widget.Toast.LENGTH_LONG).show();
                    }
                }
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void carregarCodigo() {
        java.io.File[] arquivos = pastaModulos.listFiles();
        if (arquivos == null || arquivos.length == 0) {
            android.widget.Toast.makeText(MainActivity.this,
										  "Nenhum arquivo salvo.",
										  android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        final String[] nomes = new String[arquivos.length];
        for (int i = 0; i < arquivos.length; i++) nomes[i] = arquivos[i].getName();

        new AlertDialog.Builder(MainActivity.this)
            .setTitle("📂 Abrir arquivo")
            .setItems(nomes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface d, int which) {
                    try {
                        java.io.File f = new java.io.File(pastaModulos, nomes[which]);
                        java.io.BufferedReader r = new java.io.BufferedReader(
                            new java.io.FileReader(f));
                        StringBuilder sb = new StringBuilder();
                        String linha;
                        while ((linha = r.readLine()) != null) {
                            sb.append(linha).append('\n');
                        }
                        r.close();
                        editor.setText(sb.toString());
                        android.widget.Toast.makeText(MainActivity.this,
													  "✓ Carregado: " + nomes[which],
													  android.widget.Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        android.widget.Toast.makeText(MainActivity.this,
													  "Erro: " + e.getMessage(),
													  android.widget.Toast.LENGTH_LONG).show();
                    }
                }
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }
}
