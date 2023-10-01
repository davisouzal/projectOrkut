package br.ufal.ic.p2.jackut.Exceptions;

public class NaoHaMensagensException extends RuntimeException{
    public NaoHaMensagensException(){
        super("Não há mensagens.");
    }
}
