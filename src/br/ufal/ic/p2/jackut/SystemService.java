package br.ufal.ic.p2.jackut;
import br.ufal.ic.p2.jackut.Exceptions.*;
import br.ufal.ic.p2.jackut.models.*;

import java.io.*;
import java.util.*;

import service.RecadoService;

public class SystemService {

    public Map<String, Usuario> getUsuarios() {
        return usuarios;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    private final RecadoService recadoService = new RecadoService(this);

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

    public SystemService() {
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
                //buffered reader ? melhor para ler arquivos grandes
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
                    // Adiciona o dono ? comunidade se ele n?o estiver na lista de membros
                    if (!novaComunidade.getMembros().contains(usuarios.get(dono))) {
                        novaComunidade.getMembros().add(usuarios.get(dono));
                    }

                    for (int i = 3; i < comunidade.length; i++) {
                        Usuario membro = usuarios.get(comunidade[i]);
                        // Adicione membros ? comunidade se eles n?o estiverem na lista de membros
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

    public String abrirSessao(String login, String senha) throws LoginOuSenhaInvalidoException {
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
                    writer4.write(recado.getRemetente().getLogin() + ";" + recado.getDestinatario().getLogin() + ";" + recado.getMensagem());
                    writer4.newLine();
                }
            }
            writer4.close();
            //salva as mensagens em apenas um arquivo de mensagens
            BufferedWriter writer5 = new BufferedWriter(new FileWriter("mensagens.txt"));
            for (Usuario user : usuarios.values()) {
                Queue<Mensagem> mensagens = user.getMensagens();

                // Collections.reverse(mensagens);
                for (Mensagem mensagem : mensagens) {
                    writer5.write(mensagem.getRemetente().getLogin() + ";" + mensagem.getMensagem());
                    writer5.newLine();
                    System.out.println(mensagem.getMensagem());
                }
            }
            writer5.close();


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


    //abrir sessao
    public String login(String login, String senha) throws LoginOuSenhaInvalidoException {
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

}
