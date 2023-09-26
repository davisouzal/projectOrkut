package br.ufal.ic.p2.jackut.Exceptions;

public class EsperandoConviteException extends RuntimeException{
    public EsperandoConviteException() {
        super("Usuário já está adicionado como amigo, esperando aceitação do convite.");
    }
}
