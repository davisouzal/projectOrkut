package br.ufal.ic.p2.jackut.Exceptions;

public class EsperandoConviteException extends RuntimeException{
    public EsperandoConviteException() {
        super("Usu�rio j� est� adicionado como amigo, esperando aceita��o do convite.");
    }
}
