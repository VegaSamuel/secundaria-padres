package dao;

import conexion.Conexion;
import dominio.Padre;
import interfaces.IPadreDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Detalla los métodos de persistencia del padre.
 * @author Samuel Vega
 */
public class PadreDAO implements IPadreDAO {
    private final Connection conexion;
    
    public PadreDAO() {
        this.conexion = Conexion.getConexion();
    }
    
    @Override
    public Padre obten(int id) {      
        String sql = "SELECT id, nombre, email FROM padres WHERE id = ?";
        Padre padre = new Padre();
        
        try(
           PreparedStatement stmt = conexion.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    padre.setId(id);
                    padre.setNombre(rs.getString("nombre"));
                    padre.setEmail(rs.getString("email"));
                }
            }
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return padre;
    }
    
    @Override
    public Padre obtenPorEmail(String email) {
        String sql = "SELECT id, nombre, email FROM padres WHERE email = ?";
        Padre padre = null;
        
        try (
            PreparedStatement stmt = conexion.prepareStatement(sql) 
        ) {
            stmt.setString(1, email);
            
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    padre = new Padre();
                    padre.setId(rs.getInt("id"));
                    padre.setNombre(rs.getString("nombre"));
                    padre.setEmail(rs.getString("email"));
                }
            }
        } catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        } 
        
        return padre;
    }

    @Override
    public Padre agregarPadre(Padre padre) {
        String sql = "INSERT INTO padres(nombre, email) VALUES(?, ?)";
        
        try(
            PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, padre.getNombre());
            stmt.setString(2, padre.getEmail());
            stmt.executeUpdate();
            
            try(ResultSet llaves = stmt.getGeneratedKeys()) {
                if(llaves.next()) {
                    padre.setId(llaves.getInt(1));
                }
            }
        } catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return padre;
    }
    
    @Override
    public Padre modificarPadre(Padre padre) {
        String sql = "UPDATE padres SET nombre = ?, email = ? WHERE id = ?";
        
        try (
            PreparedStatement stmt = conexion.prepareStatement(sql)
        ) {
            stmt.setString(1, padre.getNombre());
            stmt.setString(2, padre.getEmail());
            stmt.setInt(3, padre.getId());
            
            int afectadas = stmt.executeUpdate();
            
            if(afectadas > 0) {
                return padre;
            }else {
                System.out.println("No se encontró el padre");
            }
        } catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return null;
    }

    @Override
    public Padre eliminarPadre(int id) {
        Padre padre = new Padre();
        String sql = "DELETE FROM padres WHERE id = ?";
        
        try(
            PreparedStatement stmt = conexion.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            
            int afectadas = stmt.executeUpdate();
            
            if(afectadas > 0) {
                return padre;
            }else {
                System.out.println("No se pudo eliminar el padre.");
            }
        } catch(Exception sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return null;
    }

    @Override
    public List<Padre> obtenerPadres() {
        List<Padre> padres = new ArrayList<>();
        String sql = "SELECT id, nombre, email FROM padres";
        
        try (
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ){
            while(rs.next()) {
                Padre padre = new Padre();
                padre.setId(rs.getInt("id"));
                padre.setNombre(rs.getString("nombre"));
                padre.setEmail(rs.getString("email"));
                padres.add(padre);
            }  
        }catch(SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        
        return padres;
    }
    
}
