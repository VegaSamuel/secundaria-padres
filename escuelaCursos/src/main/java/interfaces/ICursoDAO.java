package interfaces;

import dominio.Curso;
import java.util.List;

/**
 *
 * @author Samuel Vega
 */
public interface ICursoDAO {
    
    public Curso obten(int id);
    
    public Curso agregarCurso(Curso curso);
    
    public Curso modificarCurso(Curso curso);
    
    public Curso eliminarCurso(int id);
    
    public List<Curso> obtenerCursos();
}
