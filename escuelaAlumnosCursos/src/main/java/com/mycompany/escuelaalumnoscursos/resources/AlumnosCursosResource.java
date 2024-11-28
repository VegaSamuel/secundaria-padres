package com.mycompany.escuelaalumnoscursos.resources;

import conexion.Conexion;
import dao.AlumnoCursoDAO;
import dominio.AlumnoCurso;
import interfaces.IAlumnoCursoDAO;
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

@Path("/alumnosCursos")
public class AlumnosCursosResource {
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAlumnoCurso(@PathParam("id") int id) {
        IAlumnoCursoDAO alumnosCursos = null;
        
        try {
            alumnosCursos = new AlumnoCursoDAO();
            AlumnoCurso alumnoCurso = alumnosCursos.obten(id);
            
            if(alumnoCurso != null) {
                return Response.ok(alumnoCurso).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch(Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener el curso del alumno").build();
        }finally {
            if(alumnosCursos != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @GET
    @Path("/{idCurso}/alumnos/{idAlumno}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAlumnoCursoByAlumnoAndCurso(@PathParam("idAlumno") int idAlumno, @PathParam("idCurso") int idCurso) {
        IAlumnoCursoDAO alumnosCursos = null;
        
        try {
            alumnosCursos = new AlumnoCursoDAO();
            AlumnoCurso alumnoCurso = alumnosCursos.obtenPorAlumnoYCurso(idAlumno, idCurso);
            
            if(alumnoCurso != null) {
                return Response.ok(alumnoCurso).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch(Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener el curso del alumno").build();
        }finally {
            if(alumnosCursos != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAlumnoCurso(AlumnoCurso alumnoCurso) {
        IAlumnoCursoDAO alumnosCursos = null;
        
        try {
            alumnosCursos = new AlumnoCursoDAO();
            AlumnoCurso nuevo = alumnosCursos.agregarAlumnoCurso(alumnoCurso);
            return Response.status(Response.Status.CREATED).entity(nuevo).build();
        }catch(Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener el curso del alumno").build();
        }finally {
            if(alumnosCursos != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyAlumnoCurso(@PathParam("id") int id, AlumnoCurso alumnoCurso) {
        IAlumnoCursoDAO alumnosCursos = null;
        
        try {
            alumnosCursos = new AlumnoCursoDAO();
            alumnoCurso.setId(id);
            AlumnoCurso actualizado = alumnosCursos.modificarAlumnoCurso(alumnoCurso);
            
            if(actualizado != null) {
                return Response.ok(actualizado).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch(Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener el curso del alumno").build();
        }finally {
            if(alumnosCursos != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeAlumnoCurso(@PathParam("id") int id) {
        IAlumnoCursoDAO alumnosCursos = null;
        
        try {
            alumnosCursos = new AlumnoCursoDAO();
            AlumnoCurso eliminado = alumnosCursos.eliminarAlumnoCurso(id);
        
            if(eliminado != null) {
                return Response.ok(eliminado).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener al eliminar el curso del alumno").build();
        }finally {
            if(alumnosCursos != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAlumnosCursos() {
        IAlumnoCursoDAO alumnosCursos = null;
        
        try {
            alumnosCursos = new AlumnoCursoDAO();
            List<AlumnoCurso> lAlumnosCursos = alumnosCursos.obtenerAlumnosCursos();
            return Response.ok(lAlumnosCursos).build();
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener la lista de alumnos y cursos").build();
        }finally {
            if(alumnosCursos != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @GET
    @Path("/alumnos/{idAlumno}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAlumnosCursosPorAlumno(@PathParam("idAlumno") int idAlumno) {
        IAlumnoCursoDAO alumnosCursos = null;
        
        try {
            alumnosCursos = new AlumnoCursoDAO();
            List<AlumnoCurso> lAlumnosCursos = alumnosCursos.obtenerAlumnosCursosPorAlumno(idAlumno);
            return Response.ok(lAlumnosCursos).build();
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener la lista de alumnos y cursos").build();
        }finally {
            if(alumnosCursos != null) {
                Conexion.cerrarConexion();
            }
        }
    }
}
