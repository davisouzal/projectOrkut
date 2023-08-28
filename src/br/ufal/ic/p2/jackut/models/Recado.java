package br.ufal.ic.p2.jackut.models;

public class Recado {
    private final Usuario remetente;
    private final Usuario destinatario;
    private final String recado;
    public Recado(Usuario remetente, Usuario destinatario, String recado) {
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.recado = recado;
    }
    public Usuario getRemetente() {
        return this.remetente;
    }

    public Usuario getDestinatario() {
        return this.destinatario;
    }

    public String getRecado() {
        return this.recado;
    }
}