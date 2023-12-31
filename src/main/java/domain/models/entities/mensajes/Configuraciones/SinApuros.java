package domain.models.entities.mensajes.Configuraciones;
import domain.models.entities.comunidad.Usuario;
import domain.models.entities.converters.LocalDateTimeAttributeConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Getter
@Setter
@Entity
@DiscriminatorValue("SinApuros")
public class SinApuros extends TiempoConfigurado {

    @ElementCollection
    @CollectionTable(name = "sin_apuros_horarios", joinColumns = @JoinColumn(name = "sin_apuros_id"))
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "horarios")
    private List<LocalDateTime> horarios;

    @ElementCollection
    @CollectionTable(name = "sin_apuros_notificaciones_pendientes", joinColumns = @JoinColumn(name = "sin_apuros_id"))
    @Column(name = "notificacion")
    private List<String> notificacionesPendientes;

    public SinApuros(){
        this.inicializarNotificacionesPendientes();
        this.horarios = new ArrayList<>();
    }

    public void inicializarNotificacionesPendientes(){
        this.notificacionesPendientes = new ArrayList<>();
    }

    public void agregarHorarios(LocalDateTime ... horarios){
        Collections.addAll(this.horarios, horarios);
    }

    @Override
    public void recibirNotificacion(Usuario usuario, String notificacion) {
        notificacionesPendientes.add(notificacion);
    }

    @Override
    public void mandarPendientes(Usuario usuario) {
        if(this.esHoradeMandarPendientes()){
            this.notificacionesPendientes.forEach(n -> usuario.getMedioConfigurado().enviarNotificacion(usuario, n));
        }
    }

    @Override
    public String discriminador() {
        return "SinApuros";
    }

    public boolean esHoradeMandarPendientes(){
        return horarios.stream().anyMatch(h -> h.equals(LocalDateTime.now()));
    }








}