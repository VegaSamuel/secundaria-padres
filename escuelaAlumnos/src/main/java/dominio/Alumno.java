package dominio;

/**
 *
 * @author Samuel Vega
 */
public class Alumno {
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private int idPadre;

    public Alumno() {}

    public Alumno(int id, String nombre, String apellido, String email, int idPadre) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.idPadre = idPadre;
    }

    public Alumno(String nombre, String apellido, String email, int idPadre) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.idPadre = idPadre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdPadre() {
        return idPadre;
    }

    public void setIdPadre(int idPadre) {
        this.idPadre = idPadre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.id;
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
        final Alumno other = (Alumno) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Alumno{" + "id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", email=" + email + ", idPadre=" + idPadre + '}';
    }

}
