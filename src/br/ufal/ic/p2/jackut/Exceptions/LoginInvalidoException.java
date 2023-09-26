package br.ufal.ic.p2.jackut.Exceptions;

public class LoginInvalidoException extends RuntimeException{
    public LoginInvalidoException() {
        super("Login inválido.");
    }
}
