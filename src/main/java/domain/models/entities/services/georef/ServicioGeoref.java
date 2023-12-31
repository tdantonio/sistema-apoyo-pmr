package domain.models.entities.services.georef;

import domain.models.entities.services.ServicioAPI;
import domain.models.entities.services.georef.entities.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ServicioGeoref extends ServicioAPI implements Localizador{
    private static ServicioGeoref instancia = null;
    private static int maximaCantidadRegistrosDefault = 200;

    public ServicioGeoref(String urlApi) {
        super(urlApi);
    }


    public static ServicioGeoref instancia(String url){
        if(instancia == null){
            instancia = new ServicioGeoref(url);
        }
        return instancia;
    }

    public ListadoDeProvincias listadoDeProvincias(){
        GeorefService georefService = this.retrofit.create(GeorefService.class);
        Call<ListadoDeProvincias> requestProvinciasArgentinas = georefService.provincias(30);
        Response<ListadoDeProvincias> responseProvinciasArgentinas;
        try {
            responseProvinciasArgentinas = requestProvinciasArgentinas.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseProvinciasArgentinas.body();
    }

    public ListadoDeMunicipios listadoDeMunicipios(){
        GeorefService georefService = this.retrofit.create(GeorefService.class);
        Call<ListadoDeMunicipios> requestMunicipios = georefService.municipios(2000);
        Response<ListadoDeMunicipios> responseMunicipios;
        try {
            responseMunicipios = requestMunicipios.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseMunicipios.body();
    }

    public  ListadoDeDepartamentos listadoDeDepartamentos(){
        GeorefService georefService = this.retrofit.create(GeorefService.class);
        Call<ListadoDeDepartamentos> requestDepartamentos = georefService.departamentos(1000);
        Response<ListadoDeDepartamentos> responseDepartamentos;
        try {
            responseDepartamentos = requestDepartamentos.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseDepartamentos.body();
    }

    public Provincia provincia(String nombreProvincia){
        GeorefService georefService = this.retrofit.create(GeorefService.class);
        Call<ListadoDeProvincias> requestProvincia = georefService.provincias(nombreProvincia);
        Response<ListadoDeProvincias> responseProvincia;
        try {
            responseProvincia = requestProvincia.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ListadoDeProvincias provincias = responseProvincia.body();
        assert provincias != null;
        return provincias.provincias.get(0);
    }

    public ListadoDeMunicipios listadoDeMunicipiosDeProvincia(Provincia provincia) throws IOException {
        GeorefService georefService = this.retrofit.create(GeorefService.class);
        Call<ListadoDeMunicipios> requestListadoDeMunicipios = georefService.municipios(Math.toIntExact(provincia.getId()), "id, nombre", maximaCantidadRegistrosDefault);
        Response<ListadoDeMunicipios> responseListadoDeMunicipios = requestListadoDeMunicipios.execute();
        return responseListadoDeMunicipios.body();
    }

    public Municipio municipio(String municipio){
        GeorefService georefService = this.retrofit.create(GeorefService.class);
        Call<ListadoDeMunicipios> requestMunicipios = georefService.municipios(municipio);
        Response<ListadoDeMunicipios> responseMunicipio;
        try {
            responseMunicipio = requestMunicipios.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ListadoDeMunicipios municipios = responseMunicipio.body();
        assert municipios != null;
        return municipios.municipios.get(0);
    }

    public Departamento departamento(String departamento){
        GeorefService georefService = this.retrofit.create(GeorefService.class);
        Call<ListadoDeDepartamentos> requestDepartamentos = georefService.departamentos(departamento);
        Response<ListadoDeDepartamentos> responseDepartamento;
        try {
            responseDepartamento = requestDepartamentos.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ListadoDeDepartamentos departamentos = responseDepartamento.body();
        assert departamentos != null;
        return departamentos.departamentos.get(0);
    }

    public Direccion direccion(String departamento, String direccion){
        GeorefService georefService = this.retrofit.create(GeorefService.class);
        Call<ListaDeDirecciones> requestDirecciones = georefService.direcciones(direccion, departamento);
        Response<ListaDeDirecciones> responseDirecciones;
        try {
            responseDirecciones = requestDirecciones.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ListaDeDirecciones direcciones = responseDirecciones.body();
        assert direcciones != null;
        return direcciones.direcciones.get(0);
    }
}