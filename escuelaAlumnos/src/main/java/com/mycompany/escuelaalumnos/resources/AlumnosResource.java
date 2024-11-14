package com.mycompany.escuelaalumnos.resources;

import conexion.Conexion;
import dao.AlumnoDAO;
import dominio.Alumno;
import interfaces.IAlumnoDAO;
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
@Path("/alumnos")
public class AlumnosResource {
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPadre(@PathParam("id") int id) {
        IAlumnoDAO alumnos = null;
        
        try {
            alumnos = new AlumnoDAO();
            Alumno alumno = alumnos.obten(id);
            
            if(alumno != null) {
                return Response.ok(alumno).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener al alumno").build();
        }finally {
            if(alumnos != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPadre(Alumno alumno) {
        IAlumnoDAO alumnos = null;
        
        try {
            alumnos = new AlumnoDAO();
            Alumno nuevo = alumnos.agregarAlumno(alumno);
            return Response.status(Response.Status.CREATED).entity(nuevo).build();
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al añadir al alumno").build();
        }finally {
            if(alumnos != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyPadre(@PathParam("id") int id, Alumno alumno) {
        IAlumnoDAO alumnos = null;
        
        try {
            alumnos = new AlumnoDAO();
            alumno.setId(id);
            Alumno actualizado = alumnos.modificarAlumno(alumno);

            if(actualizado != null) {
                return Response.ok(actualizado).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al modificar al alumno").build();
        }finally {
            if(alumnos != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePadre(int id) {
        IAlumnoDAO alumnos = null;
        
        try {
            alumnos = new AlumnoDAO();
            Alumno eliminado = alumnos.eliminarAlumno(id);
        
            if(eliminado != null) {
                return Response.ok(eliminado).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener al eliminar al alumno").build();
        }finally {
            if(alumnos != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPadres() {
        IAlumnoDAO alumnos = null;
        
        try {
            alumnos = new AlumnoDAO();
            List<Alumno> lAlumnos = alumnos.obtenerAlumnos();
            return Response.ok(lAlumnos).build();
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener la lista de alumnos").build();
        }finally {
            if(alumnos != null) {
                Conexion.cerrarConexion();
            }
        }
    }
}
