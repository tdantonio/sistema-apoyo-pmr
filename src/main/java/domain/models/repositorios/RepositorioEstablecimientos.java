package domain.models.repositorios;

import domain.models.entities.comunidad.Miembro;
import domain.models.entities.entidadesDeServicio.Establecimiento;
import domain.models.entities.entidadesDeServicio.Servicio;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityTransaction;
import java.util.List;

public class RepositorioEstablecimientos implements WithSimplePersistenceUnit {

    public List<Establecimiento> obtenerEstablecimientos(){
        return entityManager()
                .createQuery("from Establecimiento ")
                .getResultList();
    }

    public void agregar(Establecimiento establecimiento) {
        EntityTransaction tx = entityManager().getTransaction();
        tx.begin();
        entityManager().persist(establecimiento);
        tx.commit();
    }

    public Establecimiento obtenerEstablecimiento(Long id) {
        return entityManager().find(Establecimiento.class, id);
    }
}