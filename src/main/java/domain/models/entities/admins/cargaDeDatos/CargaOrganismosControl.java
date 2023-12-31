package domain.models.entities.admins.cargaDeDatos;

import domain.models.entities.entidadesDeServicio.OrganismoDeControl;
import lombok.Getter;

@Getter
public class CargaOrganismosControl extends CargaDatosTemplate <OrganismoDeControl>{

    public CargaOrganismosControl(String token) {
        super(token);
    }

    @Override
    public OrganismoDeControl transformarLinea(String[] campos) {
        String nombre = campos[0];
        return new OrganismoDeControl(nombre);
    }
}