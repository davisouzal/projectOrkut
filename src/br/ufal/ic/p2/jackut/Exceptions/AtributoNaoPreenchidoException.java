package br.ufal.ic.p2.jackut.Exceptions;

public class AtributoNaoPreenchidoException extends RuntimeException{
    public AtributoNaoPreenchidoException() {
        super("Atributo não preenchido.");
    }
}
