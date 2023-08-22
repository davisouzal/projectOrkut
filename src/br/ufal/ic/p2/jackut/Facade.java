package br.ufal.ic.p2.jackut;

import java.util.HashMap;
import java.util.Map;

public class Facade {
    private Map<String, Usuario> usuarios = new HashMap<>();
    private Usuario usuarioLogado = null;

    public void criarUsuario(String login, String senha, String nome) {

        Usuario novoUsuario = new Usuario(login, senha, nome);
        //caso o usuario insira um login vazio retorna essa exce��o
        if(novoUsuario.getLogin()==null){
            throw new RuntimeException("Login inv�lido.");
        }//caso o ususario insira uma senha vazia retorna essa exce��o
        if(novoUsuario.getSenha()==null){
            throw new RuntimeException("Senha inv�lida.");
        }
        if(usuarios.containsKey(login)){
            throw new RuntimeException("Conta com esse nome j� existe.");
        }
        usuarios.put(login, novoUsuario);
    }

    public void abrirSessao(String login, String senha) {
        //verifica se entre os ususarios cont�m o login inserido, caso contrario, lan�a a exce��o.
        if (usuarios.containsKey(login)) {
            Usuario usuario = usuarios.get(login);
            if (usuario.validarSenha(senha)) {
                usuarioLogado = usuario;
            } else {//caso a senha nao sexa validada, lan�a a exce��o
                throw new RuntimeException("Login ou senha inv�lidos.");
            }
        } else {
            throw new RuntimeException("Login ou senha inv�lidos.");
        }
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

    public void encerrarSistema(){
        usuarios.clear();
        usuarioLogado = null;
    }
}

class Usuario {
    private String login;
    private String senha;
    private String nome;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

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
