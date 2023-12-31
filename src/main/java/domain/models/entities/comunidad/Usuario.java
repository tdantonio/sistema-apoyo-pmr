package domain.models.entities.comunidad;

import domain.models.entities.converters.MedioConfiguradoAttributeConverter;
import domain.models.entities.db.EntidadPersistente;
import domain.models.entities.entidadesDeServicio.PrestacionDeServicio;
import domain.models.entities.localizacion.Localizacion;
import domain.models.entities.mensajes.Configuraciones.MedioConfigurado;
import domain.models.entities.mensajes.Configuraciones.TiempoConfigurado;
import domain.models.entities.validaciones.CredencialDeAcceso;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "usuario")
public class Usuario extends EntidadPersistente {

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private CredencialDeAcceso credencialDeAcceso;

    @Column(name = "mail", nullable = true)
    private String mail;

    @Column(name = "telefono", nullable = true)
    private String telefono;

    @OneToOne(cascade = CascadeType.ALL)
    private Interes interes;

    @Transient
    private Localizacion localizacion;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Miembro> miembros;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tiempo_configurado_id", nullable = true)
    private TiempoConfigurado tiempoConfigurado;

    @Column(nullable = true)
    @Convert(converter = MedioConfiguradoAttributeConverter.class)
    private MedioConfigurado medioConfigurado;

    @Column
    private double puntosDeConfianza;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "grado_confianza_id")
    private GradoDeConfianza gradoDeConfianza;

    public Usuario() {
        this.miembros = new ArrayList<>();
    }

    public void setLocalizacion(String provincia, String departamento, String direccion){
        Localizacion localizacionSet = new Localizacion();
        localizacionSet.setProvincia(provincia);
        localizacionSet.setDireccion(departamento, direccion);
        this.localizacion = localizacionSet;
    }

    public void agregarMiembros(Miembro ... miembros){
        Collections.addAll(this.miembros, miembros);
    }

    public void generarIncidente(String titulo, PrestacionDeServicio prestacionDeServicio, String descripcion){
        miembros.forEach(m -> m.getComunidad().generarIncidente(this, titulo, prestacionDeServicio, descripcion));
    }

    public void cerrarIncidente(Comunidad comunidad, Incidente incidente){
        comunidad.cerrarIncidente(incidente);
        incidente.setUsuarioCierre(this);
    }

    public boolean estaInteresadoEn(Incidente incidente){
        return this.interes.contieneEntidad(incidente.getPrestacionDeServicio().getEntidad()) &&
                this.interes.contieneServicio(incidente.getPrestacionDeServicio().getServicio());
    }

    public void mandarPendientes(){
        this.tiempoConfigurado.mandarPendientes(this);
    }

    public boolean estaCercaDe(Incidente incidente){
        return this.localizacion.estaCercaDe(incidente.getLocalizacion());
    }

    public List<Comunidad> obtenerComunidades(){
        return miembros.stream().map(Miembro::getComunidad).toList();
    }
}
