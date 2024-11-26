package interfaces;

import dominio.AlumnoCurso;
import java.util.List;

public interface IAlumnoCursoDAO {
    
    public AlumnoCurso obten(int id);
    
    public AlumnoCurso obtenPorAlumnoYCurso(int idAlumno, int idCurso);
    
    public AlumnoCurso agregarAlumnoCurso(AlumnoCurso curso);
    
    public AlumnoCurso modificarAlumnoCurso(AlumnoCurso curso);
    
    public AlumnoCurso eliminarAlumnoCurso(int id);
    
    public List<AlumnoCurso> obtenerAlumnosCursos();
}
