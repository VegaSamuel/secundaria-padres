package dao;

import conexion.Conexion;
import dominio.Tarea;
import interfaces.ITareaDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Samuel Vega
 */
public class TareaDAO implements ITareaDAO {
    private final Connection conexion;
    
    public TareaDAO() {
        conexion = Conexion.getConexion();
    }
    
    @Override
    public Tarea obten(int id) {
        String sql = "SELECT id, nombre, avalado_padre, curso_id FROM tareas WHERE id = ?";
        Tarea tarea = new Tarea();
        
        try(
           PreparedStatement stmt = conexion.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    tarea.setId(id);
                    tarea.setNombre(rs.getString("nombre"));
                    tarea.setAvaladoPadre(rs.getInt("avalado_padre"));
                    tarea.setIdCurso(rs.getInt("curso_id"));
                }
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return tarea;
    }

    @Override
    public Tarea agregarTarea(Tarea tarea) {
        String sql = "INSERT INTO tareas(nombre, avalado_padre, curso_id) VALUES(?, ?, ?)";
        
        try(
            PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, tarea.getNombre());
            stmt.setInt(2, tarea.getAvaladoPadre());
            stmt.setInt(3, tarea.getIdCurso());
            stmt.executeUpdate();
            
            try(ResultSet llaves = stmt.getGeneratedKeys()) {
                if(llaves.next()) {
                    tarea.setId(llaves.getInt(1));
                }
            }
        } catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return tarea;
    }

    @Override
    public Tarea modificarTarea(Tarea tarea) {
        String sql = "UPDATE tareas SET nombre = ?, avalado_padre = ?, curso_id = ? WHERE id = ?";
        
        try (
            PreparedStatement stmt = conexion.prepareStatement(sql)
        ) {
            stmt.setString(1, tarea.getNombre());
            stmt.setInt(2, tarea.getAvaladoPadre());
            stmt.setInt(3, tarea.getIdCurso());
            stmt.setInt(4, tarea.getId());
            
            int afectadas = stmt.executeUpdate();
            
            if(afectadas > 0) {
                return tarea;
            }else {
                System.out.println("No se encontró la tarea");
            }
        } catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return null;
    }

    @Override
    public Tarea eliminarTarea(int id) {
        Tarea tarea = new Tarea();
        String sql = "DELETE FROM tareas WHERE id = ?";
        
        try(
            PreparedStatement stmt = conexion.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            
            int afectadas = stmt.executeUpdate();
            
            if(afectadas > 0) {
                return tarea;
            }else {
                System.out.println("No se pudo eliminar la tarea.");
            }
        } catch(Exception sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return null;
    }

    @Override
    public List<Tarea> obtenerTareas() {
        List<Tarea> tareas = new ArrayList<>();
        String sql = "SELECT id, nombre, avalado_padre, curso_id FROM tareas";
        
        try (
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ){
            while(rs.next()) {
                Tarea tarea = new Tarea();
                tarea.setId(rs.getInt("id"));
                tarea.setNombre(rs.getString("nombre"));
                tarea.setAvaladoPadre(rs.getInt("avalado_padre"));
                tarea.setIdCurso(rs.getInt("curso_id"));
                tareas.add(tarea);
            }  
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return tareas;
    }
    
}
