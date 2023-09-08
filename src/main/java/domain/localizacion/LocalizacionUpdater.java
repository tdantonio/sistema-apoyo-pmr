package domain.localizacion;

import domain.mensajes.Notificaciones.SugerenciaRevision;
import domain.comunidad.Incidente;
import domain.comunidad.Miembro;
import domain.comunidad.Usuario;
import domain.repositorios.RepoUsuarios;
import domain.repositorios.RepositorioComunidades;

import java.util.ArrayList;
import java.util.List;

public class LocalizacionUpdater {
    private static SistemaPosicionamiento sistemaPosicionamiento;
    private static List<Usuario> usuarios;

    public LocalizacionUpdater(SistemaPosicionamiento sistema) {
        sistemaPosicionamiento = sistema;
        usuarios = new ArrayList<>();
    }

    public static void agregarUsuarios(List<Usuario> usuariosList){
        usuarios.addAll(usuariosList);
    }

    public static void actualizarLocalizaciones(){
        usuarios.forEach(u -> u.setLocalizacion(sistemaPosicionamiento.getPosicion(u.getTelefono())));
        LocalizacionUpdater.verificarIncidentesCercanos();
    }

    public static void verificarIncidentesCercanos(){
        new RepoUsuarios().obtenerUsuarios().forEach(LocalizacionUpdater::notificarIncidentesCercanos);
    }

    public static void notificarIncidentesCercanos(Usuario usuario){
        List<Incidente> incidentesCercanosEInteresantes = usuario.obtenerComunidades()
                .stream().flatMap(c -> c.getIncidentes().stream())
                .filter(i -> usuario.estaInteresadoEn(i) && usuario.estaCercaDe(i))
                .toList();
        incidentesCercanosEInteresantes.forEach(i -> new SugerenciaRevision().notificar(usuario, i));
    }


    public static void main(String[] args) {
        List<Usuario> usuarios = RepositorioComunidades.obtenerMiembros().stream().map(Miembro::getUsuario).toList();
        LocalizacionUpdater.agregarUsuarios(usuarios);
        LocalizacionUpdater.actualizarLocalizaciones();
    }
}
