package domain.models.repositorios;

import domain.models.entities.entidadesDeServicio.OrganismoDeControl;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class RepositorioOrganismoDeControl implements WithSimplePersistenceUnit {

    public void agregar(OrganismoDeControl organismoDeControl){
        if(this.obtenerOrganismoDeControl(organismoDeControl.getId()) == null){
            EntityTransaction tx = entityManager().getTransaction();
            tx.begin();
            entityManager().persist(organismoDeControl);
            tx.commit();
        }
    }

    public List<OrganismoDeControl> obtenerOrganismosDeControl(){
        String jpql = "SELECT e FROM OrganismoDeControl e";
        EntityTransaction tx = entityManager().getTransaction();
        TypedQuery<OrganismoDeControl> query = entityManager().createQuery(jpql, OrganismoDeControl.class);
        tx.begin();
        List<OrganismoDeControl> organismos = query.getResultList();
        tx.commit();
        return organismos;
    }

    public OrganismoDeControl obtenerOrganismoDeControl(Long id){
        OrganismoDeControl organismoDeControl = null;
        String jpql = "SELECT o FROM OrganismoDeControl o " +
                "WHERE o.id = :id";
        EntityTransaction tx = entityManager().getTransaction();
        TypedQuery<OrganismoDeControl> query = entityManager().createQuery(jpql, OrganismoDeControl.class);
        try {
            tx.begin();
            query.setParameter("id", id);
            organismoDeControl = query.getSingleResult();
            tx.commit();
        } catch (NoResultException e){
            organismoDeControl = null;
        } finally {
            if (tx != null && tx.isActive()){
                tx.rollback();
            }
        }
        return organismoDeControl;
    }
}
