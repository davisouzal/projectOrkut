package br.ufal.ic.p2.jackut.Exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("Usuário não cadastrado.");
    }
}
