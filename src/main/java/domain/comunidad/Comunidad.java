package domain.comunidad;

import domain.Mensajes.Notificador;
import domain.entidadesDeServicio.Entidad;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class Comunidad {
    private List<Miembro> miembros;
    private List<Incidente> incidentes;

    public void notificarMiembros(String notificacion){

        miembros.forEach( m -> {
            Notificador.notificar(m, notificacion);
        });
    }

    public void generarIncidente(Entidad entidad, String descripcion){
        Incidente nuevoIncidente = new Incidente(descripcion, entidad);
        nuevoIncidente.setEstado(false);
        nuevoIncidente.setFechaApertura(LocalDate.now());
        incidentes.add(nuevoIncidente);
        this.notificarMiembros("Notificacion Generacion de nuevo Incidente");
    }

    public void cerrarIncidente(Incidente incidente){
        incidente.setEstado(true);
        incidente.setFechaCierre(LocalDate.now());
    }
    //Faltan cosas
}
