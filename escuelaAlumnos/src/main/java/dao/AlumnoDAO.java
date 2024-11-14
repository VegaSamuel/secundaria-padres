package dao;

import conexion.Conexion;
import dominio.Alumno;
import interfaces.IAlumnoDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Samuel Vega
 */
public class AlumnoDAO implements IAlumnoDAO {
    private final Connection conexion;
    
    public AlumnoDAO() {
        this.conexion = Conexion.getConexion();
    }

    @Override
    public Alumno obten(int id) {
        String sql = "SELECT id, nombre, apellido, email, padre_id FROM alumnos WHERE id = ?";
        Alumno alumno = null;
        
        try(PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    alumno = new Alumno();
                    alumno.setId(id);
                    alumno.setNombre(rs.getString("nombre"));
                    alumno.setApellido(rs.getString("apellido"));
                    alumno.setEmail(rs.getString("email"));
                    alumno.setIdPadre(rs.getInt("padre_id"));
                }
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
            
        return alumno;
    }

    @Override
    public Alumno obtenPorEmail(String email) {
        String sql = "SELECT id, nombre, apellido, email, padre_id FROM alumnos WHERE id = ?";
        Alumno alumno = null;
        
        try(PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, email);
            
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    alumno = new Alumno();
                    alumno.setId(rs.getInt("id"));
                    alumno.setNombre(rs.getString("nombre"));
                    alumno.setApellido(rs.getString("apellido"));
                    alumno.setEmail(email);
                    alumno.setIdPadre(rs.getInt("padre_id"));
                }
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return alumno;
    }

    @Override
    public Alumno agregarAlumno(Alumno alumno) {
        String sql = "INSERT INTO alumnos(nombre, apellido, email, padre-id) VALUES(?, ?, ?, ?)";
        
        try(PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, alumno.getNombre());
            stmt.setString(2, alumno.getApellido());
            stmt.setString(3, alumno.getEmail());
            stmt.setInt(4, alumno.getIdPadre());
            stmt.executeUpdate();
            
            try(ResultSet llaves = stmt.getGeneratedKeys()) {
                if(llaves.next()) {
                    alumno.setId(llaves.getInt(1));
                }
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return alumno;
    }

    @Override
    public Alumno modificarAlumno(Alumno alumno) {
        String sql = "UPDATE alumnos SET nombre = ?, apellido = ?, email = ?, padre_id = ?";
        
        try(PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, alumno.getNombre());
            stmt.setString(2, alumno.getApellido());
            stmt.setString(3, alumno.getEmail());
            stmt.setInt(4, alumno.getIdPadre());
            
            int afectadas = stmt.executeUpdate();
            
            if(afectadas > 0) {
                return alumno;
            }else {
                System.out.println("No se encontró el alumno");
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return null;
    }

    @Override
    public Alumno eliminarAlumno(int id) {
        Alumno alumno = new Alumno();
        String sql = "DELETE FROM alumnos WHERE id = ?";
        
        try(PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            int afectadas = stmt.executeUpdate();
            
            if(afectadas > 0) {
                return alumno;
            }else {
                System.out.println("No se pudo eliminar al alumno");
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return null;
    }

    @Override
    public List<Alumno> obtenerAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido, email, padre_id FROM alumnos";
        
        try(
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while(rs.next()) {
                Alumno alumno = new Alumno();
                alumno.setId(rs.getInt("id"));
                alumno.setNombre(rs.getString("nombre"));
                alumno.setApellido(rs.getString("apellido"));
                alumno.setEmail(rs.getString("email"));
                alumno.setIdPadre(rs.getInt("padre_id"));
                alumnos.add(alumno);
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return alumnos;
    }
    
}
