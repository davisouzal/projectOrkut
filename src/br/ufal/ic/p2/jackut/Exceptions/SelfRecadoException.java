package br.ufal.ic.p2.jackut.Exceptions;

public class SelfRecadoException extends RuntimeException{
    public SelfRecadoException() {
        super("Usuário não pode enviar recado para si mesmo.");
    }
}
