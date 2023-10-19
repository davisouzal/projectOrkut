package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Exceptions.*;
import br.ufal.ic.p2.jackut.models.*;
import br.ufal.ic.p2.jackut.utils.Formater;

import java.io.*;
import java.util.*;

public class Facade {
    private final System system = new System();
    public static final String USER_NOT_FOUND = "Usuário não cadastrado.";


    //ao iniciar o programa estabelece o arquivo txt de usuarios ou le o existente

    public void criarUsuario(String login, String senha, String nome) throws ContaJaExisteException, LoginInvalidoException, SenhaInvalidaException  {
        Usuario usuario = new Usuario(login, senha, nome);
        system.addUser(usuario);
    }

    public String getAtributoUsuario(String login, String atributo) throws UserNotFoundException, AtributoNaoPreenchidoException{
        return system.getAtributoUsuario(login, atributo);
    }

    public void zerarSistema() {
        system.zerarSistema();
    }


    public void encerrarSistema() {
        system.encerrarSistema();
    }

    public String abrirSessao(String login, String senha) throws LoginOuSenhaInvalidoException{
        return system.abrirSessao(login, senha);
    }

    public void editarPerfil(String id, String atributo, String valor)  throws UserNotFoundException{
        system.editarPerfil(id, atributo, valor);
    }

    public Boolean ehAmigo(String login, String loginAmigo) {
        return system.ehAmigo(login, loginAmigo);
    }

    //send friend request, se o amigo ja tiver mandado um, adiciona na lista de amigo de ambos
    public void adicionarAmigo(String id, String loginAmigo) {
        system.adicionarAmigo(id, loginAmigo);
    }

    //amigos adicionados do usuario do login no header
    public String getAmigos(String login) {
        return system.getAmigos(login);
    }
    //esse vai ser o geral do sistema, o enviar recado isolado vai ser o de baixo  (eviarRecadoPaquera)
    public void enviarRecado(String id, String destinatario, String mensagem) throws UserNotFoundException {
        system.enviarRecado(id, destinatario, mensagem);
    }
    //esse eh pra ser enviado no sistema de adicionar paqueras
    public void enviarRecadoPaquera(Usuario remetente, Usuario destinatario, String mensagem) throws UserNotFoundException {
        system.enviarRecadoPaquera(remetente, destinatario, mensagem);
    }

    public String lerRecado(String id) throws UserNotFoundException, NaoHaRecadosException {
        return system.lerRecado(id);
    }

    //////////////////milestone 2////////////////////////////////////////////////////////
    public void criarComunidade(String id, String nome, String descricao) throws UserNotFoundException {
        system.criarComunidade(id, nome, descricao);
    }

    public String getDescricaoComunidade(String nome) throws ComunidadeNaoEncontradaException {
        return system.getDescricaoComunidade(nome);
    }

    public String getDonoComunidade(String nomeComunidade) throws ComunidadeNaoEncontradaException {
        return system.getDonoComunidade(nomeComunidade);
    }

    public String getMembrosComunidade(String nomeComunidade) throws ComunidadeNaoEncontradaException {
        return system.getMembrosComunidade(nomeComunidade);

    }

    //get Comunidades
    public String getComunidades(String login) throws UserNotFoundException {
        return system.getComunidades(login);
    }

    //adicionar Comunidades, adiciona usuario na lista de membros da comunidade
    public void adicionarComunidade(String id, String nomeComunidade) throws UserNotFoundException, ComunidadeNaoEncontradaException, UsuarioJaEstaNaComunidadeException {
        Usuario user = system.getSessoes().get(id);
        system.adicionarComunidade(user, nomeComunidade);
    }

    public String lerMensagem(String id) throws UserNotFoundException, NaoHaMensagensException {
        Usuario user = system.getSessoes().get(id);
        return system.lerMensagem(user);
    }

    public void enviarMensagem(String id, String comunidade, String mensagem) throws UserNotFoundException, ComunidadeNaoEncontradaException {
        Usuario user = system.getSessoes().get(id);
        system.enviarMensagem(user, comunidade, mensagem);
    }

    public boolean ehFa(String login, String idolo) throws UserNotFoundException {
        return system.ehFa(login, idolo);
    }

    public void adicionarIdolo(String id, String idolo) throws UserNotFoundException, ExistentRelantionshipException, AutoRelationshipException, AlreadyEnemyException {
        Usuario user = system.getSessoes().get(id);
        Usuario idol = system.getUsuarios().get(idolo);
        system.adicionarIdolo(user, idol);
    }


    public String getFas(String login) throws UserNotFoundException {
        Usuario user = system.getUsuarios().get(login);
        return system.getFas(user);
    }

    public boolean ehPaquera(String id, String paquera){
        Usuario user = system.getSessoes().get(id);
        Usuario paquerado = system.getUsuarios().get(paquera);

        return user.getPaqueras().contains(paquerado);
    }

    public void adicionarPaquera(String id, String paquera) throws UserNotFoundException, AlreadyEnemyException, ExistentRelantionshipException {
        Usuario user = system.getSessoes().get(id);
        Usuario paquerado = system.getUsuarios().get(paquera);
        system.adicionarPaquera(user, paquerado);
    }

    public String getPaqueras(String id) throws UserNotFoundException {
        Usuario user = system.getSessoes().get(id);
        return system.getPaqueras(user);
    }

    public void adicionarInimigo(String id, String inimigo) throws UserNotFoundException, ExistentRelantionshipException, AutoRelationshipException {

        Usuario user = system.getSessoes().get(id);
        Usuario enemy = system.getUsuarios().get(inimigo);

        system.adicionarInimigo(user, enemy);
    }

    public void removerUsuario(String id) {
        Usuario user = system.getSessoes().get(id);
        system.removerUsuario(user, id);

    }
}


