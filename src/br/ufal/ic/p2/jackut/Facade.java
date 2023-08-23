package br.ufal.ic.p2.jackut;

import java.util.HashMap;
import java.util.Map;

public class Facade {
    private Map<String, Usuario> usuarios = new HashMap<>();
    private Usuario usuarioLogado = null;

    public void criarUsuario(String login, String senha, String nome) {
        if (login == null || login.isEmpty()) {
            throw new RuntimeException("Login inv�lido.");
        }

        if( senha == null || senha.isEmpty()) {
            throw new RuntimeException("Senha inv�lida.");
        }

        if (!usuarios.containsKey(login)) {
            Usuario novoUsuario = new Usuario(login, senha, nome);
            usuarios.put(login, novoUsuario);
        } else {
            throw new RuntimeException("Conta com esse nome j� existe.");
        }
    }

    public void abrirSessao(String login, String senha) {
        if (login == null || senha == null) {
            throw new RuntimeException("Login ou senha inv�lidos.");
        }

        Usuario usuario = usuarios.get(login);
        if (usuario == null || !usuario.validarSenha(senha)) {
            throw new RuntimeException("Login ou senha inv�lidos.");
        }
        usuarioLogado = usuario;
    }

    public String getAtributoUsuario(String login, String atributo) {
        if (usuarios.containsKey(login)) {
            switch (atributo) {
                case "nome":
                    return usuarios.get(login).getNome();
                default:
                    throw new RuntimeException("Atributo inv�lido.");
            }
        } else {
            throw new RuntimeException("Usu�rio n�o cadastrado.");
        }
    }

    public void zerarSistema() {
        usuarios.clear();
        usuarioLogado = null;
    }

    public void encerrarSistema() {
        zerarSistema();
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
    }
}
