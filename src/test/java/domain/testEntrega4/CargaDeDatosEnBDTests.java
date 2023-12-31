package domain.testEntrega4;

import domain.models.entities.builders.InteresBuilder;
import domain.models.entities.entidadesDeServicio.Entidad;
import domain.models.entities.entidadesDeServicio.Establecimiento;
import domain.models.entities.entidadesDeServicio.PrestacionDeServicio;
import domain.models.entities.entidadesDeServicio.Servicio;
import domain.models.entities.mensajes.Configuraciones.CuandoSucede;
import domain.models.entities.mensajes.Configuraciones.MensajeEmail;
import domain.models.entities.mensajes.MailSender;
import domain.models.entities.comunidad.*;
import domain.models.entities.validaciones.CredencialDeAcceso;
import domain.server.Server;
import domain.server.init.Initializer;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

public class CargaDeDatosEnBDTests implements SimplePersistenceTest {

    private Usuario fede;

    private CredencialDeAcceso credencialFede;
    private Entidad lineaMitre;
    private Servicio escaleraMecanica;
    private Establecimiento estacionRetiro;
    private Establecimiento estacionVillaBallester;
    private PrestacionDeServicio trenesArgentinos;
    private Comunidad comunidadHipoacusicosCABA;
    private Incidente incidente1;
    private Entidad utn;
    private Servicio banio;
    private Servicio escalera;
    private Interes interes;
    private Initializer init;

    @BeforeEach

    public void init(){
        EntityManager entityManager = entityManager();
        init = new Initializer();
        init.init(entityManager);

        credencialFede = new CredencialDeAcceso("elFede");
        fede = new Usuario();
        fede.setNombre("Federico");
        fede.setApellido("Moccia");
        fede.setMail("federico21433@hotmail.com");
        MailSender mailer = Mockito.mock(MailSender.class);
        MensajeEmail medio = new MensajeEmail(mailer);


        fede.setMedioConfigurado(medio);
        fede.setInteres(interes);
        fede.setCredencialDeAcceso(credencialFede);
        fede.setMedioConfigurado(new MensajeEmail());
        fede.setTiempoConfigurado(new CuandoSucede());
        fede.setPuntosDeConfianza(5);

        //servicio
        escaleraMecanica = new Servicio();
        escaleraMecanica.setNombre("Escalera Mecanica");
        escaleraMecanica.setEstado(true);

        estacionRetiro = new Establecimiento();
        estacionRetiro.setNombre("Estacion retiro");
        estacionRetiro.setLocalizacion("Buenos Aires", "Comuna 1", "Av. Dr. Jose M Ramos Mejia 1430");
        estacionRetiro.agregarServicios(escaleraMecanica);


        estacionVillaBallester = new Establecimiento();
        estacionVillaBallester.setNombre("Estacion Villa Ballester");
        estacionVillaBallester.setLocalizacion("Buenos Aires", "San Martin", "San Martin 4900");
        estacionVillaBallester.agregarServicios(escaleraMecanica);


        lineaMitre = new Entidad();
        lineaMitre.setNombre("Linea Mitre");
        lineaMitre.setLocalizacion("Buenos Aires");
        lineaMitre.agregarEstablecimientos(estacionRetiro, estacionVillaBallester);

        trenesArgentinos = new PrestacionDeServicio();
        trenesArgentinos.setEntidad(lineaMitre);
        trenesArgentinos.setServicio(escaleraMecanica);
        trenesArgentinos.setEstablecimiento(estacionVillaBallester);

        comunidadHipoacusicosCABA = new Comunidad();
        comunidadHipoacusicosCABA.generarIncidente(fede, "titulo",trenesArgentinos, "");

        incidente1 = new Incidente("Se rompio la barrera", trenesArgentinos);
        LocalDateTime fechaAperturaIncidente1 = LocalDateTime.of(2023, 3, 7, 9, 24);
        LocalDateTime fechaCierreIncidente1 = LocalDateTime.of(2023, 3, 9, 10, 54);
        incidente1.setFechaHoraApertura(fechaAperturaIncidente1);
        incidente1.setFechaHoraCierre(fechaCierreIncidente1);

        utn = new Entidad();
        utn.setNombre("UTN");

        escalera = new Servicio();
        escalera.setNombre("Escalera");

        InteresBuilder interesBuilder = new InteresBuilder();
        interes = interesBuilder.agregarEntidades(utn).agregarServicios(escalera, banio).construir();


    }

    @Test
    public void cargarUnosDatos(){

            withTransaction(() -> {
                entityManager().persist(fede);
            });
    }
}
