package br.ufal.ic.p2.jackut.models;

public class Recado extends Text{
    private final Usuario destinatario;
    public Recado(Usuario remetente, Usuario destinatario, String recado) {
        super(recado, remetente);
        this.destinatario = destinatario;

    }
    public Usuario getRemetente() {
        return this.remetente;
    }

    public Usuario getDestinatario() {
        return this.destinatario;
    }

}