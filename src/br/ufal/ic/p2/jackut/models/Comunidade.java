package br.ufal.ic.p2.jackut.models;

import java.util.ArrayList;
import java.util.List;

public class Comunidade {
    private final String nome;
    private final String descricaoComunidade;
    private final Usuario dono;

    private final ArrayList<Usuario> membros = new ArrayList<>();

    public Comunidade(String nome, String descricao, Usuario dono) {
        this.nome = nome;
        this.descricaoComunidade = descricao;
        this.dono = dono;
        this.membros.add(dono);
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricaoComunidade;
    }

    public Usuario getDono() {
        return dono;
    }

    public List<Usuario> getMembros() {
        return membros;
    }

    public void enviarMensagem(Mensagem mensagem    ) {
        for (Usuario membro : membros) {
            membro.receberMensagem(mensagem);
        }
    }

}
