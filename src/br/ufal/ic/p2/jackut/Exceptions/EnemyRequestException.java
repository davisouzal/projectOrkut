package br.ufal.ic.p2.jackut.Exceptions;

public class EnemyRequestException extends RuntimeException {
    public EnemyRequestException(String enemyName) {
        super("Fun��o inv�lida: "+ enemyName +" � seu inimigo.");
    }
}
