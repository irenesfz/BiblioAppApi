
package clases;

import java.io.Serializable;
import java.util.Objects;


public class Mensaje implements Serializable {
    String mensaje;

    public Mensaje() {
    }

    public Mensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.mensaje);
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
        final Mensaje other = (Mensaje) obj;
        if (!Objects.equals(this.mensaje, other.mensaje)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mensaje{" + "mensaje=" + mensaje + '}';
    }
    
    
}
