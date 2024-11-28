package interfaces;

import dominio.Tarea;
import java.util.List;

/**
 *
 * @author Samuel Vega
 */
public interface ITareaDAO {
    
    public Tarea obten(int id);
    
    public Tarea agregarTarea(Tarea tarea);
    
    public Tarea modificarTarea(Tarea tarea);
    
    public Tarea eliminarTarea(int id);
    
    public List<Tarea> obtenerTareas();
    
    public List<Tarea> obtenerTareasPorCurso(int idCurso);
}
