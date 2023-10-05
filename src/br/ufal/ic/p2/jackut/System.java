package br.ufal.ic.p2.jackut;
import br.ufal.ic.p2.jackut.Exceptions.AtributoNaoPreenchidoException;
import br.ufal.ic.p2.jackut.Exceptions.UserNotFoundException;
import br.ufal.ic.p2.jackut.models.Atributo;
import br.ufal.ic.p2.jackut.models.Comunidade;
import br.ufal.ic.p2.jackut.models.Usuario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class System {
    private Map<String, Usuario> usuarios;
    private Usuario usuarioLogado;

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
}
