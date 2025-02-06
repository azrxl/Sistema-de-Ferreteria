package cr.ac.una.proxy.objects;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    String identificacion;
    String contrasena;

    public User(String identificacion, String contrasena) {
        this.identificacion = identificacion;
        this.contrasena = contrasena;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.identificacion);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(this.identificacion, other.identificacion);
    }

}

