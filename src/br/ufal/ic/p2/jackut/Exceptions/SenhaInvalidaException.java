package br.ufal.ic.p2.jackut.Exceptions;

public class SenhaInvalidaException extends RuntimeException{
    public SenhaInvalidaException() {
        super("Senha inválida.");
    }
}
