package br.ufal.ic.p2.jackut.Exceptions;

public class AlreadyEnemyException extends Exception{
    public AlreadyEnemyException(String user){
        super("Fun��o inv�lida: " + user + " � seu inimigo.");
    }
}
