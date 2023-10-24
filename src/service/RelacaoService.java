package service;
import br.ufal.ic.p2.jackut.Exceptions.AlreadyEnemyException;
import br.ufal.ic.p2.jackut.Exceptions.AutoRelationshipException;
import br.ufal.ic.p2.jackut.Exceptions.ExistentRelantionshipException;
import br.ufal.ic.p2.jackut.Exceptions.UserNotFoundException;
import br.ufal.ic.p2.jackut.SystemService;
import br.ufal.ic.p2.jackut.models.Usuario;

public class RelacaoService {
    private final SystemService system;
    private final RecadoService recadoService;

    public RelacaoService(SystemService system, RecadoService recadoService) {
        this.system = system;
        this.recadoService = recadoService;
    }

    public SystemService getSystem() {
        return system;
    }

    public boolean ehFa(String login, String idolo) throws UserNotFoundException {
        if (!system.getUsuarios().containsKey(login) || !this.system.getUsuarios().containsKey(idolo)) {
            throw new UserNotFoundException();
        }
        Usuario user = this.system.getUsuarios().get(login);
        Usuario idol = this.system.getUsuarios().get(idolo);
        if ((user == null) || (idol == null)) {
            throw new UserNotFoundException();
        }
        return idol.getFas().contains(user);
    }

    public void adicionarIdolo(Usuario user, Usuario idol) throws UserNotFoundException, ExistentRelantionshipException, AutoRelationshipException, AlreadyEnemyException {
        if (!system.getUsuarios().containsValue(idol)) {
            throw new UserNotFoundException();
        }
        if (user.equals(idol)) {
            throw new AutoRelationshipException("fã");
        }
        if (user.getIdolos().contains(idol)) {
            throw new ExistentRelantionshipException("Ídolo");
        }
        if (user.getInimigos().contains(idol)) {
            throw new AlreadyEnemyException(idol.getNome());
        }

        user.setIdolo(idol);
        idol.setFa(user);

    }

    public String getFas(Usuario user) throws UserNotFoundException {
        if (!this.system.getUsuarios().containsKey(user.getLogin())) {
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

    public void adicionarInimigo(Usuario user, Usuario enemy) throws ExistentRelantionshipException, AutoRelationshipException, UserNotFoundException {

        if (user.equals(enemy)) {
            throw new AutoRelationshipException("inimigo");
        }
        if (user.getInimigos().contains(enemy)) {
            throw new ExistentRelantionshipException("inimigo");
        }
        if (enemy == null) {
            throw new UserNotFoundException();
        }

        user.setInimigo(enemy);
        enemy.setInimigo(user);
    }



    public void adicionarPaquera(Usuario user, Usuario paquerado) throws UserNotFoundException, AlreadyEnemyException, ExistentRelantionshipException {

        if (user == null || paquerado == null) {
            throw new UserNotFoundException();
        }

        if (user.equals(paquerado)) {
            throw new AutoRelationshipException("paquera");
        }
        if (user.getPaqueras().contains(paquerado)) {
            throw new ExistentRelantionshipException("paquera");
        }
        if (user.getInimigos().contains(paquerado)) {
            throw new AlreadyEnemyException(paquerado.getNome());
        }

        if (user.getPaquerasRecebidas().contains(paquerado) || paquerado.getPaquerasRecebidas().contains(user)) {
            recadoService.enviarRecadoPaquera(user, paquerado, user.getNome() + " é seu paquera - Recado do Jackut."); // aqui chamaria o recadoService
            recadoService.enviarRecadoPaquera(paquerado, user, paquerado.getNome() + " é seu paquera - Recado do Jackut.");
        }
        //adiciona o parquerado a lista de paqueras do ususario
        user.getPaqueras().add(paquerado);
        //adiciona o usuario a lista de paqueras recebidas do paquerado
        paquerado.getPaquerasRecebidas().add(user);

    }

    public String getPaqueras(Usuario user) throws UserNotFoundException {
        if (user == null || !system.getUsuarios().containsKey(user.getLogin())) {
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

    public boolean ehPaquera(Usuario user, Usuario paquerado) {
        return user.getPaqueras().contains(paquerado);
    }

}
