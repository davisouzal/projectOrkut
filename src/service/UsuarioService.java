package service;

import br.ufal.ic.p2.jackut.Exceptions.AtributoNaoPreenchidoException;
import br.ufal.ic.p2.jackut.Exceptions.UserNotFoundException;
import br.ufal.ic.p2.jackut.Exceptions.*;
import br.ufal.ic.p2.jackut.SystemService;
import br.ufal.ic.p2.jackut.models.Comunidade;
import br.ufal.ic.p2.jackut.models.Usuario;

public class UsuarioService {

    private final SystemService system;

    public UsuarioService(SystemService system){
        this.system = system;
    }

    public SystemService getSystem() {
        return system;
    }

    public String getUser(String login, Usuario user, String atributo) throws UserNotFoundException {
        if (system.getUsuarios().containsKey(login)) {
            if (atributo.equals("nome")) {
                return system.getUsuarios().get(login).getNome();
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

        if (!system.getUsuarios().containsKey(user.getLogin())) {
            system.getUsuarios().put(user.getLogin(), user);
        } else {
            throw new ContaJaExisteException();
        }
        user.addAtributo("nome", user.getNome());
    }

    public String getAtributoUsuario(String login, String atributo) throws UserNotFoundException, AtributoNaoPreenchidoException{
        Usuario user = system.getUsuarios().get(login);
        if (user == null) {
            throw new UserNotFoundException();
        }
        if(user.getAtributo(atributo) == null){
            throw new AtributoNaoPreenchidoException();
        }
        return user.getAtributo(atributo).getValor();
    }

    public void editarPerfil(String id, String atributo, String valor) throws UserNotFoundException{
        Usuario usuario = system.getSessoes().get(id);
        if (system.getUsuarioLogado() == null) {
            throw new UserNotFoundException();
        } else {
            system.getUsuarios().get(system.getUsuarioLogado().getLogin()).addAtributo(atributo, valor);
        }
    }

    public void removerUsuario(Usuario user, String id) {
        if (user == null) {
            throw new UserNotFoundException();
        }
        system.getUsuarios().remove(user.getLogin());
        system.getSessoes().remove(id);
        //remove membro da comunidade e verifica se eh dono, se for remove tambem
        for (Comunidade comunidade : system.getComunidades().values()) {
            comunidade.getMembros().remove(user);
            if (comunidade.getDono().equals(user)) {
                this.system.getComunidades().remove(comunidade.getNome());
                //remove os membros da comunidade tb
                for (Usuario usuario : comunidade.getMembros()) {
                    usuario.getComunidades().remove(comunidade.getNome());
                }
            }
        }

        //a comuidade do ususario
        user.getComunidades().clear();
        //relacionamentos e mensagens enviadas
        for (Usuario usuario : system.getUsuarios().values()) {
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
