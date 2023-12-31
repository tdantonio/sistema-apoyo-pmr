package domain.models.entities.services.calculadorasGradoDeConfianza.gradoCalculadorEquipo14.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IncidenteApi14 {
    public long id;
    public UsuarioApi14 usuarioApertura;
    public UsuarioApi14 usuarioCierre;
    public String descripcion;
    public Boolean estado;
    public PrestacionDeServicioApi14 prestacionDeServicio;
    public String fechaApertura;
    public String fechaCierre;
}
