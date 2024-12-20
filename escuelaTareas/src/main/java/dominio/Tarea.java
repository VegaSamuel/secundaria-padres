package dominio;

import java.sql.Date;

/**
 *
 * @author Samuel Vega
 */
public class Tarea {
    private int id;
    private String nombre;
    private Date fechaEntrega;
    private float calificacion;
    private int avaladoPadre;
    private int idCurso;

    public Tarea() {}

    public Tarea(int id, String nombre, Date fechaEntrega, float calificacion, int avaladoPadre, int idCurso) {
        this.id = id;
        this.nombre = nombre;
        this.fechaEntrega = fechaEntrega;
        this.calificacion = calificacion;
        this.avaladoPadre = avaladoPadre;
        this.idCurso = idCurso;
    }

    public Tarea(String nombre, int avaladoPadre, int idCurso) {
        this.nombre = nombre;
        this.avaladoPadre = avaladoPadre;
        this.idCurso = idCurso;
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

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    public int getAvaladoPadre() {
        return avaladoPadre;
    }

    public void setAvaladoPadre(int avaladoPadre) {
        this.avaladoPadre = avaladoPadre;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.id;
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
        final Tarea other = (Tarea) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Tarea{" + "id=" + id + ", nombre=" + nombre + ", avaladoPadre=" + avaladoPadre + ", idCurso=" + idCurso + '}';
    }

}
