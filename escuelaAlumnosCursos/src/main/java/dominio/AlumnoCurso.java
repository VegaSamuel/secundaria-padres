package dominio;

public class AlumnoCurso {
    private int id;
    private int idAlumno;
    private int idCurso;
    private float calificacion;

    public AlumnoCurso() {}

    public AlumnoCurso(int idAlumno, int idCurso, float califiacion) {
        this.idAlumno = idAlumno;
        this.idCurso = idCurso;
        this.calificacion = califiacion;
    }

    public AlumnoCurso(int id, int idAlumno, int idCurso, float califiacion) {
        this.id = id;
        this.idAlumno = idAlumno;
        this.idCurso = idCurso;
        this.calificacion = califiacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float califiacion) {
        this.calificacion = califiacion;
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
        final AlumnoCurso other = (AlumnoCurso) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "AlumnoCurso{" + "id=" + id + ", idAlumno=" + idAlumno + ", idCurso=" + idCurso + ", califiacion=" + calificacion + '}';
    }

}
