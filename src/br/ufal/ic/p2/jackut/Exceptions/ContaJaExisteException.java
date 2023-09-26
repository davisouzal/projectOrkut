package br.ufal.ic.p2.jackut.Exceptions;

public class ContaJaExisteException extends RuntimeException{
    public ContaJaExisteException() {
        super("Conta com esse nome já existe.");
    }
}
