package domain.models.entities.entidadesDeServicio;

import domain.models.entities.db.EntidadPersistente;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "entidad_prestadora")
public class EntidadPrestadora extends EntidadPersistente {
    @Column
    private String nombre;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "entidad_prestadora_id")
    private List<Entidad> entidades;

    public EntidadPrestadora(String nombre) {
        this.nombre = nombre;
        this.entidades = new ArrayList<>();
    }

    public EntidadPrestadora() {
        this.entidades = new ArrayList<>();
    }

    public boolean estaAsociadoA(Entidad entidad){
        return this.entidades.contains(entidad);
    }

}

