package br.ufal.ic.p2.jackut.Exceptions;

public class SelfRecadoException extends RuntimeException{
    public SelfRecadoException() {
        super("Usu�rio n�o pode enviar recado para si mesmo.");
    }
}
