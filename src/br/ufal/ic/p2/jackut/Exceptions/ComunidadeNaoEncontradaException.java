package br.ufal.ic.p2.jackut.Exceptions;

public class ComunidadeNaoEncontradaException extends RuntimeException {
    public ComunidadeNaoEncontradaException() {
        super("Comunidade não existe.");
    }
}
