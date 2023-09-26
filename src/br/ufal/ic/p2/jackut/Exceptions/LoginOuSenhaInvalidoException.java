package br.ufal.ic.p2.jackut.Exceptions;

public class LoginOuSenhaInvalidoException extends RuntimeException{
    public LoginOuSenhaInvalidoException() {
        super("Login ou senha inválidos.");
    }
}
