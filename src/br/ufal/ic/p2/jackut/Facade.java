package br.ufal.ic.p2.jackut;

import java.util.HashMap;
import java.util.Map;

public class Facade {
    private Map<String, Usuario> usuarios = new HashMap<>();
    private Usuario usuarioLogado = null;

    public void criarUsuario(String login, String senha, String nome) {
        if (login == null || login.isEmpty()) {
            throw new RuntimeException("Login inválido.");
        }

        if( senha == null || senha.isEmpty()) {
            throw new RuntimeException("Senha inválida.");
        }

        if (!usuarios.containsKey(login)) {
            Usuario novoUsuario = new Usuario(login, senha, nome);
            novoUsuario.setId("${id"+(usuarios.size()+1)+"}"); //id fica nessa notaçao
            usuarios.put(login, novoUsuario);
        } else {
            throw new RuntimeException("Conta com esse nome já existe.");
        }
    }

    public void abrirSessao(String login, String senha) {
        if (login == null || senha == null) {
            throw new RuntimeException("Login ou senha inválidos.");
        }

        Usuario usuario = usuarios.get(login);
        if (usuario == null || !usuario.validarSenha(senha)) {
            throw new RuntimeException("Login ou senha inválidos.");
        }
        usuarioLogado = usuario;
    }

    public String getAtributoUsuario(String login, String atributo) {
        Usuario user = usuarios.get(login);

        if (usuarios.containsKey(login)) {
            if(atributo.equals("nome")){
                return usuarios.get(login).getNome();
            }
            if(user.getAtributos().containsKey(atributo)) {
                return (String) user.getAtributos().get(atributo);
            }
            else{
                throw new RuntimeException("Atributo não preenchido.");
            }
        } else {
            throw new RuntimeException("Usuário não cadastrado.");
        }
    }

    public void zerarSistema() {
        usuarios.clear();
        usuarioLogado = null;
    }

    public String userFindById(String id){
        if(id == null){
            id = "${id"+1+"}";
        }
        for (Map.Entry<String, Usuario> entry : usuarios.entrySet()) {
            Usuario user = entry.getValue();
            if(user.getId().equals(id)) { //found it
                return user.getLogin();
            }
        }
        return null;
    }

    public void encerrarSistema() {
        usuarioLogado = null;
    }

    public void editarPerfil(String id, String atributo, String valor){

        //search users by id
        String login = userFindById(id);
        if(login == null){
            throw new RuntimeException("Usuário não cadastrado.");
        }

    }


}
