package br.ufal.ic.p2.jackut.Exceptions;
public class NaoHaRecados extends RuntimeException {
    public NaoHaRecados() {
        super("N�o h� recados.");
    }
}