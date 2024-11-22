package com.mycompany.escuelacursos.resources;

import conexion.Conexion;
import dao.CursoDAO;
import dominio.Curso;
import interfaces.ICursoDAO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 *
 * @author Samuel Vega
 */
@Path("/cursos")
public class CursosResource {
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurso(@PathParam("id") int id) {
        ICursoDAO cursos = null;
        
        try {
            cursos = new CursoDAO();
            Curso curso = cursos.obten(id);
            
            if(curso != null) {
                return Response.ok(curso).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch(Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener el curso").build();
        }finally {
            if(cursos != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCurso(Curso curso) {
        ICursoDAO cursos = null;
        
        try {
            cursos = new CursoDAO();
            cursos.agregarCurso(curso);
            return Response.status(Response.Status.CREATED).entity(curso).build();
        }catch(Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al añadir el curso").build();
        }finally {
            if(cursos != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyCurso(@PathParam("id") int id, Curso curso) {
        ICursoDAO cursos = null;
        
        try {
            cursos = new CursoDAO();
            curso.setId(id);
            Curso actualizado = cursos.modificarCurso(curso);
            
            if(actualizado != null) {
                return Response.ok(actualizado).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch(Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al modificar el curso").build();
        }finally {
            if(cursos != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCurso(@PathParam("id") int id) {
        ICursoDAO cursos = null;
        
        try {
            cursos = new CursoDAO();
            Curso eliminado = cursos.eliminarCurso(id);
            
            if(eliminado != null) {
                return Response.ok(eliminado).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch(Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al eliminar el curso").build();
        }finally {
            if(cursos != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCursos() {
        ICursoDAO padres = null;
        
        try {
            padres = new CursoDAO();
            List<Curso> lCursos = padres.obtenerCursos();
            return Response.ok(lCursos).build();
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener la lista de padres").build();
        }finally {
            if(padres != null) {
                Conexion.cerrarConexion();
            }
        }
    }
}
