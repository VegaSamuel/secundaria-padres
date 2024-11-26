package dao;

import conexion.Conexion;
import dominio.Curso;
import interfaces.ICursoDAO;
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
public class CursoDAO implements ICursoDAO {
    private final Connection conexion;
    
    public CursoDAO() {
        conexion = Conexion.getConexion();
    }

    @Override
    public Curso obten(int id) {
        String sql = "SELECT id, nombre FROM cursos WHERE id = ?";
        Curso curso = new Curso();
        
        try(
            PreparedStatement stmt = conexion.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    curso.setId(id);
                    curso.setNombre(rs.getString("nombre"));
                }
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return curso;
    }

    @Override
    public Curso agregarCurso(Curso curso) {
        String sql = "INSERT INTO cursos(id, nombre) VALUES (?, ?)";
        
        try(
            PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setInt(1, curso.getId());
            stmt.setString(2, curso.getNombre());
            stmt.executeUpdate();
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return curso;
    }

    @Override
    public Curso modificarCurso(Curso curso) {
        String sql = "UPDATE cursos SET nombre = ? WHERE = ?";
        
        try(
            PreparedStatement stmt = conexion.prepareStatement(sql)
        ) {
            stmt.setString(1, curso.getNombre());
            stmt.setInt(2, curso.getId());
            
            int afectadas = stmt.executeUpdate();
            
            if(afectadas > 0) {
                return curso;
            }else {
                System.out.println("No se encontró el curso");
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return null;
    }

    @Override
    public Curso eliminarCurso(int id) {
        Curso curso = new Curso();
        String sql = "DELETE FROM cursos WHERE id = ?";
        
        try(
            PreparedStatement stmt = conexion.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            
            int afectadas = stmt.executeUpdate();
            
            if(afectadas > 0) {
                return curso;
            }else {
                System.out.println("No se pudo eliminar el curso");
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return null;
    }

    @Override
    public List<Curso> obtenerCursos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT id, nombre FROM cursos";
        
        try(
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while(rs.next()) {
                Curso curso = new Curso();
                curso.setId(rs.getInt("id"));
                curso.setNombre(rs.getString("nombre"));
                cursos.add(curso);
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return cursos;
    }
    
}
