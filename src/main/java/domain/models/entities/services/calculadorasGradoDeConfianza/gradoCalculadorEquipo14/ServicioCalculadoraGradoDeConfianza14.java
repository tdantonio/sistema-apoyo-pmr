package domain.models.entities.services.calculadorasGradoDeConfianza.gradoCalculadorEquipo14;

import domain.models.entities.comunidad.Comunidad;
import domain.models.entities.comunidad.GradoDeConfianza;
import domain.models.entities.comunidad.Incidente;
import domain.models.entities.comunidad.Usuario;
import domain.models.entities.converters.GradoDeConfianzaConverter;
import domain.models.entities.services.calculadorasGradoDeConfianza.CalculadorDeConfianzaAdapter;
import domain.models.entities.services.calculadorasGradoDeConfianza.gradoCalculadorEquipo14.entities.ComunidadApi14;
import domain.models.entities.services.calculadorasGradoDeConfianza.gradoCalculadorEquipo14.entities.GradoDeConfianzaApi14;
import domain.models.entities.services.calculadorasGradoDeConfianza.gradoCalculadorEquipo14.entities.PayloadDTOApi14;
import domain.models.entities.services.calculadorasGradoDeConfianza.gradoCalculadorEquipo14.entities.UsuarioApi14;
import domain.models.entities.services.calculadorasGradoDeConfianza.gradoCalculadorEquipo5.GradoDeConfianza5Service;
import domain.models.entities.services.calculadorasGradoDeConfianza.gradoCalculadorEquipo5.entities.ComunidadApi5;
import domain.models.entities.services.calculadorasGradoDeConfianza.gradoCalculadorEquipo5.entities.ComunidadDevuelta;
import domain.models.entities.services.calculadorasGradoDeConfianza.gradoCalculadorEquipo5.entities.UsuarioApi5;
import domain.models.entities.services.calculadorasGradoDeConfianza.gradoCalculadorEquipo5.entities.UsuarioDevuelto;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServicioCalculadoraGradoDeConfianza14 implements CalculadorDeConfianzaAdapter {

    private static ServicioCalculadoraGradoDeConfianza14 instancia = null;

    private final String urlApi = "aca iría la url si el servidor estuviera corriendo";

    private Retrofit retrofit;

    private ServicioCalculadoraGradoDeConfianza14() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(urlApi)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ServicioCalculadoraGradoDeConfianza14 instancia(){
        if(instancia== null){
            instancia = new ServicioCalculadoraGradoDeConfianza14();
        }
        return instancia;
    }

    public PayloadDTOApi14 jsonDevuelto(PayloadDTOApi14 jsonComunidadUsuario) throws IOException{
        GradoDeConfianza14Service gradoDeConfianza14Service = this.retrofit.create((GradoDeConfianza14Service.class));
        Call<PayloadDTOApi14> requestGradoConfianzaUsuario = gradoDeConfianza14Service.usuarioComunidad(jsonComunidadUsuario);
        Response<PayloadDTOApi14> responseGradoConfianzaUsuario = requestGradoConfianzaUsuario.execute();
        return responseGradoConfianzaUsuario.body();
    }

    @Override
    public void calcularGradoConfianzaPara(List<Usuario> usuarios, List<Comunidad> comunidades, List<Incidente> incidentes) throws IOException {
        PayloadDTOApi14 json = new PayloadDTOApi14();

        json.cargar(usuarios, comunidades, incidentes);

        json = jsonDevuelto(json);

        List<UsuarioApi14> usuariosDevueltos = new ArrayList<>();
        List<ComunidadApi14> comunidadesDevueltas = new ArrayList<>();

        usuariosDevueltos.addAll(json.getUsuarios());
        comunidadesDevueltas.addAll(json.getComunidades());

        usuariosDevueltos.forEach(usuarioDevuelto -> {
            usuarios.forEach(usuario -> this.actualizarUsuario(usuario, usuarioDevuelto));
        });

        comunidadesDevueltas.forEach(comunidadDevuelta -> {
            comunidades.forEach(comunidad -> this.actualizarComunidad(comunidad, comunidadDevuelta));
        });
    }

    private void actualizarComunidad(Comunidad comunidad, ComunidadApi14 comunidadDevuelta) {
        comunidad.setPuntosDeConfianza(comunidadDevuelta.getPuntosDeConfianza());
        GradoDeConfianza gradoDeConfianza;
        double puntosMinimos = comunidadDevuelta.getGradoDeConfianza().getPuntosMinimos();
        double puntosMaximos = comunidadDevuelta.getGradoDeConfianza().getPuntosMaximos();
        gradoDeConfianza = GradoDeConfianzaConverter.crearGradoAPartirDePuntosMinYMax(puntosMinimos, puntosMaximos);
        comunidad.setGradoDeConfianza(gradoDeConfianza);
    }

    private void actualizarUsuario(Usuario usuario, UsuarioApi14 usuarioDevuelto) {
        usuario.setPuntosDeConfianza(usuarioDevuelto.getPuntosDeConfianza());
        GradoDeConfianza gradoDeConfianza;
        double puntosMinimos = usuarioDevuelto.getGradoDeConfianza().getPuntosMinimos();
        double puntosMaximos = usuarioDevuelto.getGradoDeConfianza().getPuntosMaximos();
        gradoDeConfianza = GradoDeConfianzaConverter.crearGradoAPartirDePuntosMinYMax(puntosMinimos, puntosMaximos);
        usuario.setGradoDeConfianza(gradoDeConfianza);
    }
}
