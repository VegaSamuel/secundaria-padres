package interfaces;

import dominio.Alumno;
import java.util.List;

/**
 *
 * @author Samuel Vega
 */
public interface IAlumnoDAO {
    
    public Alumno obten(int id);
    
    public Alumno obtenPorEmail(String email);
    
    public Alumno agregarAlumno(Alumno alumno);
    
    public Alumno modificarAlumno(Alumno alumno);
    
    public Alumno eliminarAlumno(int id);
    
    public List<Alumno> obtenerAlumnos();
}
