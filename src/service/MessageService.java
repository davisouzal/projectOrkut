package service;

import br.ufal.ic.p2.jackut.Exceptions.ComunidadeNaoEncontradaException;
import br.ufal.ic.p2.jackut.Exceptions.NaoHaMensagensException;
import br.ufal.ic.p2.jackut.Exceptions.UserNotFoundException;
import br.ufal.ic.p2.jackut.System;
import br.ufal.ic.p2.jackut.models.Comunidade;
import br.ufal.ic.p2.jackut.models.Usuario;
import br.ufal.ic.p2.jackut.models.Mensagem;

import java.util.Map;

public class MessageService {
    private Map<String, Usuario> usuarios;
    private Map<String, Comunidade> comunidades;

    private final System system;

    public MessageService(Map<String, Usuario> usuarios, Map<String, Comunidade> comunidades, System system) {
        this.usuarios = usuarios;
        this.comunidades = comunidades;
        this.system = system;
    }

    public void enviarMensagem(String usuarioId, String comunidadeNome, String mensagem) throws UserNotFoundException, ComunidadeNaoEncontradaException {
        Usuario usuario = system.getSessoes().get(usuarioId);
        Comunidade comunidade = system.getComunidades().get(comunidadeNome);

        if (usuario == null) {
            throw new UserNotFoundException();
        }

        if (comunidade == null) {
            throw new ComunidadeNaoEncontradaException();
        }

        Mensagem message = new Mensagem(mensagem, usuario);
        comunidade.enviarMensagem(message);
    }

    public String lerMensagem(String usuarioId) throws UserNotFoundException {
        Usuario usuario = system.getSessoes().get(usuarioId);

        if (usuario == null) {
            throw new UserNotFoundException();
        }

        if (usuario.getMensagens().isEmpty()) {
            throw new NaoHaMensagensException();
        }

        Mensagem mensagem = usuario.getMensagens().poll();
        assert mensagem != null;
        return mensagem.getMensagem();
    }

    public System getSystem() {
        return system;
    }
}