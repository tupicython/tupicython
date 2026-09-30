package com.tupi.setup.motor;

import com.tupi.setup.interpretador.*;
import java.util.List;

public class ModuloTelaMotor implements MeduloTupi {

    public String nome() { return "TelaMotor"; }

    public void registrar(final Ambiente destino) {

        destino.definir("janela", new Nativa() {
                public Object chamar(List<Object> a) {
                    return TelaMotor.janela();
                }
            });

        destino.definir("adiciona_texto", new Nativa() {
                public Object chamar(List<Object> a) {
                    return TelaMotor.adicionaTexto(a.get(0).toString());
                }
            });

        destino.definir("adiciona_entrada", new Nativa() {
                public Object chamar(List<Object> a) {
                    return TelaMotor.adicionaEntrada(a.get(0).toString());
                }
            });

        destino.definir("adiciona_botao", new Nativa() {
                public Object chamar(List<Object> a) {
                    return TelaMotor.adicionaBotao(a.get(0).toString(), a.get(1));
                }
            });

        destino.definir("adiciona_imagem", new Nativa() {
                public Object chamar(List<Object> a) {
                    return TelaMotor.adicionaImagem(a.get(0).toString());
                }
            });

        destino.definir("adiciona_checkbox", new Nativa() {
                public Object chamar(List<Object> a) {
                    return TelaMotor.adicionaCheckbox(a.get(0).toString());
                }
            });

        destino.definir("adiciona_slider", new Nativa() {
                public Object chamar(List<Object> a) {
                    return TelaMotor.adicionaSlider(
                        Double.parseDouble(a.get(0).toString()),
                        Double.parseDouble(a.get(1).toString())
                    );
                }
            });

        destino.definir("adiciona_espaco", new Nativa() {
                public Object chamar(List<Object> a) {
                    return TelaMotor.adicionaEspaco(
                        Double.parseDouble(a.get(0).toString())
                    );
                }
            });

        destino.definir("limpar", new Nativa() {
                public Object chamar(List<Object> a) {
                    TelaMotor.limpar();
                    return null;
                }
            });

        destino.definir("remove", new Nativa() {
                public Object chamar(List<Object> a) {
                    TelaMotor.remover(a.get(0));
                    return null;
                }
            });

        destino.definir("valor", new Nativa() {
                public Object chamar(List<Object> a) {
                    return TelaMotor.valor(a.get(0));
                }
            });

        destino.definir("escreve", new Nativa() {
                public Object chamar(List<Object> a) {
                    TelaMotor.escreve(a.get(0).toString());
                    return null;
                }
            });

        destino.definir("mostra", new Nativa() {
                public Object chamar(List<Object> a) {
                    TelaMotor.mostra();
                    return null;
                }
            });
    }
}
