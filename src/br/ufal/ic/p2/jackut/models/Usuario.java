package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.Exceptions.NaoHaMensagensException;
import br.ufal.ic.p2.jackut.utils.Formater;

import java.util.*;


public class Usuario {
    private final String login;
    private final String senha;
    private final String nome;

    private final Queue<Recado> recados = new LinkedList<>();

    //exemplo: nome: "nome", valor: "joao"
    private final List<Atributo> atributos = new ArrayList<>();

    //amigos no qual enviou friendRequest
    private final List<Usuario> conviteAmigos = new ArrayList<>();

    //amigos da lista de amigos que ACEITARAM o friendRequest
    private final List<Usuario> amigo = new ArrayList<>();
    //milestone2
    private final Map<String, Comunidade> comunidades = new LinkedHashMap<>(); //deixei como linked pq por algum motivo o teste qria q fosse em ordem
    private final Queue<Mensagem> mensagens = new LinkedList<>();
    private final ArrayList<Usuario> idolos = new ArrayList<>();
    private final ArrayList<Usuario> fas = new ArrayList<>();
    private final ArrayList<Usuario> paqueras = new ArrayList<>();
    private final ArrayList<Usuario> paquerasRecebidas = new ArrayList<>();
    private final ArrayList<Usuario> inimigos = new ArrayList<>();

    public ArrayList<Mensagem> getMensagensRecebidas() {
        return mensagensRecebidas;
    }

    private final ArrayList<Mensagem> mensagensRecebidas = new ArrayList<>();


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

    //////////////////milestone 2////////////////////////////////////////////////////////
    public Comunidade criarComunidade(String nome, String descricao) {
        Comunidade comunidade = new Comunidade(nome, descricao, this);
        this.comunidades.put(nome, comunidade);
        return comunidade;
    }

    public Map<String, Comunidade> getComunidades() {
        return comunidades;
    }

    public void receberMensagem(Mensagem mensagem) {
        this.mensagens.add(mensagem);
    }


    public Mensagem lerMensagem() throws NaoHaMensagensException{
        if(this.mensagens.isEmpty()){
            throw new NaoHaMensagensException();
        }

        return this.mensagens.poll();
    }

    public Queue<Mensagem> getMensagens() {
        return this.mensagens;
    }

    public void setIdolo(Usuario usuario) {
        this.idolos.add(usuario);
    }

    public void setFa(Usuario usuario) {
        this.fas.add(usuario);
    }

    public ArrayList<Usuario> getIdolos() {
        return this.idolos;
    }

    public ArrayList<Usuario> getFas() {
        return this.fas;
    }


    public void setPaquera(Usuario usuario) {
        this.paqueras.add(usuario);
    }

    public void setPaquerasRecebidas(Usuario usuario) {
        this.paquerasRecebidas.add(usuario);
    }

    public ArrayList<Usuario> getPaqueras() {
        return this.paqueras;
    }

    public ArrayList<Usuario> getPaquerasRecebidas() {
        return this.paquerasRecebidas;
    }

    public String getPaquerasString() {
        return Formater.format(this.paqueras);
    }

    public void setInimigo(Usuario usuario) {
        this.inimigos.add(usuario);
    }

    public ArrayList<Usuario> getInimigos() {
        return this.inimigos;
    }

    public Mensagem LerMensagem() throws NaoHaMensagensException {
        if (this.mensagens.isEmpty()) {
            throw new NaoHaMensagensException();
        }
        return this.mensagens.poll();
    }
}