package br.ufal.ic.p2.jackut.Exceptions;
public class NaoHaRecados extends RuntimeException {
    public NaoHaRecados() {
        super("Não há recados.");
    }
}