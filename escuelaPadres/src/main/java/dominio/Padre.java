package dominio;

/**
 * Define y permite crear un padre para el sistema.
 * @author Samuel Vega
 */
public class Padre {
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    
    /**
     * Constructor por omisión
     */
    public Padre() {}

    /**
     * Constructor que inicializa los atributos de la clase.
     * @param id ID del padre.
     * @param nombre Nombre del padre.
     * @param apellido Apellido del padre.
     * @param email Correo electrónico del padre.
     */
    public Padre(int id, String nombre, String apellido, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    /**
     * Constructor que inicializa los atributos de la clase sin ID.
     * @param nombre Nombre del padre.
     * @param apellido Apellido del padre.
     * @param email Correo electrónico del padre.
     */
    public Padre(String nombre, String apellido, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    // Getters & Setters
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.id;
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
        final Padre other = (Padre) obj;
        return this.id == other.id;
    }
    
    @Override
    public String toString() {
        return "Padre{" + "id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", email=" + email + '}';
    }

}
