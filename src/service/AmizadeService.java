package service;

import br.ufal.ic.p2.jackut.Exceptions.*;
import br.ufal.ic.p2.jackut.SystemService;
import br.ufal.ic.p2.jackut.models.Usuario;

import static br.ufal.ic.p2.jackut.Facade.USER_NOT_FOUND;

public class AmizadeService {

    private final SystemService system;
    public AmizadeService(SystemService system) {
        this.system = system;
    }

    public Boolean ehAmigo(String login, String loginAmigo){
        Usuario user = system.getUsuarios().get(login);
        Usuario userAmigo = system.getUsuarios().get(loginAmigo);
        return user.getAmigo().contains(userAmigo) && userAmigo.getAmigo().contains(user);
    }

    public void adicionarAmigo(String id, String loginAmigo) throws UserNotFoundException, AmigoJaAdicionadoException, EsperandoConviteException, SelfRequestException, EnemyRequestException {
        //pega o usuario das sessoes com o seu id de sessao
        Usuario user = system.getSessoes().get(id);
        if (user == null) {
            throw new RuntimeException(USER_NOT_FOUND);
        }
        system.login(user.getLogin(), user.getSenha());
        Usuario userAmigo = system.getUsuarios().get(loginAmigo);
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
        Usuario user = system.getUsuarios().get(login);
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
}
