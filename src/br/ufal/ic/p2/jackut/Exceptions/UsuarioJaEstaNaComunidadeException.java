package br.ufal.ic.p2.jackut.Exceptions;

public class UsuarioJaEstaNaComunidadeException extends RuntimeException {
    public UsuarioJaEstaNaComunidadeException() {
        super("Usuario j� faz parte dessa comunidade.");
    }
}
