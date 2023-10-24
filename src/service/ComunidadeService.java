package service;

import br.ufal.ic.p2.jackut.Exceptions.ComunidadeNaoEncontradaException;
import br.ufal.ic.p2.jackut.Exceptions.UserNotFoundException;
import br.ufal.ic.p2.jackut.Exceptions.ComunidadeJaExisteException;
import br.ufal.ic.p2.jackut.Exceptions.UsuarioJaEstaNaComunidadeException;
import br.ufal.ic.p2.jackut.SystemService;
import br.ufal.ic.p2.jackut.models.Comunidade;
import br.ufal.ic.p2.jackut.models.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ComunidadeService {
    private final SystemService system;
    public ComunidadeService(SystemService system) {
        this.system = system;
    }

    public void criarComunidade(String id, String nome, String descricao) throws UserNotFoundException {
        Usuario usuario = system.getSessoes().get(id);
        if (usuario == null) {
            throw new UserNotFoundException();
        }

        if(system.getComunidades().containsKey(nome)    ){
            throw new ComunidadeJaExisteException();
        }

        Comunidade comunidade = usuario.criarComunidade(nome, descricao);

        system.getComunidades().put(nome, comunidade); // Aqui seria pra dar um put em comunidades
    }

    public String getDescricaoComunidade(String nome) throws ComunidadeNaoEncontradaException {
        if(!system.getComunidades().containsKey(nome)){
            throw new ComunidadeNaoEncontradaException();
        }
        return system.getComunidades().get(nome).getDescricao();
    }

    public String getComunidades(String login) throws UserNotFoundException {
        Usuario usuario = system.getUsuarios().get(login);
        if (usuario == null) {
            throw new UserNotFoundException();
        }

        List<String> comunidadesString = new ArrayList<>();
        for (Comunidade comunidade : usuario.getComunidades().values()){
            comunidadesString.add(comunidade.getNome());
        }

        return "{" + String.join(",", comunidadesString) + "}";
    }

    public String getDonoComunidade(String nomeComunidade) throws ComunidadeNaoEncontradaException {
        if (!system.getComunidades().containsKey(nomeComunidade)) {
            throw new ComunidadeNaoEncontradaException();
        }
        return system.getComunidades().get(nomeComunidade).getDono().getLogin();
    }

    public String getMembrosComunidade(String nomeComunidade) throws ComunidadeNaoEncontradaException {
        //checa se comunidade existe
        if (! system.getComunidades().containsKey(nomeComunidade)) {
            throw new ComunidadeNaoEncontradaException();
        }
        //pega comunidade e os membros
        Comunidade comunidade =  system.getComunidades().get(nomeComunidade);
        List<Usuario> membros = comunidade.getMembros();

        //cria uma lista de logins dos membros
        List<String> membrosString = new ArrayList<>();
        for (Usuario user : membros) {
            membrosString.add(user.getLogin());
        }

        return "{" + String.join(",", membrosString) + "}"; //retorna os membros em uma string
    }

    public void adicionarComunidade(Usuario user, String nome) throws UserNotFoundException, ComunidadeNaoEncontradaException, UsuarioJaEstaNaComunidadeException {
        if (user == null) {
            throw new UserNotFoundException();
        }
        if (!system.getUsuarios().containsKey(user.getLogin())) {
            throw new UserNotFoundException();
        }
        if (!system.getComunidades().containsKey(nome)) {
            throw new ComunidadeNaoEncontradaException();
        }
        if (user.getComunidades().containsKey(nome)) {
            throw new UsuarioJaEstaNaComunidadeException();
        }
        //adiciona usuario na comunidade e comunidade no perfil do usuario
        system.getComunidades().get(nome).getMembros().add(user);
        Comunidade comunidade = system.getComunidades().get(nome);
        user.getComunidades().put(nome, comunidade);
    }
}
