package br.ufal.ic.p2.jackut.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;


public class Usuario {
    private final String login;
    private final String senha;
    private final String nome;

    private final Queue<Recado> recados;

    //exemplo: nome: "nome", valor: "joao"
    private final List<Atributo> atributos = new ArrayList<>();

    //amigos no qual enviou friendRequest
    private final List<Usuario> conviteAmigos = new ArrayList<>();

    //amigos da lista de amigos que ACEITARAM o friendRequest
    private final List<Usuario> amigo = new ArrayList<>();

    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.recados = new LinkedList<>();

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

    public void enviarRecado(Usuario destinatario, String recado) {
        Recado r = new Recado(this, destinatario, recado);
        destinatario.recados.add(r);
    }

    public Recado getRecado() {
        return this.recados.poll();
    }

    public Queue<Recado> getRecados(){
        return this.recados;
    }
}