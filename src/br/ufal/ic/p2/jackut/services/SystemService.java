package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.Exceptions.*;
import br.ufal.ic.p2.jackut.Facade;
import br.ufal.ic.p2.jackut.models.Comunidade;
import br.ufal.ic.p2.jackut.models.Usuario;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SystemService {

    private Usuario usuarioLogado = null;
    private Map<String, Usuario> usuarios = new HashMap<>();
    private Map<String, Usuario> sessoes = new HashMap<>();
    private Map<String, Comunidade> comunidades  = new HashMap<>();

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public Map<String, Usuario> getUsuarios() {
        return usuarios;
    }

    public Map<String, Usuario> getSessoes() {
        return sessoes;
    }

    public Map<String, Comunidade> getComunidades() {
        return comunidades;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public SystemService(){

    }

    public void addUsuario(Usuario usuario) throws ContaJaExisteException, SenhaInvalidaException, LoginInvalidoException{
        String login = usuario.getLogin();
        if (usuarios.containsKey(login)) {
            throw new ContaJaExisteException();
        }

        if(login == null || login.isEmpty()) throw new LoginInvalidoException();
        if(usuario.getSenha() == null || usuario.getSenha().isEmpty()) throw new SenhaInvalidaException();

        this.usuarios.put(usuario.getNome(), usuario);
    }

    public String abrirSessao(String login, String senha) throws LoginOuSenhaInvalidoException{
        if (login == null || senha == null) {
            throw new LoginOuSenhaInvalidoException();
        }
        Usuario usuario = usuarios.get(login);
        if (usuario == null || !usuario.validarSenha(senha)) {
            throw new LoginOuSenhaInvalidoException();
        }
        this.usuarioLogado = usuario;

        //cria um id para o usuario logado e retorna o id

        String id = UUID.randomUUID().toString();
        this.sessoes.put(id, usuario);
        return id;
    }

    public String getAtributoUsuario(String login, String atributo) throws AtributoNaoPreenchidoException, UserNotFoundException{
        Usuario user = usuarios.get(login);

        if (usuarios.containsKey(login)) {
            if (atributo.equals("nome")) {
                return usuarios.get(login).getNome();
            }
            if (user.getAtributo(atributo) != null) {
                return user.getAtributo(atributo).getValor();
            } else {
                throw new AtributoNaoPreenchidoException();
            }
        } else {
            throw new UserNotFoundException();
        }
    }

    public void zerarSistema(){
        this.usuarioLogado = null;
        this.usuarios.clear();
        this.sessoes.clear();
        this.comunidades.clear();

        new File("src/br/ufal/ic/p2/jackut/data/usuarios.txt").delete();
    }

    public void editarPerfil(String id, String atributo, String valor){
        if(usuarioLogado == null) {
            throw new UserNotFoundException();
        }
        else{
            usuarios.get(usuarioLogado.getLogin()).addAtributo(atributo, valor);
        }
    }


}
