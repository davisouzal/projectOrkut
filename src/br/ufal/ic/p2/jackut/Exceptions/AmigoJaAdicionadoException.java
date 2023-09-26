package br.ufal.ic.p2.jackut.Exceptions;

public class AmigoJaAdicionadoException extends RuntimeException{
    public AmigoJaAdicionadoException() {
        super("Usuário já está adicionado como amigo.");
    }
}
