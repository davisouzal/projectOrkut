package br.ufal.ic.p2.jackut.Exceptions;

public class AmigoJaAdicionadoException extends RuntimeException{
    public AmigoJaAdicionadoException() {
        super("Usu�rio j� est� adicionado como amigo.");
    }
}
