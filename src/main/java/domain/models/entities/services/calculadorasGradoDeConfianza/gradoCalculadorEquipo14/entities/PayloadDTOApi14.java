package domain.models.entities.services.calculadorasGradoDeConfianza.gradoCalculadorEquipo14.entities;

import domain.models.entities.comunidad.Comunidad;
import domain.models.entities.comunidad.GradoDeConfianza;
import domain.models.entities.comunidad.Incidente;
import domain.models.entities.comunidad.Usuario;
import domain.models.entities.converters.GradoDeConfianzaConstructor;
import domain.models.entities.entidadesDeServicio.Entidad;
import domain.models.entities.entidadesDeServicio.Establecimiento;
import domain.models.entities.entidadesDeServicio.PrestacionDeServicio;
import domain.models.entities.entidadesDeServicio.Servicio;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PayloadDTOApi14 {
    public List<UsuarioApi14> usuarios;
    public List<ComunidadApi14> comunidades;
    public List<IncidenteApi14> incidentes;

    public PayloadDTOApi14(){
        usuarios = new ArrayList<>();
        comunidades = new ArrayList<>();
        incidentes = new ArrayList<>();
    }

    public void cargar(List<Usuario> usuarios , List<Comunidad> comunidades, List<Incidente> incidentes){
        this.cargarUsuarios(usuarios);
        this.cargarAListaComunidadApi14(comunidades);
        this.cargarAListaIncidentesApi14(incidentes);
    }

    public void cargarAListaIncidentesApi14(List<Incidente> incidentes){
        incidentes.forEach(incidente -> this.cargarIncidente(incidente));
    }

    private void cargarAListaComunidadApi14(List<Comunidad> comunidades) {
        comunidades.forEach(comunidad -> this.cargarComunidad(comunidad));
    }

    private void cargarComunidad(Comunidad comunidad) {
        ComunidadApi14 comunidadApi14 = new ComunidadApi14();
        comunidadApi14.setId(comunidad.getId());
        comunidadApi14.setPuntosDeConfianza(comunidad.getPuntosDeConfianza());
        GradoDeConfianzaConstructor gradoDeConfianzaConstructor = new GradoDeConfianzaConstructor();
        comunidadApi14.setGradoDeConfianza(gradoDeConfianzaConstructor.cargarGradoAPartirDePuntos(comunidad.getPuntosDeConfianza()));

        comunidadApi14.setUsuarios(this.obtenerListaUsuarioApi14(comunidad));
    }

    private List<UsuarioApi14> obtenerListaUsuarioApi14(Comunidad comunidad) {
        List<UsuarioApi14> usuarioApi14s = new ArrayList<>();
        List<Usuario> usuarios = new ArrayList<>();

        usuarios.addAll(comunidad.obtenerUsuarioAPartirDeMiembros());
        usuarioApi14s.addAll(cargarUsuarios(usuarios));

        return usuarioApi14s;
    }

    private List<UsuarioApi14> cargarUsuarios(List<Usuario> usuarios) {
        List<UsuarioApi14> usuarioApi14s = new ArrayList<>();
        usuarios.forEach(usuario -> usuarioApi14s.add(this.cargarUsuario(usuario)));

        return usuarioApi14s;
    }

    public UsuarioApi14 cargarUsuario(Usuario usuario){
        UsuarioApi14 usuarioApi14 = new UsuarioApi14();
        usuarioApi14.setId(usuario.getId());
        usuarioApi14.setPuntosDeConfianza(usuario.getPuntosDeConfianza());
        usuarioApi14.setGradoDeConfianza(this.cargarGrado(usuario));

        return usuarioApi14;
    }

    public GradoDeConfianzaApi14 cargarGrado(Usuario usuario){
        GradoDeConfianzaApi14 gradoDeConfianza = new GradoDeConfianzaApi14();
        gradoDeConfianza.setPuntosMaximos(usuario.getGradoDeConfianza().getPuntosMaximos());
        gradoDeConfianza.setPuntosMinimos(usuario.getGradoDeConfianza().getPuntosMinimos());

        return gradoDeConfianza;
    }

    public IncidenteApi14 cargarIncidente(Incidente incidente){
        IncidenteApi14 incidenteApi14 = new IncidenteApi14();

        incidenteApi14.setId(incidente.getId());
        incidenteApi14.setEstado(incidente.getEstado());
        incidenteApi14.setDescripcion(incidente.getDescripcion());
        incidenteApi14.setFechaApertura(incidente.getFechaHoraApertura().toString());
        incidenteApi14.setFechaCierre(incidente.getFechaHoraCierre().toString());
        incidenteApi14.setUsuarioApertura(this.cargarUsuario(incidente.getUsuarioApertura()));
        incidenteApi14.setUsuarioCierre(this.cargarUsuario(incidente.getUsuarioCierre()));
        incidenteApi14.setPrestacionDeServicio(this.cargarPrestacion(incidente.getPrestacionDeServicio()));

        return incidenteApi14;
    }

    private PrestacionDeServicioApi14 cargarPrestacion(PrestacionDeServicio prestacionDeServicio) {
        PrestacionDeServicioApi14 prestacionDeServicioApi14 = new PrestacionDeServicioApi14();
        prestacionDeServicioApi14.setServicio(this.cargarServicio(prestacionDeServicio.getServicio()));
        prestacionDeServicioApi14.setEstablecimiento(this.cargarEstablecimiento(prestacionDeServicio.getEstablecimiento()));
        prestacionDeServicioApi14.setEntidad(this.cargarEntidad(prestacionDeServicio.getEntidad()));

        return prestacionDeServicioApi14;
    }

    public ServicioApi14 cargarServicio(Servicio servicio){
        ServicioApi14 servicioApi14 = new ServicioApi14();
        servicioApi14.setId(servicio.getId());
        servicioApi14.setEstado(servicio.getEstado());
        servicioApi14.setNombre(servicio.getNombre());

        return servicioApi14;
    }

    private EstablecimientoApi14 cargarEstablecimiento(Establecimiento establecimiento) {
        EstablecimientoApi14 establecimientoApi14 = new EstablecimientoApi14();
        establecimientoApi14.setId(establecimiento.getId());
        establecimientoApi14.setNombre(establecimiento.getNombre());

        return establecimientoApi14;
    }

    private EntidadApi14 cargarEntidad(Entidad entidad) {
        EntidadApi14 entidadApi14 = new EntidadApi14();
        entidadApi14.setId(entidad.getId());
        entidadApi14.setNombre(entidad.getNombre());

        return entidadApi14;
    }
}
