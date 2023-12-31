package domain.server;

import domain.controllers.*;
import io.github.flbulgarelli.jpa.extras.perthread.PerThreadEntityManagerAccess;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.Entity;
import javax.persistence.EntityManager;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Router implements WithSimplePersistenceUnit {

    public void init() {
        Server.app().after((handler) -> {
            entityManager().clear();
        });

        Server.app().routes( () -> {
            get("/", ((LoginController) FactoryController.controller("login"))::index);
            get("login", ((LoginController) FactoryController.controller("login"))::show);
            post("login", ((LoginController) FactoryController.controller("login"))::login);
            get("registro", ((LoginController) FactoryController.controller("login"))::create);
            post("registro", ((LoginController) FactoryController.controller("login"))::save);

            get("index", ((UsuarioController) FactoryController.controller("usuario"))::index);
            get("perfil", ((UsuarioController) FactoryController.controller("usuario"))::perfil);
            post("perfil", ((UsuarioController) FactoryController.controller("usuario"))::update);

            get("quienes_somos", ((UsuarioController) FactoryController.controller("usuario"))::quienesSomos);

            get("incidentes", ((IncidenteController) FactoryController.controller("incidentes"))::index);
            get("incidentes/crear", ((IncidenteController) FactoryController.controller("incidentes"))::create);
            post("incidentes/{incidente_id}/cerrar/{comunidad_id}", ((IncidenteController) FactoryController.controller("incidentes"))::close);
            post("incidentes", ((IncidenteController) FactoryController.controller("incidentes"))::save);

            get("comunidades",((ComunidadController) FactoryController.controller("comunidades"))::index);

            get("miembros/{id}/admin",((ComunidadController) FactoryController.controller("comunidades"))::admin);
            get("miembros/{id}/ver",((ComunidadController) FactoryController.controller("comunidades"))::ver);
            post("miembros/{miembro_id}/baja", ((MiembroController) FactoryController.controller("miembros"))::baja);
            get("miembros/{miembro_id}/editar", ((MiembroController) FactoryController.controller("miembros"))::editar);
            get("miembros/{comunidad_id}/alta", ((MiembroController) FactoryController.controller("miembros"))::create);
            post("miembros/{comunidad_id}/alta", ((MiembroController) FactoryController.controller("miembros"))::save);
            post("miembros/{miembro_id}", ((MiembroController) FactoryController.controller("miembros"))::update);

            // Pantallas de admins
            get("admin/login", ((AdminController) FactoryController.controller("admin"))::show);
            post("admin/login", ((AdminController) FactoryController.controller("admin"))::login);
            get("admin/index", ((AdminController) FactoryController.controller("admin"))::index);

            get("admin/entidades_prestadoras", ctx -> {
                CargaDatosController controller = ((CargaDatosController) FactoryController.controller("carga_datos"));
                controller.show(ctx, "entidades_prestadoras");
            });
            post("admin/entidades_prestadoras", ctx -> {
                CargaDatosController controller = ((CargaDatosController) FactoryController.controller("carga_datos"));
                controller.upload(ctx, "entidades_prestadoras");
            });

            get("admin/organismos_de_control", ctx -> {
                CargaDatosController controller = ((CargaDatosController) FactoryController.controller("carga_datos"));
                controller.show(ctx, "organismos_de_control");
            });
            post("admin/organismos_de_control", ctx -> {
                CargaDatosController controller = ((CargaDatosController) FactoryController.controller("carga_datos"));
                controller.upload(ctx, "organismos_de_control");
            });

            get("admin/rankings", ((RankingController) FactoryController.controller("ranking"))::index);
            post("admin/ranking", ((RankingController) FactoryController.controller("ranking"))::generate);
            post("admin/rankings", ((RankingController) FactoryController.controller("ranking"))::generate);
        });
    }
}
