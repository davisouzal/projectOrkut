package br.ufal.ic.p2.jackut;

import java.util.HashMap;
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
    private final Map<String, String> atributos = new HashMap<>();


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

    public Map getAtributos() {
        return atributos;
    }
}