package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.models.Atributo;
import br.ufal.ic.p2.jackut.models.Usuario;
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
            novoUsuario.setId("${id"+(usuarios.size()+1)+"}"); //id fica nessa nota�ao?
            novoUsuario.getAtributos().add(new Atributo("nome", nome));
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
        Usuario user = usuarios.get(login);

        if (usuarios.containsKey(login)) {
            if(atributo.equals("nome")){
                return usuarios.get(login).getNome();
            }
            if(user.getAtributo(atributo)!=null){
                return user.getAtributo(atributo).getValor();
            }
            else{
                throw new RuntimeException("Atributo n�o preenchido.");
            }
        } else {
            throw new RuntimeException("Usu�rio n�o cadastrado.");
        }
    }

    public void zerarSistema() {
        usuarios.clear();
        usuarioLogado = null;
    }

    public Usuario findUserById(String id){
        for(Usuario user : usuarios.values()){
            if(user.getId().equals(id)){
                return user;
            }
        }
        return null;
    }

    public void encerrarSistema() {
        usuarioLogado = null;
    }

    public void editarPerfil(String id, String atributo, String valor) {

        //search users by id
        if (usuarioLogado == null) {
            throw new RuntimeException("Usu�rio n�o cadastrado.");
        } else {
            usuarios.get(usuarioLogado.getLogin()).addAtributo(atributo, valor);

        }
    }
    public Boolean ehAmigo(String login, String loginAmigo){
        Usuario user = usuarios.get(login);
        Usuario userAmigo = usuarios.get(loginAmigo);
        return user.getAmigo().contains(userAmigo) && userAmigo.getAmigo().contains(user);
    }

    //send friend request, se o amigo ja tiver mandado um, adiciona na lista de amigo de ambos
    public void adicionarAmigo(String id, String loginAmigo){
        Usuario user = usuarioLogado;
        Usuario userAmigo = usuarios.get(loginAmigo);
        if(userAmigo == null){
            throw new RuntimeException("Usu�rio n�o cadastrado.");
        }
        if(ehAmigo(user.getLogin(), userAmigo.getLogin())){
            throw new RuntimeException("Usu�rio j� � seu amigo.");
        }
        if(user.getConviteAmigos().contains(userAmigo) && !userAmigo.getConviteAmigos().contains(user)){
            throw new RuntimeException("Usu�rio j� est� adicionado como amigo, esperando aceita��o do convite.");
        }
        if(user.getAmigo().contains(userAmigo) && userAmigo.getAmigo().contains(user)){
            throw new RuntimeException("Usu�rio j� est� adicionado como amigo.");
        }
        user.getConviteAmigos().add(userAmigo);
        if(userAmigo.getConviteAmigos().contains(user)){
            user.getAmigo().add(userAmigo);
            userAmigo.getAmigo().add(user);
            user.getConviteAmigos().remove(userAmigo);
            userAmigo.getConviteAmigos().remove(user);
        }

    }
    //amigos adicionados do usuario do login no header
    public String getAmigos(String login){
        Usuario user = usuarios.get(login);
        StringBuilder amigos = new StringBuilder();
        amigos.append("{");
        for(Usuario amigo : user.getAmigo()){
            amigos.append(amigo.getLogin());
            if(user.getAmigo().indexOf(amigo) != user.getAmigo().size()-1){
                amigos.append(",");
            }
        }
        amigos.append("}");
        return amigos.toString();

    }
}
