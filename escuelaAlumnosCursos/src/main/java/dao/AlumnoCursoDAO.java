package dao;

import conexion.Conexion;
import dominio.AlumnoCurso;
import interfaces.IAlumnoCursoDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AlumnoCursoDAO implements IAlumnoCursoDAO {
    private final Connection conexion;
    
    public AlumnoCursoDAO() {
        conexion = Conexion.getConexion();
    }

    @Override
    public AlumnoCurso obten(int id) {
        String sql = "SELECT id, alumno_id, curso_id, calificacion_final FROM alumnos_cursos WHERE id = ?";
        AlumnoCurso alumnoCurso = new AlumnoCurso();
        
        try(
            PreparedStatement stmt = conexion.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    alumnoCurso.setId(id);
                    alumnoCurso.setIdAlumno(rs.getInt("alumno_id"));
                    alumnoCurso.setIdCurso(rs.getInt("curso_id"));
                    alumnoCurso.setCalificacion(rs.getFloat("calificacion_final"));
                }
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return alumnoCurso;
    }

    @Override
    public AlumnoCurso obtenPorAlumnoYCurso(int idAlumno, int idCurso) {
        String sql = "SELECT id, alumno_id, curso_id, calificacion_final FROM alumnos_cursos WHERE alumno_id = ? AND curso_id = ?";
        AlumnoCurso alumnoCurso = new AlumnoCurso();
        
        try(
            PreparedStatement stmt = conexion.prepareStatement(sql)
        ) {
            stmt.setInt(1, idAlumno);
            stmt.setInt(2, idCurso);
            
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    alumnoCurso.setId(rs.getInt("id"));
                    alumnoCurso.setIdAlumno(idAlumno);
                    alumnoCurso.setIdCurso(idCurso);
                    alumnoCurso.setCalificacion(rs.getFloat("calificacion_final"));
                }
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return alumnoCurso;
    }
    
    @Override
    public AlumnoCurso agregarAlumnoCurso(AlumnoCurso alumnoCurso) {
        String sql = "INSERT INTO alumnos_cursos(alumno_id, curso_id, calificacion_final) VALUES (?, ?, ?)";
        
        try(
            PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setInt(1, alumnoCurso.getIdAlumno());
            stmt.setInt(2, alumnoCurso.getIdCurso());
            stmt.setFloat(3, alumnoCurso.getCalificacion());
            stmt.executeUpdate();
            
            try(ResultSet llaves = stmt.getGeneratedKeys()) {
                if(llaves.next()) {
                    alumnoCurso.setId(llaves.getInt(1));
                }
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return alumnoCurso;
    }

    @Override
    public AlumnoCurso modificarAlumnoCurso(AlumnoCurso alumnoCurso) {
        String sql = "UPDATE alumnos_cursos SET alumno_id = ?, curso_id = ?, calificacion_final = ? WHERE = ?";
        
        try(
            PreparedStatement stmt = conexion.prepareStatement(sql)
        ) {
            stmt.setInt(1, alumnoCurso.getIdAlumno());
            stmt.setInt(2, alumnoCurso.getIdCurso());
            stmt.setFloat(3, alumnoCurso.getCalificacion());
            stmt.setInt(4, alumnoCurso.getId());
            
            int afectadas = stmt.executeUpdate();
            
            if(afectadas > 0) {
                return alumnoCurso;
            }else {
                System.out.println("No se encontró el curso o alumno");
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return null;
    }

    @Override
    public AlumnoCurso eliminarAlumnoCurso(int id) {
        AlumnoCurso alumnoCurso = new AlumnoCurso();
        String sql = "DELETE FROM alumno_cursos WHERE id = ?";
        
        try(
            PreparedStatement stmt = conexion.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            
            int afectadas = stmt.executeUpdate();
            
            if(afectadas > 0) {
                return alumnoCurso;
            }else {
                System.out.println("No se pudo eliminar el curso o alumno");
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return null;
    }

    @Override
    public List<AlumnoCurso> obtenerAlumnosCursos() {
        List<AlumnoCurso> alumnosCursos = new ArrayList<>();
        String sql = "SELECT id, alumno_id, curso_id, calificacion_final FROM alumno_cursos";
        
        try(
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while(rs.next()) {
                AlumnoCurso alumnoCurso = new AlumnoCurso();
                alumnoCurso.setId(rs.getInt("id"));
                alumnoCurso.setIdAlumno(rs.getInt("alumno_id"));
                alumnoCurso.setIdCurso(rs.getInt("curso_id"));
                alumnoCurso.setCalificacion(rs.getFloat("calificacion_final"));
                alumnosCursos.add(alumnoCurso);
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return alumnosCursos;
    }
    
}
