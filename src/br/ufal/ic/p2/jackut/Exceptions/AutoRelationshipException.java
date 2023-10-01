package br.ufal.ic.p2.jackut.Exceptions;

public class AutoRelationshipException extends RuntimeException{
    public AutoRelationshipException(String relation) {
        super(
                relation.equalsIgnoreCase("amizade")
                        ?
                        "Usu�rio n�o pode adicionar a si mesmo como amigo."
                        :
                        "Usu�rio n�o pode ser " + relation.toLowerCase() + " de si mesmo."
        );
    }
}
