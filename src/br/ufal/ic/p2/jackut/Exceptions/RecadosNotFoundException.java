package br.ufal.ic.p2.jackut.Exceptions;

public class RecadosNotFoundException extends RuntimeException{
    public RecadosNotFoundException(){
        super("N�o h� recados.");
    }
}
