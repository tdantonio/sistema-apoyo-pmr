package domain.validaciones;

public class UsaCredencialesPorDefecto implements Validacion{

    @Override
    public boolean validar(CredencialDeAcceso credencialDeAcceso){
        return !credencialDeAcceso.getNombreUsuario().equals(credencialDeAcceso.getContrasenia());
    }
}
