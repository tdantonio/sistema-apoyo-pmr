package domain.models.entities.entidadesDeServicio;

import domain.models.entities.db.EntidadPersistente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "prestacion")
public class PrestacionDeServicio extends EntidadPersistente {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "entidad_id", referencedColumnName = "id")
    private Entidad entidad;

    @ManyToOne
    @JoinColumn(name = "establecimiento_id", referencedColumnName = "id")
    private Establecimiento establecimiento;

    @ManyToOne
    @JoinColumn(name = "servicio_id", referencedColumnName = "id")
    private Servicio servicio;

    public boolean esLaMismaQue(PrestacionDeServicio prestacion){
        return this.entidad == prestacion.getEntidad() && this.establecimiento == prestacion.getEstablecimiento() && this.servicio == prestacion.getServicio();
    }

}
