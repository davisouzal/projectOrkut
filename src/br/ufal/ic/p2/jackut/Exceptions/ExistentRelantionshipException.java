package br.ufal.ic.p2.jackut.Exceptions;

public class ExistentRelantionshipException extends Exception{
    public ExistentRelantionshipException(String relation) {
        super("Usuário já está adicionado como " + relation.toLowerCase() + ".");
    }
}
