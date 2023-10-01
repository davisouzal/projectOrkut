package br.ufal.ic.p2.jackut.Exceptions;

public class AutoRelationshipException extends RuntimeException{
    public AutoRelationshipException(String relation) {
        super(
                relation.equalsIgnoreCase("amizade")
                        ?
                        "Usuário não pode adicionar a si mesmo como amigo."
                        :
                        "Usuário não pode ser " + relation.toLowerCase() + " de si mesmo."
        );
    }
}
