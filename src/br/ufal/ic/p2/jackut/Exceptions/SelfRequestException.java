package br.ufal.ic.p2.jackut.Exceptions;

public class SelfRequestException extends RuntimeException{
    public SelfRequestException() {
        super("Usuário não pode adicionar a si mesmo como amigo.");
    }
}
