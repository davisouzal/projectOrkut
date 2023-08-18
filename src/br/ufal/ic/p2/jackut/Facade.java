package br.ufal.ic.p2.jackut;

import java.util.HashMap;
import java.util.Map;

public class Facade {
    private Map<String, Usuario> usuarios = new HashMap<>();
    private Usuario usuarioLogado = null;

    public void criarUsuario(String login, String senha, String nome) {
        if (!usuarios.containsKey(login)) {
            Usuario novoUsuario = new Usuario(login, senha, nome);
            usuarios.put(login, novoUsuario);
        } else {
            throw new RuntimeException("Conta com esse nome já existe.");
        }
    }

    public void abrirSessao(String login, String senha) {
        if (usuarios.containsKey(login)) {
            Usuario usuario = usuarios.get(login);
            if (usuario.validarSenha(senha)) {
                usuarioLogado = usuario;
            } else {
                throw new RuntimeException("Senha inválida.");
            }
        } else {
            throw new RuntimeException("Login inválido.");
        }
    }

    public String getAtributoUsuario(String login, String atributo) {
        if (usuarios.containsKey(login)) {
            switch (atributo) {
                case "nome":
                    return usuarios.get(login).getNome();
                default:
                    throw new RuntimeException("Atributo inválido.");
            }
        } else {
            throw new RuntimeException("Usuário não cadastrado.");
        }
    }

    public void zerarSistema() {
        usuarios.clear();
        usuarioLogado = null;
    }
}

class Usuario {
    private String login;
    private String senha;
    private String nome;

    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public boolean validarSenha(String senha) {
        return this.senha.equals(senha);
    }
}
