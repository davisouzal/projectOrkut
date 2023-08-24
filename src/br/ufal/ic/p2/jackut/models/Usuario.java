package br.ufal.ic.p2.jackut.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Usuario {
    private final String login;
    private final String senha;
    private final String nome;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private final List<Atributo> atributos = new ArrayList<>();

    //amigos no qual enviou friendRequest
    private final List<Usuario> conviteAmigos = new ArrayList<>();

    //amigos da lista de amigos que ACEITARAM o friendRequest
    private final List<Usuario> amigo = new ArrayList<>();

    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;

    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public String getNome() {
        return nome;
    }

    public boolean validarSenha(String senha) {
        return this.senha.equals(senha);
    }

    public List<Atributo> getAtributos() {
        return atributos;
    }

    public Atributo getAtributo(String nome) {
        for (Atributo atributo : atributos) {
            if (atributo.getNome().equals(nome)) {
                return atributo;
            }
        }
        return null;
    }

    public void addAtributo(String nome, String valor) {
        Atributo atributo = new Atributo(nome, valor);
        atributos.add(atributo);

    }

    public List<Usuario> getAmigo() {
        return amigo;
    }

    public List<Usuario> getConviteAmigos() {
        return conviteAmigos;
    }
}