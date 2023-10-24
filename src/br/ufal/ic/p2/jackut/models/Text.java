package br.ufal.ic.p2.jackut.models;

public class Text {

    String mensagem;
    Usuario remetente;

    public Text(String mensagem, Usuario remetente){
        this.mensagem = mensagem;
        this.remetente = remetente;
    }

    public String getMensagem() {
        return mensagem;
    }

    public Usuario getRemetente() {
        return remetente;
    }

    public void setRemetente(Usuario remetente) {
        this.remetente = remetente;
    }

    public void setText(String mensagem) {
        this.mensagem = mensagem;
    }
}
