package service;

import br.ufal.ic.p2.jackut.Exceptions.EnemyRequestException;
import br.ufal.ic.p2.jackut.Exceptions.NaoHaRecadosException;
import br.ufal.ic.p2.jackut.Exceptions.SelfRecadoException;
import br.ufal.ic.p2.jackut.Exceptions.UserNotFoundException;
import br.ufal.ic.p2.jackut.models.Recado;
import br.ufal.ic.p2.jackut.models.Usuario;
import br.ufal.ic.p2.jackut.System;

import java.util.Map;

public class RecadoService {

    private final System system;
    public RecadoService( System system) {
        this.system = system;
    }

    public void enviarRecado(String id, String destinatario, String mensagem) throws UserNotFoundException {
        Usuario sender = system.getSessoes().get(id);
        Usuario receiver = system.getUsuarios().get(destinatario);

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

    public void enviarRecadoPaquera(Usuario remetente, Usuario destinatario, String mensagem) throws UserNotFoundException, SelfRecadoException {
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
        Usuario usuario = system.getSessoes().get(id);

        if (usuario == null) {
            throw new UserNotFoundException();
        }

        Recado recado = usuario.getRecado();

        if (recado == null) {
            throw new NaoHaRecadosException();
        }

        return recado.getRecado();
    }
}
