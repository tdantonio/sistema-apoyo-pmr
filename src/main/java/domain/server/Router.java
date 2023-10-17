package domain.server;

import domain.controllers.*;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Router {

    public static void init() {
        Server.app().routes( () -> {
            get("/", ((LoginController) FactoryController.controller("login"))::index);
            get("login", ((LoginController) FactoryController.controller("login"))::show);
            post("login", ((LoginController) FactoryController.controller("login"))::login);
            get("registro", ((LoginController) FactoryController.controller("login"))::create);
            post("registro", ((LoginController) FactoryController.controller("login"))::save);
            get("index", (context -> context.render("/index.hbs")));

            get("incidentes", ((IncidenteController) FactoryController.controller("incidentes"))::index);
            get("incidentes/crear", ((IncidenteController) FactoryController.controller("incidentes"))::create);
            get("incidentes/{id}", ((IncidenteController) FactoryController.controller("incidentes"))::show);
            get("incidentes/{id}/cerrar", ((IncidenteController) FactoryController.controller("incidentes"))::close);
            get("incidentes/{id}/editar", ((IncidenteController) FactoryController.controller("incidentes"))::edit);
            post("incidentes", ((IncidenteController) FactoryController.controller("incidentes"))::save);
            post("incidentes/{id}", ((IncidenteController) FactoryController.controller("incidentes"))::update); // no es put pq form de html no acepta put
            delete("incidentes/{id}", ((IncidenteController) FactoryController.controller("incidentes"))::delete);

            get("comunidades",((ComunidadController) FactoryController.controller("comunidades"))::index);

            get("miembros/{id}/admin",((ComunidadController) FactoryController.controller("comunidades"))::admin);
            get("miembros/{id}/ver",((ComunidadController) FactoryController.controller("comunidades"))::ver);
            post("miembros/{miembro_id}/baja", ((MiembroController) FactoryController.controller("miembros"))::baja);
            get("miembros/{miembro_id}/editar", ((MiembroController) FactoryController.controller("miembros"))::editar);
            get("miembros/{comunidad_id}/alta", ((MiembroController) FactoryController.controller("miembros"))::create);
            post("miembros", ((MiembroController) FactoryController.controller("miembros"))::save);
            post("miembros/{miembro_id}", ((MiembroController) FactoryController.controller("miembros"))::update);

            get("/entidades_prestadoras", ctx -> {
                CargaDatosController controller = ((CargaDatosController) FactoryController.controller("carga_datos"));
                controller.show(ctx, "cargaEntidades.hbs");
            });
            post("/entidades_prestadoras", ctx -> {
                CargaDatosController controller = ((CargaDatosController) FactoryController.controller("carga_datos"));
                controller.upload(ctx, "entidades_prestadoras");
            });

            get("/organismos_de_control", ctx -> {
                CargaDatosController controller = ((CargaDatosController) FactoryController.controller("carga_datos"));
                controller.show(ctx, "cargaOrganismos.hbs");
            });
            post("/organismos_de_control", ctx -> {
                CargaDatosController controller = ((CargaDatosController) FactoryController.controller("carga_datos"));
                controller.upload(ctx, "organismos_de_control");
            });

            get("/rankings", ((RankingController) FactoryController.controller("ranking"))::index);
            post("/ranking", ((RankingController) FactoryController.controller("ranking"))::generate);
            get("/ranking/{criterio}", ((RankingController) FactoryController.controller("ranking"))::show);
        });
    }
}
