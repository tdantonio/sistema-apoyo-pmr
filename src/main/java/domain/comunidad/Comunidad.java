package domain.comunidad;

import domain.Mensajes.Notificaciones.*;
import domain.entidadesDeServicio.PrestacionDeServicio;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Comunidad {
    private List<Miembro> miembros;
    private List<Incidente> incidentes;

    public  Comunidad(){
        this.miembros = new ArrayList<>();
        this.incidentes = new ArrayList<>();
    }
    public void notificarMiembros(Incidente incidente, TipoNotificacion notificacion){

        miembros.stream().filter(m -> m.estaInteresadoEn(incidente)).forEach( m -> {
            // TODO es realmente necesario el notificador? no
            notificacion.notificar(m, incidente);
        });
    }

    public void agregarMiembro(Miembro miembro) {
        miembros.add(miembro);
    }

    public void generarIncidente(PrestacionDeServicio prestacionDeServicio, String descripcion){
        Incidente nuevoIncidente = new Incidente(descripcion, prestacionDeServicio);
        nuevoIncidente.setEstado(false);
        nuevoIncidente.setFechaApertura(LocalDateTime.now());
        incidentes.add(nuevoIncidente);

        this.notificarMiembros(nuevoIncidente, new AperturaIncidente());
    }

    public void cerrarIncidente(Incidente incidente){
        incidente.setEstado(true);
        incidente.setFechaCierre(LocalDateTime.now());
        this.notificarMiembros(incidente, new CierreIncidente());
    }

    public void recibirLocalizacion(Miembro miembro){
        SugerenciaRevision notificacion = new SugerenciaRevision();
        List<Incidente> incidentesCercanos = incidentes.stream().filter(
                i -> miembro.getUsuario().getLocalizacion().estaCercaDe(i.getLocalizacion()) && i.getEstado()
        ).toList();
        incidentesCercanos.forEach(
                i -> notificacion.notificar(miembro, i));

    }
}
