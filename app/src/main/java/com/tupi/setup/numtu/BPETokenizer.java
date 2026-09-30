package com.tupi.setup.numtu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class BPETokenizer {

    public int vocabSize;
    public int minFreq;
    public Map<Long, Integer> merges = new HashMap<Long, Integer>();
    public Map<Integer, byte[]> vocab = new HashMap<Integer, byte[]>();

    public BPETokenizer() {
        this(3000, 2);
    }

    public BPETokenizer(int vocabSize, int minFreq) {
        this.vocabSize = vocabSize;
        this.minFreq = minFreq;
    }

    private static long parKey(int a, int b) {
        return ((long) a << 32) | (b & 0xFFFFFFFFL);
    }

    // ============ TREINO ============
    public void treinar(String texto) {
        texto = texto.toLowerCase();

        // Divide em palavras
        List<String> palavras = tokenizePalavras(texto);

        // Conta frequência
        Map<String, Integer> contagem = new HashMap<String, Integer>();
        for (String p : palavras) {
            Integer c = contagem.get(p);
            contagem.put(p, c == null ? 1 : c + 1);
        }

        // Converte pra lista de bytes
        Map<List<Integer>, Integer> words = new HashMap<List<Integer>, Integer>();
        for (Map.Entry<String, Integer> e : contagem.entrySet()) {
            List<Integer> bytes = new ArrayList<Integer>();
            for (byte b : e.getKey().getBytes()) {
                bytes.add(b & 0xFF);
            }
            Integer c = words.get(bytes);
            words.put(bytes, c == null ? e.getValue() : c + e.getValue());
        }

        // Inicializa vocab com 0-255 + especiais
        vocab.clear();
        for (int i = 0; i < 256; i++) {
            vocab.put(i, new byte[]{(byte) i});
        }
        vocab.put(256, "<pad>".getBytes());
        vocab.put(257, "<eos>".getBytes());

        int nextId = 258;

        while (nextId < vocabSize) {
            // Conta pares
            Map<Long, Integer> stats = new HashMap<Long, Integer>();
            for (Map.Entry<List<Integer>, Integer> e : words.entrySet()) {
                List<Integer> w = e.getKey();
                int freq = e.getValue();
                for (int i = 0; i < w.size() - 1; i++) {
                    long k = parKey(w.get(i), w.get(i + 1));
                    Integer c = stats.get(k);
                    stats.put(k, c == null ? freq : c + freq);
                }
            }

            if (stats.isEmpty()) break;

            // Acha o par mais frequente
            long melhorPar = 0;
            int melhorFreq = -1;
            for (Map.Entry<Long, Integer> e : stats.entrySet()) {
                if (e.getValue() > melhorFreq) {
                    melhorFreq = e.getValue();
                    melhorPar = e.getKey();
                }
            }

            if (melhorFreq < minFreq) break;

            int a = (int) (melhorPar >>> 32);
            int b = (int) (melhorPar & 0xFFFFFFFFL);

            merges.put(melhorPar, nextId);

            byte[] ba = vocab.get(a);
            byte[] bb = vocab.get(b);
            byte[] novo = new byte[ba.length + bb.length];
            System.arraycopy(ba, 0, novo, 0, ba.length);
            System.arraycopy(bb, 0, novo, ba.length, bb.length);
            vocab.put(nextId, novo);
            nextId++;

            // Aplica merge em todas as palavras
            Map<List<Integer>, Integer> novoWords = new HashMap<List<Integer>, Integer>();
            for (Map.Entry<List<Integer>, Integer> e : words.entrySet()) {
                List<Integer> w = e.getKey();
                int freq = e.getValue();
                List<Integer> nw = mergeWord(w, a, b, nextId - 1);
                Integer c = novoWords.get(nw);
                novoWords.put(nw, c == null ? freq : c + freq);
            }
            words = novoWords;
        }
    }

    private List<Integer> mergeWord(List<Integer> w, int a, int b, int novoId) {
        List<Integer> out = new ArrayList<Integer>();
        int i = 0;
        while (i < w.size()) {
            if (i < w.size() - 1 && w.get(i) == a && w.get(i + 1) == b) {
                out.add(novoId);
                i += 2;
            } else {
                out.add(w.get(i));
                i++;
            }
        }
        return out;
    }

    // ============ ENCODE ============
    public int[] codificar(String texto) {
        texto = texto.toLowerCase();
        List<String> palavras = tokenizePalavras(texto);
        List<Integer> ids = new ArrayList<Integer>();
        for (String p : palavras) {
            List<Integer> bytes = new ArrayList<Integer>();
            for (byte b : p.getBytes()) bytes.add(b & 0xFF);
            List<Integer> enc = codificarPalavra(bytes);
            ids.addAll(enc);
        }
        int[] r = new int[ids.size()];
        for (int i = 0; i < r.length; i++) r[i] = ids.get(i);
        return r;
    }

    private List<Integer> codificarPalavra(List<Integer> ids) {
        List<Integer> cur = new ArrayList<Integer>(ids);
        while (cur.size() >= 2) {
            long melhorPar = -1;
            int melhorRank = Integer.MAX_VALUE;
            int melhorIdx = -1;
            for (int i = 0; i < cur.size() - 1; i++) {
                long k = parKey(cur.get(i), cur.get(i + 1));
                Integer r = merges.get(k);
                if (r != null && r < melhorRank) {
                    melhorRank = r;
                    melhorPar = k;
                    melhorIdx = i;
                }
            }
            if (melhorIdx == -1) break;
            int novoId = merges.get(melhorPar);
            cur.set(melhorIdx, novoId);
            cur.remove(melhorIdx + 1);
        }
        return cur;
    }

    // ============ DECODE ============
    public String decodificar(int[] ids) {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        for (int id : ids) {
            byte[] b = vocab.get(id);
            if (b != null) out.write(b, 0, b.length);
        }
        return new String(out.toByteArray());
    }

    // ============ UTILS ============
    public int tamanhoVocab() {
        return vocab.size();
    }

    private List<String> tokenizePalavras(String texto) {
        List<String> palavras = new ArrayList<String>();
        StringBuilder cur = new StringBuilder();
        boolean ultimoEspaco = false;
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (Character.isWhitespace(c)) {
                if (cur.length() > 0) {
                    palavras.add(cur.toString());
                    cur.setLength(0);
                }
                cur.append(c);
                palavras.add(cur.toString());
                cur.setLength(0);
                ultimoEspaco = true;
            } else {
                if (ultimoEspaco) {
                    // já foi adicionado o espaço
                    ultimoEspaco = false;
                }
                cur.append(c);
            }
        }
        if (cur.length() > 0) palavras.add(cur.toString());
        return palavras;
    }
}
