package br.ufal.ic.p2.jackut.Exceptions;

public class ExistentRelantionshipException extends Exception{
    public ExistentRelantionshipException(String relation) {
        super("Usu�rio j� est� adicionado como " + relation.toLowerCase() + ".");
    }
}
