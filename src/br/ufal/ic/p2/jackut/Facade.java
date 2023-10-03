package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Exceptions.*;
import br.ufal.ic.p2.jackut.models.*;
import br.ufal.ic.p2.jackut.utils.Formater;

import java.io.*;
import java.util.*;

public class Facade {
    public static final String USER_NOT_FOUND = "Usuário não cadastrado.";
    private Map<String, Usuario> usuarios;
    private Usuario usuarioLogado;

    private Map<String, Usuario> sessoes;

    //milestone 2
    private Map<String, Comunidade> comunidades;

    //ao iniciar o programa estabelece o arquivo txt de usuarios ou le o existente
    public Facade() {
        try {
            //verifica se ja arquivo ja existe. se nao, cria um novo
            if (!new File("usuarios.txt").exists()) {
                new File("usuarios.txt").createNewFile();
                this.usuarioLogado = null;
                this.usuarios = new HashMap<>();
                this.sessoes = new HashMap<>();
                //milestone 2///////////////////////
                this.comunidades = new HashMap<>();
            } else {
                this.usuarioLogado = null;
                this.usuarios = new HashMap<>();
                this.sessoes = new HashMap<>();
                //milestone 2////////////////////////
                this.comunidades = new HashMap<>();
                //buffered reader é melhor para ler arquivos grandes
                BufferedReader reader = new BufferedReader(new FileReader("usuarios.txt"));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] user = line.split(";", -1); //limit:-1 == se nao tiver nada, NAO IGNORA
                    //define o atributo como vazio se nao tiver nome
                    String item = "";

                    if (user[2] != null) {
                        item = user[2];
                    }
                    Usuario usuario = new Usuario(user[0], user[1], item);
                    //adiciona os atributos
                    if (usuario.getAtributos() != null) {
                        for (int i = 3; i < user.length; i += 2) {
                            usuario.getAtributos().add(new Atributo(user[i], user[i + 1]));
                        }
                    }
                    usuarios.put(user[0], usuario);
                }
                reader.close();
                //le o arquivo de comunidades
                BufferedReader reader2 = new BufferedReader(new FileReader("comunidades.txt"));
                String line2;
                while ((line2 = reader2.readLine()) != null) {
                    String[] comunidade = line2.split(";", -1);
                    String nome = comunidade[0];
                    String descricao = comunidade[1];
                    String dono = comunidade[2];
                    Comunidade novaComunidade = new Comunidade(nome, descricao, usuarios.get(dono));
                    //adiciona os membros
                    for (int i = 3; i < comunidade.length; i++) {
                        novaComunidade.getMembros().add(usuarios.get(comunidade[i]));
                    }
                    comunidades.put(nome, novaComunidade);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void criarUsuario(String login, String senha, String nome) {
        if (login == null || login.isEmpty()) {
            throw new LoginInvalidoException();
        }

        if (senha == null || senha.isEmpty()) {
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

    public void zerarSistema() {
        usuarios.clear();
        sessoes.clear();
        comunidades.clear();
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
            BufferedWriter writer2 = new BufferedWriter(new FileWriter("comunidades.txt"));
            for (Usuario user : usuarios.values()) {
                writer.write(user.getLogin() + ";" + user.getSenha() + ";" + user.getNome());
                //tambem grava os atributos
                for (Atributo atributo : user.getAtributos()) {
                    writer.write(";" + atributo.getNome() + ";" + atributo.getValor());
                }
                writer.newLine();
            }
            writer.close();
            for (Comunidade comunidade : comunidades.values()) {
                writer2.write(comunidade.getNome() + ";" + comunidade.getDescricao() + ";" + comunidade.getDono().getLogin());
                for (Usuario user : comunidade.getMembros()) {
                    writer2.write(";" + user.getLogin());
                }
                writer2.newLine();
            }
            //escreve os membros
            writer2.close();

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

    public Boolean ehAmigo(String login, String loginAmigo) {
        Usuario user = usuarios.get(login);
        Usuario userAmigo = usuarios.get(loginAmigo);
        return user.getAmigo().contains(userAmigo) && userAmigo.getAmigo().contains(user);
    }

    //send friend request, se o amigo ja tiver mandado um, adiciona na lista de amigo de ambos
    public void adicionarAmigo(String id, String loginAmigo) {
        //pega o usuario das sessoes com o seu id de sessao
        Usuario user = sessoes.get(id);
        if (user == null) {
            throw new RuntimeException(USER_NOT_FOUND);
        }
        abrirSessao(user.getLogin(), user.getSenha());
        Usuario userAmigo = usuarios.get(loginAmigo);
        if (userAmigo == null) {
            throw new RuntimeException(USER_NOT_FOUND);
        }
        if (ehAmigo(user.getLogin(), userAmigo.getLogin())) {
            throw new AmigoJaAdicionadoException();
        }
        if (user.getConviteAmigos().contains(userAmigo) && !userAmigo.getConviteAmigos().contains(user)) {
            throw new EsperandoConviteException();
        }
        if (user.getAmigo().contains(userAmigo) && userAmigo.getAmigo().contains(user)) {
            throw new AmigoJaAdicionadoException();
        }
        if (user.getLogin().equals(userAmigo.getLogin())) {
            throw new SelfRequestException();
        }
        //milestone2
        if (user.getInimigos().contains(userAmigo)) {
            throw new EnemyRequestException(userAmigo.getNome());
        }

        user.getConviteAmigos().add(userAmigo);
        //se o amigo ja tiver mandado um, adiciona na lista de amigo de ambos
        if (userAmigo.getConviteAmigos().contains(user)) {
            user.getAmigo().add(userAmigo);
            userAmigo.getAmigo().add(user);
            user.getConviteAmigos().remove(userAmigo);
            userAmigo.getConviteAmigos().remove(user);
        }

    }

    //amigos adicionados do usuario do login no header
    public String getAmigos(String login) {
        Usuario user = usuarios.get(login);
        StringBuilder amigos = new StringBuilder();
        amigos.append("{");
        for (Usuario amigo : user.getAmigo()) {
            amigos.append(amigo.getLogin());
            if (user.getAmigo().indexOf(amigo) != user.getAmigo().size() - 1) {
                amigos.append(",");
            }
        }
        amigos.append("}");
        return amigos.toString();

    }
    //esse vai ser o geral do sistema, o enviar recado isolado vai ser o de baixo  (eviarRecadoPaquera)
    public void enviarRecado(String id, String destinatario, String mensagem) throws UserNotFoundException {
        Usuario sender = sessoes.get(id);
        Usuario receiver = usuarios.get(destinatario);
        if (sender == null || receiver == null) {
            throw new UserNotFoundException();
        }

        if (sender.getLogin().equals(receiver.getLogin())) {
            throw new SelfRecadoException();
        }

        if(sender.getInimigos().contains(receiver)){
            throw new EnemyRequestException(receiver.getNome());
        }

        Recado recado = new Recado(sender, receiver, mensagem);
        receiver.getRecados().add(recado);


    }
    //esse eh pra ser enviado no sistema de adicionar paqueras
    public void enviarRecadoPaquera(Usuario remetente, Usuario destinatario, String mensagem) throws UserNotFoundException {


        if (remetente == null || destinatario == null) {
            throw new UserNotFoundException();
        }

        if (remetente.getLogin().equals(destinatario.getLogin())) {
            throw new SelfRecadoException();
        }
        Recado recado = new Recado(remetente, destinatario, mensagem);
        destinatario.getRecados().add(recado);


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
        if (comunidades.containsKey(nome)) {
            throw new ComunidadeJaExisteException();
        }
        usuario.criarComunidade(nome, descricao);

        Comunidade novaComunidade = usuario.getComunidades().get(nome);
        //de alguma forma ele ja adiciona o dono automaticamente
        comunidades.put(nome, novaComunidade);
    }

    public String getDescricaoComunidade(String nome) throws ComunidadeNaoEncontradaException {
        if (!comunidades.containsKey(nome)) {
            throw new ComunidadeNaoEncontradaException();
        }
        return comunidades.get(nome).getDescricao();
    }

    public String getDonoComunidade(String nomeComunidade) throws ComunidadeNaoEncontradaException {
        if (!comunidades.containsKey(nomeComunidade)) {
            throw new ComunidadeNaoEncontradaException();
        }
        return comunidades.get(nomeComunidade).getDono().getLogin();
    }

    public String getMembrosComunidade(String nomeComunidade) throws ComunidadeNaoEncontradaException {
        //checa se comunidade existe
        if (!comunidades.containsKey(nomeComunidade)) {
            throw new ComunidadeNaoEncontradaException();
        }
        //pega comunidade e os membros
        Comunidade comunidade = comunidades.get(nomeComunidade);
        List<Usuario> membros = comunidade.getMembros();

        //cria uma lista de logins dos membros
        List<String> membrosString = new ArrayList<>();
        for (Usuario user : membros) {
            membrosString.add(user.getLogin());
        }

        return "{" + String.join(",", membrosString) + "}"; //retorna os membros em uma string
    }

    //get Comunidades
    public String getComunidades(String login) {
        Usuario user = usuarios.get(login);
        if (user == null) {
            throw new UserNotFoundException();
        }

        List<String> comunidadesString = new ArrayList<>();
        for (Comunidade comunidade : user.getComunidades().values()) {
            comunidadesString.add(comunidade.getNome());
        }

        return "{" + String.join(",", comunidadesString) + "}";
    }

    //adicionar Comunidades, adiciona usuario na lista de membros da comunidade
    public void adicionarComunidade(String id, String nome) {
        Usuario user = sessoes.get(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        if (!usuarios.containsKey(user.getLogin())) {
            throw new UserNotFoundException();
        }
        if (!comunidades.containsKey(nome)) {
            throw new ComunidadeNaoEncontradaException();
        }
        if (user.getComunidades().containsKey(nome)) {
            throw new UsuarioJaEstaNaComunidadeException();
        }
        //adiciona usuario na comunidade e comunidade no perfil do usuario
        comunidades.get(nome).getMembros().add(user);
        Comunidade comunidade = comunidades.get(nome);
        user.getComunidades().put(nome, comunidade);
    }

    public String lerMensagem(String id) throws UserNotFoundException, NaoHaMensagensException {
        Usuario user = sessoes.get(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        if (!usuarios.containsKey(user.getLogin())) {
            throw new UserNotFoundException();
        }
        if (user.getMensagens().isEmpty()) {
            throw new NaoHaMensagensException();
        }
        Mensagem mensagem = user.getMensagens().poll();
        return mensagem.getMensagem();

    }

    public void enviarMensagem(String id, String comunidade, String mensagem) throws UserNotFoundException, ComunidadeNaoEncontradaException {
        Usuario user = sessoes.get(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        if (!usuarios.containsKey(user.getLogin())) {
            throw new UserNotFoundException();
        }
        Comunidade comunidadeEnvio = comunidades.get(comunidade);
        if (!comunidades.containsKey(comunidade)) {
            throw new ComunidadeNaoEncontradaException();
        }
        Mensagem message = new Mensagem(mensagem, user);

        comunidadeEnvio.enviarMensagem(message);
    }

    public boolean ehFa(String login, String idolo) throws UserNotFoundException {
        if (!this.usuarios.containsKey(login) || !this.usuarios.containsKey(idolo)) {
            throw new UserNotFoundException();
        }
        Usuario user = this.usuarios.get(login);
        Usuario idol = this.usuarios.get(idolo);
        if ((user == null) || (idol == null)) {
            throw new UserNotFoundException();
        }
        return idol.getFas().contains(user);
    }

    public void adicionarIdolo(String id, String idolo) throws UserNotFoundException, ExistentRelantionshipException, AutoRelationshipException, AlreadyEnemyException {
        if (!this.usuarios.containsKey(idolo)) {
            throw new UserNotFoundException();
        }
        ;
        Usuario user = this.sessoes.get(id);
        Usuario idol = this.usuarios.get(idolo);
        if (user == null || idol == null) {
            throw new UserNotFoundException();
        }
        if (user.equals(idol)) {
            throw new AutoRelationshipException("fã");
        }
        if (user.getIdolos().contains(idol)) {
            throw new ExistentRelantionshipException("ídolo");
        }
        if (user.getInimigos().contains(idol)) {
            throw new AlreadyEnemyException(idol.getNome());
        }

        user.setIdolo(idol);
        idol.setFa(user);

    }

    //retorna fans em formato de string json
    public String getFas(String login) throws UserNotFoundException {
        if (!this.usuarios.containsKey(login)) {
            throw new UserNotFoundException();
        }
        ;
        Usuario user = this.usuarios.get(login);
        if (user == null) {
            throw new UserNotFoundException();
        }
        //S
        StringBuilder fas = new StringBuilder();
        fas.append("{");
        //itera por todos os fans do usuario
        for (Usuario fa : user.getFas()) {
            fas.append(fa.getLogin());
            if (user.getFas().indexOf(fa) != user.getFas().size() - 1) {
                fas.append(",");
            }
        }
        fas.append("}");
        return fas.toString();
    }

    public boolean ehPaquera(String id, String paquera) throws UserNotFoundException, AutoRelationshipException, ExistentRelantionshipException, AlreadyEnemyException {
        Usuario user = this.sessoes.get(id);
        Usuario paquerado = this.usuarios.get(paquera);

        return user.getPaqueras().contains(paquerado);
    }

    public void adicionarPaquera(String id, String paquera) throws UserNotFoundException, AlreadyEnemyException, ExistentRelantionshipException {
        Usuario user = this.sessoes.get(id);
        Usuario paquerado = this.usuarios.get(paquera);
        if(user == null || paquerado == null){
            throw new UserNotFoundException();
        }

        if(user.equals(paquerado)){
            throw new AutoRelationshipException("paquera");
        }
        if(user.getPaqueras().contains(paquerado)){
            throw new ExistentRelantionshipException("paquera");
        }
        if (user.getInimigos().contains(paquerado)) {
            throw new AlreadyEnemyException(paquerado.getNome());
        }

        if (user.getPaquerasRecebidas().contains(paquerado) || paquerado.getPaquerasRecebidas().contains(user)) {
            this.enviarRecadoPaquera(user, paquerado, user.getNome() + " é seu paquera - Recado do Jackut.");
            this.enviarRecadoPaquera(paquerado, user, paquerado.getNome() + " é seu paquera - Recado do Jackut.");
        }
        //adiciona o parquerado a lista de paqueras do ususario
        user.getPaqueras().add(paquerado);
        //adiciona o usuario a lista de paqueras recebidas do paquerado
        paquerado.getPaquerasRecebidas().add(user);

        }

    public String getPaqueras(String id) throws UserNotFoundException {
        Usuario user = this.sessoes.get(id);
        if (user == null || !usuarios.containsKey(user.getLogin())) {
            throw new UserNotFoundException();
        }
        //itera por todos os paqueras do usuario
        StringBuilder paquerasJson = new StringBuilder();
        paquerasJson.append("{");
        for (Usuario paquera : user.getPaqueras()) {
            paquerasJson.append(paquera.getLogin());
            if (user.getPaqueras().indexOf(paquera) != user.getPaqueras().size() - 1) {
                paquerasJson.append(",");
            }
        }
        paquerasJson.append("}");
        return paquerasJson.toString();

    }

    public void adicionarInimigo(String id, String inimigo) throws UserNotFoundException, ExistentRelantionshipException, AutoRelationshipException {
        if (!this.usuarios.containsKey(inimigo)) {
            throw new UserNotFoundException();
        }
        Usuario user = this.sessoes.get(id);
        Usuario enemy = this.usuarios.get(inimigo);
        if (user == null || enemy == null) {
            throw new UserNotFoundException();
        }
        if (user.equals(enemy)) {
            throw new AutoRelationshipException("inimigo");
        }
        if (user.getInimigos().contains(enemy)) {
            throw new ExistentRelantionshipException("inimigo");
        }

        user.setInimigo(enemy);
        enemy.setInimigo(user);
    }

    public void removerUsuario(String id) {
        Usuario user = this.sessoes.get(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        this.usuarios.remove(user.getLogin());
        this.sessoes.remove(id);
        //remove membro da comunidade e verifica se eh dono, se for remove tambem
        for (Comunidade comunidade : this.comunidades.values()) {
            comunidade.getMembros().remove(user);
            if (comunidade.getDono().equals(user)) {
                this.comunidades.remove(comunidade.getNome());
                //remove os membros da comunidade tb
                for (Usuario usuario : comunidade.getMembros()) {
                    usuario.getComunidades().remove(comunidade.getNome());
                }
            }
        }

        //a comuidade do ususario
        user.getComunidades().clear();
        //relacionamentos e mensagens enviadas
        for (Usuario usuario : this.usuarios.values()) {
            usuario.getAmigo().remove(user);
            usuario.getConviteAmigos().remove(user);
            usuario.getPaqueras().remove(user);
            usuario.getPaquerasRecebidas().remove(user);
            usuario.getInimigos().remove(user);
            usuario.getFas().remove(user);
            usuario.getIdolos().remove(user);
            ;
            //itera pelas mensagens
            usuario.getMensagens().removeIf(mensagem -> mensagem.getRemetente().equals(user));
            //itera pelos recados
            usuario.getRecados().removeIf(recado -> recado.getRemetente().equals(user));

        }
    }
}


