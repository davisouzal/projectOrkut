package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Exceptions.*;
import br.ufal.ic.p2.jackut.models.Atributo;
import br.ufal.ic.p2.jackut.models.Comunidade;
import br.ufal.ic.p2.jackut.models.Usuario;
import br.ufal.ic.p2.jackut.models.Recado;

import java.io.*;
import java.util.*;

public class Facade {
    public static final String USER_NOT_FOUND= "Usuário não cadastrado.";
    private Map<String, Usuario> usuarios;
    private Usuario usuarioLogado;

    private Map<String, Usuario> sessoes;

    //milestone 2
    private Map<String, Comunidade> comunidades;

    //ao iniciar o programa estabelece o arquivo txt de usuarios ou le o existente
    public Facade(){
        try{
            //verifica se ja arquivo ja existe. se nao, cria um novo
            if(!new File("usuarios.txt").exists()){
                new File("usuarios.txt").createNewFile();
                this.usuarioLogado = null;
                this.usuarios = new HashMap<>();
                this.sessoes = new HashMap<>();
                //milestone 2///////////////////////
                this.comunidades = new HashMap<>();
            }
            else{
                this.usuarioLogado = null;
                this.usuarios = new HashMap<>();
                this.sessoes = new HashMap<>();
                //milestone 2////////////////////////
                this.comunidades = new HashMap<>();
                //buffered reader é melhor para ler arquivos grandes
                BufferedReader reader = new BufferedReader(new FileReader("usuarios.txt"));
                String line;    
                while((line = reader.readLine()) != null){
                    String[] user = line.split(";", -1); //limit:-1 == se nao tiver nada, NAO IGNORA
                    //define o atributo como vazio se nao tiver nome
                    String item = "";

                    if(user[2]!= null){
                        item = user[2];
                    }
                    Usuario usuario = new Usuario(user[0], user[1], item);
                    //adiciona os atributos
                    if(usuario.getAtributos()!=null){
                        for (int i = 3; i < user.length; i += 2) {
                            usuario.getAtributos().add(new Atributo(user[i], user[i + 1]));
                        }
                    }
                    usuarios.put(user[0], usuario);
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void criarUsuario(String login, String senha, String nome) {
        if (login == null || login.isEmpty()) {
            throw new LoginInvalidoException();
        }

        if( senha == null || senha.isEmpty()) {
            throw new SenhaInvalidaException();
        }

        if (!usuarios.containsKey(login)) {
            Usuario novoUsuario = new Usuario(login, senha, nome);
            usuarios.put(login, novoUsuario);
        } else {
            throw new ContaJaExisteException();
        }
    }

    public String abrirSessao(String login, String senha) {
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
                throw new AtributoNaoPreenchidoException();
            }
        } else {
            throw new UserNotFoundException();
        }
    }

    public void zerarSistema() {
        usuarios.clear();
        sessoes.clear();
        usuarioLogado = null;
        //deleta o arquivo de usuarios
        new File("usuarios.txt").delete();

    }


    public void encerrarSistema() {
        usuarioLogado = null;
        sessoes.clear();
        //escreve no arquivo de usuarios
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("usuarios.txt"));
            for(Usuario user : usuarios.values()){
                writer.write(user.getLogin()+";"+user.getSenha()+";"+user.getNome());
                //tambem grava os atributos
                for(Atributo atributo : user.getAtributos()){
                    writer.write(";" + atributo.getNome()+";"+atributo.getValor());
                }
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editarPerfil(String id, String atributo, String valor) {

        if (usuarioLogado == null) {
            throw new UserNotFoundException();
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
        //pega o usuario das sessoes com o seu id de sessao
        Usuario user = sessoes.get(id);
        if (user == null ){
            throw new RuntimeException(USER_NOT_FOUND);
        }
        abrirSessao(user.getLogin(), user.getSenha());
        Usuario userAmigo = usuarios.get(loginAmigo);
        if(userAmigo == null){
            throw new RuntimeException(USER_NOT_FOUND);
        }
        if(ehAmigo(user.getLogin(), userAmigo.getLogin())){
            throw new AmigoJaAdicionadoException();
        }
        if(user.getConviteAmigos().contains(userAmigo) && !userAmigo.getConviteAmigos().contains(user)){
            throw new EsperandoConviteException();
        }
        if(user.getAmigo().contains(userAmigo) && userAmigo.getAmigo().contains(user)){
            throw new AmigoJaAdicionadoException();
        }
        if(user.getLogin().equals(userAmigo.getLogin())){
            throw new SelfRequestException();
        }
        user.getConviteAmigos().add(userAmigo);
        //se o amigo ja tiver mandado um, adiciona na lista de amigo de ambos
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

    public void enviarRecado(String id, String destinatario, String mensagem) throws UserNotFoundException {
        Usuario sender = sessoes.get(id);
        Usuario reciever = usuarios.get(destinatario);
        if(sender == null || reciever == null){
            throw new UserNotFoundException();
        }

        if (sender.getLogin().equals(reciever.getLogin())){
            throw new SelfRecadoException();
        }

        sender.enviarRecado(reciever, mensagem);
    }
    public String lerRecado(String id) throws UserNotFoundException, NaoHaRecadosException {
        Usuario usuario = sessoes.get(id);

        if (usuario == null) {
            throw new UserNotFoundException();
        }

        Recado recado = usuario.getRecado();

        if (recado == null) {
            throw new NaoHaRecadosException();
        }

        return recado.getRecado();
    }

    //////////////////milestone 2////////////////////////////////////////////////////////
    public void criarComunidade(String id, String nome, String descricao) throws UserNotFoundException {
        Usuario usuario = sessoes.get(id);
        if (usuario == null) {
            throw new UserNotFoundException();
        }
        if(comunidades.containsKey(nome)){
            throw new ComunidadeJaExisteException();
        }
        usuario.criarComunidade(nome, descricao);
        Comunidade novaComunidade = usuario.getComunidades().get(nome);
        comunidades.put(nome, novaComunidade);
    }

    public String getDescricaoComunidade(String nome) throws ComunidadeNaoEncontradaException {
        if(!comunidades.containsKey(nome)){
            throw new ComunidadeNaoEncontradaException();
        }
        return comunidades.get(nome).getDescricao();
    }

    public String getDonoComunidade(String nomeComunidade) throws ComunidadeNaoEncontradaException{
        if(!comunidades.containsKey(nomeComunidade)){
            throw new ComunidadeNaoEncontradaException();
        }
        return comunidades.get(nomeComunidade).getDono().getLogin();
    }

    public List<Usuario> getMembrosComunidade(String nomeComunidade) throws ComunidadeNaoEncontradaException{
        if(!comunidades.containsKey(nomeComunidade)){
            throw new ComunidadeNaoEncontradaException();
        }
        return comunidades.get(nomeComunidade).getMembros();
    }
}
