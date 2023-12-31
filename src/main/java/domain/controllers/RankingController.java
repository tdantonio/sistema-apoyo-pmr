package domain.controllers;

import domain.models.entities.admins.AdminDePlataforma;
import domain.models.entities.admins.rankings.*;
import domain.models.entities.comunidad.Incidente;
import domain.models.entities.entidadesDeServicio.Entidad;
import domain.models.repositorios.*;
import domain.server.exceptions.CriterioNotSelectedException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RankingController extends Controller implements WithSimplePersistenceUnit {
    private RepositorioIncidentes repositorioIncidentes;


    public RankingController(RepositorioIncidentes repositorioIncidentes) {
        this.repositorioIncidentes = repositorioIncidentes;
    }

    public void index(Context context) {
        Map<String, Object> model = new HashMap<>();
        EntityManager entityManager = entityManager();
        AdminDePlataforma admin = super.adminLogueado(context, entityManager);
        model.put("nombre", admin.getNombre());
        context.render("admins/rankings/rankings.hbs", model);
    }

    public void generate(Context context) {
        String criterio_string = context.formParam("criterio_ranking");
        EntityManager entityManager = entityManager();
        CriterioRanking criterioRanking = this.criterioToEntity(Objects.requireNonNull(criterio_string));
        if(criterioRanking == null) {
            throw new CriterioNotSelectedException();
        }

        List<Incidente> incidentes = this.repositorioIncidentes.obtenerIncidentes(entityManager);

        GeneradorDeRankings generadorDeRankings = new GeneradorDeRankings(incidentes);
        List<Entidad> entidades = generadorDeRankings.generarSegunCriterio(criterioRanking);

        // les setteo la posicion en el ranking a cada entidad para usar en hbs
        entidades.forEach(e -> e.setIndex(entidades.indexOf(e)));

        Map<String, Object> model = new HashMap<>();

        AdminDePlataforma admin = super.adminLogueado(context, entityManager);
        model.put("nombre", admin.getNombre());
        model.put("entidades", entidades);
        model.put("criterio", this.criterioToText(criterio_string));
        context.render("admins/rankings/rankings.hbs", model);
    }

    public CriterioRanking criterioToEntity(String criterio_string) {
        return switch (criterio_string) {
            case "MayorPromedioCierre" -> new MayorPromedioCierre();
            case "MayorCantidadIncidentes" -> new MayorCantidadIncidentes();
            case "MayorGradoDeImpacto" -> new MayorGradoIncidentes();
            default -> null;
        };
    }

    public String criterioToText(String criterio_string) {
        return switch (criterio_string) {
            case "MayorPromedioCierre" -> "el mayor promedio de tiempo de cierre de incidentes";
            case "MayorCantidadIncidentes" -> "la mayor cantidad de incidentes reportados en la semana";
            case "MayorGradoDeImpacto" -> "el mayor grado de impacto en las problemáticas";
            default -> null;
        };
    }
}
