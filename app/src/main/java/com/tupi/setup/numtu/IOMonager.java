package com.tupi.setup.numtu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class IOMonager {

    private File pastaBase;

    public IOMonager(File pasta) {
        this.pastaBase = pasta;
        if (!pastaBase.exists()) pastaBase.mkdirs();
    }

    public File pasta() { return pastaBase; }

    public String lerArquivo(String nome) {
        try {
            File f = new File(pastaBase, nome);
            if (!f.exists()) throw new RuntimeException("Arquivo não encontrado: " + nome);
            StringBuilder sb = new StringBuilder();
            BufferedReader r = new BufferedReader(new FileReader(f));
            String linha;
            while ((linha = r.readLine()) != null) {
                sb.append(linha).append('\n');
            }
            r.close();
            return sb.toString();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro lendo " + nome + ": " + e.getMessage());
        }
    }

    public String lerArquivoExterno(String caminhoCompleto) {
        try {
            File f = new File(caminhoCompleto);
            if (!f.exists()) throw new RuntimeException("Arquivo não encontrado: " + caminhoCompleto);
            StringBuilder sb = new StringBuilder();
            BufferedReader r = new BufferedReader(new FileReader(f));
            String linha;
            while ((linha = r.readLine()) != null) {
                sb.append(linha).append('\n');
            }
            r.close();
            return sb.toString();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro lendo " + caminhoCompleto + ": " + e.getMessage());
        }
    }

    public void escreverArquivo(String nome, String conteudo) {
        try {
            File f = new File(pastaBase, nome);
            PrintWriter w = new PrintWriter(new FileWriter(f));
            w.print(conteudo);
            w.close();
        } catch (Exception e) {
            throw new RuntimeException("Erro escrevendo " + nome + ": " + e.getMessage());
        }
    }

    public boolean existe(String nome) {
        return new File(pastaBase, nome).exists();
    }

    public void apagar(String nome) {
        File f = new File(pastaBase, nome);
        if (f.exists()) f.delete();
    }

    public String[] listar() {
        File[] files = pastaBase.listFiles();
        if (files == null) return new String[0];
        String[] nomes = new String[files.length];
        for (int i = 0; i < files.length; i++) nomes[i] = files[i].getName();
        return nomes;
    }
}
