package interfaces;

import dominio.Padre;
import java.util.List;

/**
 * Interfaz que define los métodos de persistencia del Padre.
 * @author Samuel Vega
 */
public interface IPadreDAO {
    
    /**
     * Obtiene un padre de la base de datos.
     * @param id ID del padre a obtener.
     * @return El padre con el ID correspondiente.
     */
    public Padre obten(int id);
    
    /**
     * Obtiene un padre por medio de su correo desde la base de datos.
     * @param email Correo del padre a obtener.
     * @return El padre con el correo correspondiente.
     */
    public Padre obtenPorEmail(String email);
    
    /**
     * Agrega un padre a la base de datos.
     * @param padre Padre que se quiere agregar.
     * @return Padre agregado.
     */
    public Padre agregarPadre(Padre padre);
    
    /**
     * Modifica un padre de la base de datos.
     * @param padre Padre que se quiere modificar.
     * @return Padre modificado.
     */
    public Padre modificarPadre(Padre padre);
    
    /**
     * Elimina un padre de la base de datos.
     * @param id ID del padre que se quiere eliminar.
     * @return Padre eliminado.
     */
    public Padre eliminarPadre(int id);
    
    /**
     * Obtiene todos los padres de la base de datos.
     * @return Todos los padres de la base de datos.
     */
    public List<Padre> obtenerPadres();
    
}
