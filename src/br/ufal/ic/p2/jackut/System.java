package br.ufal.ic.p2.jackut;
import br.ufal.ic.p2.jackut.Exceptions.*;
import br.ufal.ic.p2.jackut.models.*;

import java.io.*;
import java.util.*;


import static br.ufal.ic.p2.jackut.Facade.USER_NOT_FOUND;

public class System {
    public Map<String, Usuario> getUsuarios() {
        return usuarios;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public Map<String, Usuario> getSessoes() {
        return sessoes;
    }

    public Map<String, Comunidade> getComunidades() {
        return comunidades;
    }

    private Map<String, Usuario> usuarios;
    private Usuario usuarioLogado;

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    private Map<String, Usuario> sessoes;

    //milestone 2
    private Map<String, Comunidade> comunidades;
    public System() {
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
                //buffered reader � melhor para ler arquivos grandes
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
                //le o arquivo de comunidades porem de forma melhorada e atualizada
                BufferedReader reader2 = new BufferedReader(new FileReader("comunidades.txt"));
                String line2;
                while ((line2 = reader2.readLine()) != null) {
                    String[] comunidade = line2.split(";", -1);
                    String nome = comunidade[0];
                    String descricao = comunidade[1];
                    String dono = comunidade[2];
                    Comunidade novaComunidade = new Comunidade(nome, descricao, usuarios.get(dono));
                    // Adiciona o dono � comunidade se ele n�o estiver na lista de membros
                    if (!novaComunidade.getMembros().contains(usuarios.get(dono))) {
                        novaComunidade.getMembros().add(usuarios.get(dono));
                    }

                    for (int i = 3; i < comunidade.length; i++) {
                        Usuario membro = usuarios.get(comunidade[i]);
                        // Adicione membros � comunidade se eles n�o estiverem na lista de membros
                        if (!novaComunidade.getMembros().contains(membro)) {
                            novaComunidade.getMembros().add(membro);
                        }
                    }
                    comunidades.put(nome, novaComunidade);
                }
                //insere comunidades nos usuarios de tras pra frente
                for (Comunidade comunidade : comunidades.values()) {
                    for (Usuario membro : comunidade.getMembros()) {
                        membro.getComunidades().put(comunidade.getNome(), comunidade);
                    }
                }

                //le o arquivo de amigos
                BufferedReader reader3 = new BufferedReader(new FileReader("amigos.txt"));
                String line3;
                while ((line3 = reader3.readLine()) != null) {
                    String[] amigos = line3.split(";", -1);
                    String login = amigos[0];
                    for (int i = 1; i < amigos.length; i++) {
                        usuarios.get(login).getAmigo().add(usuarios.get(amigos[i]));
                    }
                }
                reader3.close();
                //le o arquivo de recados
                BufferedReader reader4 = new BufferedReader(new FileReader("recados.txt"));
                String line4;
                while ((line4 = reader4.readLine()) != null) {
                    String[] recados = line4.split(";", -1);
                    String remetente = recados[0];
                    String destinatario = recados[1];
                    String mensagem = recados[2];
                    Recado recado = new Recado(usuarios.get(remetente), usuarios.get(destinatario), mensagem);
                    usuarios.get(destinatario).getRecados().add(recado);
                }
                reader4.close();
                //le o arquivo de mensagens
                BufferedReader reader5 = new BufferedReader(new FileReader("mensagens.txt"));
                String line5;
                while ((line5 = reader5.readLine()) != null) {
                    String[] mensagens = line5.split(";");
                    String remetente = mensagens[0];
                    String mensagem = mensagens[1];
                    Mensagem message = new Mensagem(mensagem, usuarios.get(remetente));
                    usuarios.get(remetente).getMensagens().add(message);
                }
                reader5.close();



            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    public void encerrarSistema() {
        usuarioLogado = null;
        sessoes.clear();
        //escreve no arquivo de usuarios
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("usuarios.txt"));
            for (Usuario user : usuarios.values()) {
                writer.write(user.getLogin() + ";" + user.getSenha() + ";" + user.getNome());
                //tambem grava os atributos
                for (Atributo atributo : user.getAtributos()) {
                    writer.write(";" + atributo.getNome() + ";" + atributo.getValor());
                }
                writer.newLine();
            }
            writer.close();
            //escreve no arquivo de comunidades
            BufferedWriter writer2 = new BufferedWriter(new FileWriter("comunidades.txt"));
            for (Comunidade comunidade : comunidades.values()) {
                writer2.write(comunidade.getNome() + ";" + comunidade.getDescricao() + ";" + comunidade.getDono().getLogin());
                //esta salvando o dono duas vezes
                for (Usuario membro : comunidade.getMembros()) {
                    writer2.write(";" + membro.getLogin());
                }
                writer2.newLine();
            }
            writer2.close();

            BufferedWriter writer3 = new BufferedWriter(new FileWriter("amigos.txt"));

            for (Usuario user : usuarios.values()) {
                writer3.write(user.getLogin());
                for (Usuario amigo : user.getAmigo()) {
                    writer3.write(";" + amigo.getLogin());
                }
                writer3.newLine();
            }
            writer3.close();
            //salva os recados
            //salva os recados em apenas um arquivo de recados
            BufferedWriter writer4 = new BufferedWriter(new FileWriter("recados.txt"));
            for (Usuario user : usuarios.values()) {
                for (Recado recado : user.getRecados()) {
                    writer4.write(recado.getRemetente().getLogin() + ";" + recado.getDestinatario().getLogin() + ";" + recado.getRecado());
                    writer4.newLine();
                }
            }
            writer4.close();
            //salva as mensagens em apenas um arquivo de mensagens
            BufferedWriter writer5 = new BufferedWriter(new FileWriter("mensagens.txt"));
            for (Usuario user : usuarios.values()) {
                for (Mensagem mensagem : user.getMensagens()) {
                    writer5.write(mensagem.getRemetente().getLogin() + ";" + mensagem.getMensagem());
                    writer5.newLine();
                }
            }
            //mensagens recebidas



        } catch (IOException e) {
            e.printStackTrace();
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

    public String getUser(String login, Usuario user, String atributo) throws UserNotFoundException{
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

    public void addUser(Usuario user) throws ContaJaExisteException, LoginInvalidoException, SenhaInvalidaException {
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new LoginInvalidoException();
        }

        if (user.getSenha() == null || user.getSenha().isEmpty()) {
            throw new SenhaInvalidaException();
        }

        if (!usuarios.containsKey(user.getLogin())) {
            usuarios.put(user.getLogin(), user);
        } else {
            throw new ContaJaExisteException();
        }
        user.addAtributo("nome", user.getNome());
    }
    //abrir sessao
    public String login(String login, String senha) throws LoginOuSenhaInvalidoException{
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
    public void editarPerfil(String id, String atributo, String valor) throws UserNotFoundException{
        Usuario usuario = sessoes.get(id);
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
    public void adicionarAmigo(String id, String loginAmigo) throws UserNotFoundException, AmigoJaAdicionadoException, EsperandoConviteException, SelfRequestException, EnemyRequestException{
        //pega o usuario das sessoes com o seu id de sessao
        Usuario user = sessoes.get(id);
        if (user == null) {
            throw new RuntimeException(USER_NOT_FOUND);
        }
        login(user.getLogin(), user.getSenha());
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

    public String getComunidades(String login) throws UserNotFoundException {
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

    public void adicionarComunidade(Usuario user, String nome) throws UserNotFoundException, ComunidadeNaoEncontradaException, UsuarioJaEstaNaComunidadeException {
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

    public void adicionarIdolo(Usuario user, Usuario idol) throws UserNotFoundException, ExistentRelantionshipException, AutoRelationshipException, AlreadyEnemyException {
        if(!usuarios.containsValue(idol)){
            throw new UserNotFoundException();
        }
        if (user.equals(idol)) {
            throw new AutoRelationshipException("f�");
        }
        if (user.getIdolos().contains(idol)) {
            throw new ExistentRelantionshipException("�dolo");
        }
        if (user.getInimigos().contains(idol)) {
            throw new AlreadyEnemyException(idol.getNome());
        }


        user.setIdolo(idol);
        idol.setFa(user);

    }
    //retorna fans em formato de string json
    public String getFas(Usuario user) throws UserNotFoundException {
        if (!this.usuarios.containsKey(user.getLogin())) {
            throw new UserNotFoundException();
        }

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

    public boolean ehPaquera(Usuario user, Usuario paquerado) {

        return user.getPaqueras().contains(paquerado);
    }

    public void adicionarPaquera(Usuario user, Usuario paquerado) throws UserNotFoundException, AlreadyEnemyException, ExistentRelantionshipException {

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
            this.enviarRecadoPaquera(user, paquerado, user.getNome() + " � seu paquera - Recado do Jackut.");
            this.enviarRecadoPaquera(paquerado, user, paquerado.getNome() + " � seu paquera - Recado do Jackut.");
        }
        //adiciona o parquerado a lista de paqueras do ususario
        user.getPaqueras().add(paquerado);
        //adiciona o usuario a lista de paqueras recebidas do paquerado
        paquerado.getPaquerasRecebidas().add(user);

    }

    public String getPaqueras(Usuario user) throws UserNotFoundException {
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


    public void adicionarInimigo(Usuario user, Usuario enemy) throws ExistentRelantionshipException, AutoRelationshipException, UserNotFoundException {

        if (user.equals(enemy)) {
            throw new AutoRelationshipException("inimigo");
        }
        if (user.getInimigos().contains(enemy)) {
            throw new ExistentRelantionshipException("inimigo");
        }
        if(enemy==null){
            throw new UserNotFoundException();
        }

        user.setInimigo(enemy);
        enemy.setInimigo(user);
    }

    public void removerUsuario(Usuario user, String id) {
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

    public String getAtributoUsuario(String login, String atributo) throws UserNotFoundException, AtributoNaoPreenchidoException{
        Usuario user = usuarios.get(login);
        if (user == null) {
            throw new UserNotFoundException();
        }
        if(user.getAtributo(atributo) == null){
            throw new AtributoNaoPreenchidoException();
        }
        return user.getAtributo(atributo).getValor();
    }
}
