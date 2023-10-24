package service;

import br.ufal.ic.p2.jackut.Exceptions.ComunidadeNaoEncontradaException;
import br.ufal.ic.p2.jackut.Exceptions.NaoHaMensagensException;
import br.ufal.ic.p2.jackut.Exceptions.UserNotFoundException;
import br.ufal.ic.p2.jackut.SystemService;
import br.ufal.ic.p2.jackut.models.Comunidade;
import br.ufal.ic.p2.jackut.models.Usuario;
import br.ufal.ic.p2.jackut.models.Mensagem;

public class MessageService {

    private final SystemService system;

    public MessageService(SystemService system) {
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
        usuario.getMensagensRecebidas().add(usuario.getMensagensRecebidas().size(), mensagem);
        assert mensagem != null;
        return mensagem.getMensagem();
    }

    public SystemService getSystem() {
        return system;
    }
}