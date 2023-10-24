package br.ufal.ic.p2.jackut.models;

public class Mensagem {
    private final String mensagem;
    private final Usuario remetente;

    public Mensagem(String mensagem, Usuario remetente) {
        this.mensagem = mensagem;
        this.remetente = remetente;
    }

    public String getMensagem() {
        return this.mensagem;
    }

    public Usuario getRemetente() {
        return this.remetente;
    }
}