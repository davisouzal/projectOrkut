package br.ufal.ic.p2.jackut.Exceptions;

public class UserNotFound extends RuntimeException{
    public UserNotFound() {
        super("Usuário não cadastrado.");
    }
}
