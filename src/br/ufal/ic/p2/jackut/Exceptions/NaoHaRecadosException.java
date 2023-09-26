package br.ufal.ic.p2.jackut.Exceptions;
public class NaoHaRecadosException extends RuntimeException {
    public NaoHaRecadosException() {
        super("Não há recados.");
    }
}